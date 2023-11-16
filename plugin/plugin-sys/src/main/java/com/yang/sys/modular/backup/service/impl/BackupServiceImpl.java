package com.yang.sys.modular.backup.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.common.enums.CommonSortOrderEnum;
import com.yang.common.exception.CommonException;
import com.yang.common.page.CommonPageRequest;
import com.yang.common.util.CommonCommandUtil;
import com.yang.common.util.CommonFileUtil;
import com.yang.sys.core.constant.DevConstant;
import com.yang.sys.modular.backup.entity.BackupRecord;
import com.yang.sys.modular.backup.enums.BackupTypeEnum;
import com.yang.sys.modular.backup.mapper.BackupMapper;
import com.yang.sys.modular.backup.param.*;
import com.yang.sys.modular.backup.result.BackupConfigResult;
import com.yang.sys.modular.backup.service.BackupService;
import com.yang.sys.modular.backup.task.BackupDbTaskRunner;
import com.yang.sys.modular.config.entity.DevConfig;
import com.yang.sys.modular.config.param.DevConfigBatchParam;
import com.yang.sys.modular.config.service.DevConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Slf4j
@Service
public class BackupServiceImpl extends ServiceImpl<BackupMapper, BackupRecord> implements BackupService {

    private static final String DB_USERNAME_KEY = "spring.datasource.datasource.username";

    private static final String DB_PASSWORD_KEY = "spring.datasource.datasource.password";

    @Resource
    private DevConfigService devConfigService;

    @Resource
    private Environment environment;

    @Override
    public void config(BackupConfigParam backupConfigParam) {
        checkParam(backupConfigParam);
        devConfigService.editBatch(Arrays.asList(new DevConfigBatchParam(DevConstant.BACKUP_PATH_KEY, backupConfigParam.getPath()),
                new DevConfigBatchParam(DevConstant.BACKUP_FREQUENCY_KEY, backupConfigParam.getFrequency())));
        this.restartJob();
    }

    private void checkParam(BackupConfigParam backupConfigParam) {
        if (!StrUtil.isAllNotBlank(backupConfigParam.getPath(), backupConfigParam.getFrequency())) {
            throw new CommonException("备份路径和周期不可为空!");
        }
    }

    @Override
    public QueryWrapper<BackupRecord> getQueryWrapper(BackupQueryParam backupQueryParam) {
        QueryWrapper<BackupRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(BackupRecord::getId);
        return queryWrapper;
    }

    @Override
    public Page<BackupRecord> page(BackupPageParam backupPageParam) {
        QueryWrapper<BackupRecord> queryWrapper = getQueryWrapper(BeanUtil.toBean(backupPageParam, BackupQueryParam.class));
        if (ObjectUtil.isAllNotEmpty(backupPageParam.getSortField(), backupPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(backupPageParam.getSortOrder());
            queryWrapper.orderBy(true, backupPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(backupPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(BackupRecord::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public List<BackupRecord> list(BackupListParam backupListParam) {
        QueryWrapper<BackupRecord> queryWrapper = getQueryWrapper(BeanUtil.toBean(backupListParam, BackupQueryParam.class));
        return this.list(queryWrapper);
    }

    @Override
    public void add(BackupAddParam backupAddParam) {
        if (StrUtil.isAllNotBlank(backupAddParam.getBackupName(), backupAddParam.getDataSize())) {
            BackupRecord backupInfo = BeanUtil.toBean(backupAddParam, BackupRecord.class);
            backupInfo.setBackupTime(DateTime.now());
            this.save(backupInfo);
        }
    }

    @Override
    public BackupRecord queryEntity(String id) {
        BackupRecord backupInfo = this.getById(id);
        if (ObjectUtil.isEmpty(backupInfo)) {
            throw new CommonException("备份信息不存在，id值为：{}", id);
        }
        return backupInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void restore(String backupId) {
        BackupRecord backupInfo = this.queryEntity(backupId);
        BackupConfigResult backupDbParam = getBackupConfigResult();
        String backupFullPath = backupDbParam.getBackupPath() + File.separator + backupInfo.getBackupName();

        try {
            CommonCommandUtil.restoreDb(backupDbParam.getDbHost(), backupDbParam.getDbPort(), environment.getProperty(DB_USERNAME_KEY)
                    , environment.getProperty(DB_PASSWORD_KEY), backupDbParam.getDbName(), backupFullPath);
        } catch (IOException | InterruptedException exception) {
            throw new CommonException("还原数据库备份失败!");
        }
    }

    @Override
    public void backup(BackupConfigResult backupConfigResult) {
        String backupName = backupConfigResult.getBackupName();
        String backupFullPath = backupConfigResult.getBackupPath() + File.separator + backupName;

        try {
            CommonCommandUtil.backupDb(backupConfigResult.getDbHost(), backupConfigResult.getDbPort(), environment.getProperty(DB_USERNAME_KEY)
                    , environment.getProperty(DB_PASSWORD_KEY), backupConfigResult.getDbName(), backupFullPath);
            // 添加备份记录
            long size = FileUtil.size(new File(backupFullPath));
            BackupAddParam backupAddParam = new BackupAddParam();
            backupAddParam.setBackupName(backupName);
            backupAddParam.setBackupType(BackupTypeEnum.MANUAL.getValue());
            backupAddParam.setDataSize(CommonFileUtil.formatFileSize(size));
            this.add(backupAddParam);
        } catch (Exception exception) {
            throw new CommonException("备份失败!");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void performManual() {
        BackupConfigResult backupDbParam = getBackupConfigResult();
        this.backup(backupDbParam);
    }

    /**
     * 开始自动备份任务（根据实时配置信息）
     */
    @Override
    public void restartJob() {
        BackupConfigResult backupConfigResult = this.getBackupConfigResult();
        String backupFrequency = backupConfigResult.getBackupFrequency();

        CronUtil.remove(DevConstant.BACKUP_KEY);
        if (StrUtil.isNotBlank(backupFrequency)) {
            CronUtil.schedule(DevConstant.BACKUP_KEY, backupFrequency, () -> {
                SpringUtil.getBean(BackupDbTaskRunner.class).action();
            });
        }
    }

    /**
     * 备份配置
     */
    @Override
    public BackupConfigResult getBackupConfigResult() {
        BackupConfigResult backupDbParam = new BackupConfigResult();
        List<DevConfig> devConfigList
                = devConfigService.list(new LambdaQueryWrapper<DevConfig>().eq(DevConfig::getCategory, DevConstant.BACKUP_KEY));
        for (DevConfig config : devConfigList) {
            if (DevConstant.BACKUP_PATH_KEY.equals(config.getConfigKey())) {
                backupDbParam.setBackupPath(config.getConfigValue());
            }
            if (DevConstant.BACKUP_FREQUENCY_KEY.equals(config.getConfigKey())) {
                backupDbParam.setBackupFrequency(config.getConfigValue());
            }
            if (DevConstant.BACKUP_DB_NAME_KEY.equals(config.getConfigKey())) {
                backupDbParam.setDbName(config.getConfigValue());
            }
            if (DevConstant.BACKUP_DB_HOST_KEY.equals(config.getConfigKey())) {
                backupDbParam.setDbHost(config.getConfigValue());
            }
            if (DevConstant.BACKUP_DB_PORT.equals(config.getConfigKey())) {
                backupDbParam.setDbPort(config.getConfigValue());
            }
        }

        return backupDbParam;
    }
}

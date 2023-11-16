package com.yang.sys.modular.config.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.common.enums.CommonSortOrderEnum;
import com.yang.common.exception.CommonException;
import com.yang.common.page.CommonPageRequest;
import com.yang.sys.modular.config.entity.DevConfig;
import com.yang.sys.modular.config.enums.DevConfigCategoryEnum;
import com.yang.sys.modular.config.mapper.DevConfigMapper;
import com.yang.sys.modular.config.param.*;
import com.yang.sys.modular.config.service.DevConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 配置Service接口实现类
 *
 * @author: yangjianzhi
 * @version1.0
 */
@Service
public class DevConfigServiceImpl extends ServiceImpl<DevConfigMapper, DevConfig> implements DevConfigService {

    private static final String SNOWY_SYS_DEFAULT_PASSWORD_KEY = "SNOWY_SYS_DEFAULT_PASSWORD";

    @Override
    public String getValueByKey(String key) {
        return null;
    }

    @Override
    public Page<DevConfig> page(DevConfigPageParam devConfigPageParam) {
        QueryWrapper<DevConfig> queryWrapper = new QueryWrapper<>();
        // 查询部分字段
        queryWrapper.lambda().select(DevConfig::getId, DevConfig::getConfigKey, DevConfig::getConfigValue,
                DevConfig::getCategory, DevConfig::getRemark, DevConfig::getSortCode);
        if (ObjectUtil.isAllNotEmpty(devConfigPageParam.getSortField(), devConfigPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(devConfigPageParam.getSortOrder());
            queryWrapper.orderBy(true, devConfigPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(devConfigPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(DevConfig::getSortCode);
        }
        queryWrapper.lambda().eq(DevConfig::getCategory, DevConfigCategoryEnum.BIZ_DEFINE.getValue());
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public List<DevConfig> list(DevConfigListParam devConfigListParam) {
        LambdaQueryWrapper<DevConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询部分字段
        lambdaQueryWrapper.select(DevConfig::getId, DevConfig::getConfigKey, DevConfig::getConfigValue,
                DevConfig::getCategory, DevConfig::getSortCode);
        if (ObjectUtil.isNotEmpty(devConfigListParam.getCategory())) {
            lambdaQueryWrapper.eq(DevConfig::getCategory, devConfigListParam.getCategory());
        }
        return this.list(lambdaQueryWrapper).stream().peek(devConfig -> {
            if (devConfig.getConfigKey().equals(SNOWY_SYS_DEFAULT_PASSWORD_KEY)) {
                devConfig.setConfigValue(DesensitizedUtil.password(devConfig.getConfigValue()));
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void add(DevConfigAddParam devConfigAddParam) {
        checkParam(devConfigAddParam);
        DevConfig devConfig = BeanUtil.toBean(devConfigAddParam, DevConfig.class);
        devConfig.setCategory(DevConfigCategoryEnum.BIZ_DEFINE.getValue());
        this.save(devConfig);
    }

    private void checkParam(DevConfigAddParam devConfigAddParam) {
        boolean hasSameConfig = this.count(new LambdaQueryWrapper<DevConfig>()
                .eq(DevConfig::getConfigKey, devConfigAddParam.getConfigKey())) > 0;
        if (hasSameConfig) {
            throw new CommonException("存在重复的配置，配置键为：{}", devConfigAddParam.getConfigKey());
        }
    }

    @Override
    public void edit(DevConfigEditParam devConfigEditParam) {
        DevConfig devConfig = this.queryEntity(devConfigEditParam.getId());
        checkParam(devConfigEditParam);
        BeanUtil.copyProperties(devConfigEditParam, devConfig);
        devConfig.setCategory(DevConfigCategoryEnum.BIZ_DEFINE.getValue());
        this.updateById(devConfig);
    }

    private void checkParam(DevConfigEditParam devConfigEditParam) {
        boolean hasSameConfig = this.count(new LambdaQueryWrapper<DevConfig>()
                .eq(DevConfig::getConfigKey, devConfigEditParam.getConfigKey())
                .ne(DevConfig::getId, devConfigEditParam.getId())) > 0;
        if (hasSameConfig) {
            throw new CommonException("存在重复的配置，配置键为：{}", devConfigEditParam.getConfigKey());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<DevConfigIdParam> devConfigIdParamList) {
        List<String> devConfigIdList = CollStreamUtil.toList(devConfigIdParamList, DevConfigIdParam::getId);
        if (ObjectUtil.isNotEmpty(devConfigIdList)) {
            List<DevConfig> devConfigList = this.listByIds(devConfigIdList);
            if (ObjectUtil.isNotEmpty(devConfigList)) {
                List<String> devConfigResultList = CollectionUtil.newArrayList(devConfigList.stream()
                        .map(DevConfig::getCategory).collect(Collectors.toSet()));
                if (devConfigResultList.size() != 1 || !devConfigResultList.get(0).equals(DevConfigCategoryEnum.BIZ_DEFINE.getValue())) {
                    throw new CommonException("不可删除系统内置配置");
                }
                // 执行删除
                this.removeByIds(devConfigIdList);
            }
        }
    }

    @Override
    public DevConfig detail(DevConfigIdParam devConfigIdParam) {
        return this.queryEntity(devConfigIdParam.getId());
    }

    @Override
    public DevConfig queryEntity(String id) {
        DevConfig devConfig = this.getById(id);
        if (ObjectUtil.isEmpty(devConfig)) {
            throw new CommonException("配置不存在，id值为：{}", id);
        }
        return devConfig;
    }

    @Override
    public void editBatch(List<DevConfigBatchParam> devConfigBatchParamList) {
        devConfigBatchParamList.forEach(devConfigBatchParam -> {
            this.update(new LambdaUpdateWrapper<DevConfig>()
                    .eq(DevConfig::getConfigKey, devConfigBatchParam.getConfigKey())
                    .set(DevConfig::getConfigValue, devConfigBatchParam.getConfigValue()));
        });
    }
}

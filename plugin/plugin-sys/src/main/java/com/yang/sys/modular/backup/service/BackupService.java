package com.yang.sys.modular.backup.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.sys.modular.backup.entity.BackupRecord;
import com.yang.sys.modular.backup.param.*;
import com.yang.sys.modular.backup.result.BackupConfigResult;

import java.util.List;

/**
 * @author: yangjianzhi
 * @version1.0
 */
public interface BackupService extends IService<BackupRecord> {

    /**
     * 更新备份配置
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:08
     */
    void config(BackupConfigParam backupConfigParam);

    /**
     * 获取条件封装
     *
     */
    QueryWrapper<BackupRecord> getQueryWrapper(BackupQueryParam backupQueryParam);

    /**
     * 获取备份分页
     *
     */
    Page<BackupRecord> page(BackupPageParam backupPageParam);

    /**
     * 获取备份列表
     */
    List<BackupRecord> list(BackupListParam backupListParam);

    /**
     * 新增备份记录
     */
    void add(BackupAddParam backupAddParam);

    /**
     * 获取备份详情
     */
    BackupRecord queryEntity(String id);

    /**
     * 恢复数据库备份
     */
    void restore(String backupId);

    /**
     * 备份数据库
     */
    void backup(BackupConfigResult backupConfigResult);

    /**
     * 手动备份
     */
    void performManual();

    /**
     * 执行备份任务
     */
    void restartJob();

    /**
     * 获取备份配置
     */
    BackupConfigResult getBackupConfigResult();
}

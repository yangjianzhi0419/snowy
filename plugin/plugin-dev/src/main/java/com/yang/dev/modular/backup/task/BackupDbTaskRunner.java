package com.yang.dev.modular.backup.task;

import com.yang.common.timer.CommonTimerTaskRunner;
import com.yang.dev.modular.backup.service.BackupService;

import javax.annotation.Resource;

/**
 * 备份数据库任务
 *
 * @author: yangjianzhi
 * @version1.0
 */
public class BackupDbTaskRunner implements CommonTimerTaskRunner {

    @Resource
    private BackupService backupService;

    @Override
    public void action() {
        try {
            backupService.performManual();
        } catch (Exception ignored) {
        }
    }
}

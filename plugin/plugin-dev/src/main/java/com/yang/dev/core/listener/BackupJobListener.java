package com.yang.dev.core.listener;

import cn.hutool.cron.CronUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yang.dev.modular.backup.service.BackupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 定时备份任务
 *
 * @author: yangjianzhi
 * @version1.0
 */
@Slf4j
@Configuration
public class BackupJobListener implements ApplicationListener<ApplicationStartedEvent>, Ordered {

    @SuppressWarnings("ALL")
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        SpringUtil.getBean(BackupService.class).restartJob();
        // 设置秒级别的启用
        CronUtil.setMatchSecond(true);
        // 启动定时器执行器
        CronUtil.restart();
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}

package com.yang.dev.core.config;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 功能配置 (例子)
 *
 * @author: yangjianzhi
 * @version1.0
 */
@Component
public class DevConfigInfo {

//    public static String backup_db_name;

//    @Resource
//    private DevConfigService devConfigService;

    @PostConstruct
    public void init() {
//        List<DevConfig> configList = devConfigService.list(new LambdaQueryWrapper<DevConfig>()
//                .eq(DevConfig::getCategory, DevConstant.BACKUP_CATEGORY_KEY));
//        for (DevConfig devConfig : configList) {
//            if (DevConstant.BACKUP_DB_NAME_KEY.equals(devConfig.getConfigKey())) {
//                backup_db_name = devConfig.getConfigValue();
//            }
//        }
    }
}

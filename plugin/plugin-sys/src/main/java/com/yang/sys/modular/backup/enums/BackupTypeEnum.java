package com.yang.sys.modular.backup.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 备份类型枚举
 *
 * @author: yangjianzhi
 * @version1.0
 */
@Getter
@Setter
public enum BackupTypeEnum {

    /**
     * 手动
     */
    MANUAL("manual"),

    /**
     * 自动
     */
    AUTOMATIC("automatic");

    private final String value;

    BackupTypeEnum(String value) {
        this.value = value;
    }
}

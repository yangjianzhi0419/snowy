package com.yang.dev.modular.backup.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Getter
@Setter
public class BackupAddParam {
    /** 备份名称 */
    @ApiModelProperty(value = "备份名称", position = 2)
    private String backupName;

    /** 备份类型 */
    @ApiModelProperty(value = "备份类型", position = 2)
    private String backupType;

    /** 数据大小 */
    @ApiModelProperty(value = "数据大小", position = 2)
    private String dataSize;
}

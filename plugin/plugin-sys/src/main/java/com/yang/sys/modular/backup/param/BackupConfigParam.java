package com.yang.sys.modular.backup.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Data
public class BackupConfigParam {

    @ApiModelProperty(value = "备份路径", required = true, position = 1)
    private String path;

    @ApiModelProperty(value = "备份周期", required = true, position = 1)
    private String frequency;
}

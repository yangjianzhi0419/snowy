package com.yang.gen.modular.basic.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Data
public class GenBasicTableResult {

    /** 表名称 */
    @ApiModelProperty(value = "表名称", position = 1)
    private String tableName;

    /** 表注释 */
    @ApiModelProperty(value = "表注释", position = 2)
    private String tableRemark;
}

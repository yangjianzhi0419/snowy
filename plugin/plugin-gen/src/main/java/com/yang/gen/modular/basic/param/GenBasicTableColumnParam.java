package com.yang.gen.modular.basic.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Getter
@Setter
public class GenBasicTableColumnParam {

    /** 表名称 */
    @ApiModelProperty(value = "表名称", required = true)
    @NotBlank(message = "表名称不能为空")
    private String tableName;
}

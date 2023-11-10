package com.yang.gen.modular.config.param;

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
public class GenConfigListParam {

    /** 排序字段 */
    @ApiModelProperty(value = "排序字段，字段驼峰名称，如：userName")
    private String sortField;

    /** 排序方式 */
    @ApiModelProperty(value = "排序方式，升序：ASCEND；降序：DESCEND")
    private String sortOrder;

    /** 基础ID */
    @ApiModelProperty(value = "基础ID")
    @NotBlank(message = "basicId不能为空")
    private String basicId;
}

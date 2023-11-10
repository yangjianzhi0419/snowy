package com.yang.sys.modular.user.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@Data
public class SysUserResult {
    /** id */
    @ApiModelProperty(value = "id", position = 1)
    private String id;

    /** 头像 */
    @ApiModelProperty(value = "头像", position = 3)
    private String avatar;
}

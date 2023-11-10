package com.yang.common.exception;

import cn.hutool.core.util.StrUtil;
import com.yang.common.enums.CommonErrorCodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用异常
 *
 * @author xuyuxiang
 * @date 2020/4/8 15:54
 */
@Getter
@Setter
public class CommonException extends RuntimeException {

    private int code;

    public CommonException() {
        super(CommonErrorCodeEnum.SYSTEM_ERROR.getMessage());
        this.code = CommonErrorCodeEnum.SYSTEM_ERROR.getCode();
    }

    public CommonException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CommonException(String message) {
        super(message);
        this.code = CommonErrorCodeEnum.SYSTEM_ERROR.getCode();
    }

    public CommonException(String msg, Object... arguments) {
        super(StrUtil.format(msg, arguments));
        this.code = CommonErrorCodeEnum.SYSTEM_ERROR.getCode();
    }

    public CommonException(CommonErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.code = errorCodeEnum.getCode();
    }

    public CommonException(CommonErrorCodeEnum errorCodeEnum, String msg, Object... arguments) {
        super(StrUtil.format(msg, arguments));
        this.code = errorCodeEnum.getCode();
    }

    public int getCode() {
        return code;
    }
}

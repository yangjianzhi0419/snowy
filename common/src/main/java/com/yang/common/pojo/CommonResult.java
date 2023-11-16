package com.yang.common.pojo;

import com.yang.common.enums.CommonErrorCodeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.Response;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对Ajax请求返回Json格式数据的简易封装
 *
 * @author: yangjianzhi
 * @version1.0
 */
@Data
public class CommonResult<T> {
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 500;

    @ApiModelProperty(value = "状态码")
    private int code;

    @ApiModelProperty(value = "提示语")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;

    public CommonResult(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public CommonResult(int code, T data) {
        this(code, data, "");
    }

    public CommonResult(CommonErrorCodeEnum errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

    // ============================  构建  ==================================

    public static <T> CommonResult<T> data(T data) {
        return new CommonResult<>(CODE_SUCCESS, data, "ok");
    }

    public static <T> CommonResult<T> ok() {
        return new CommonResult<>(CODE_SUCCESS, null, "操作成功");
    }

    public static <T> CommonResult<T> error() {
        return new CommonResult<>(CODE_ERROR, null, "服务器异常");
    }

    public static <T> CommonResult<T> error(int code, String message) {
        return new CommonResult<>(code, null, message);
    }

    public static <T> CommonResult<T> error(String message) {
        return new CommonResult<>(CODE_ERROR, null, message);
    }

    public static <T> CommonResult<T> error(CommonErrorCodeEnum errorCode) {
        return new CommonResult<>(errorCode);
    }

    public static <T> CommonResult<T> error(CommonErrorCodeEnum errorCode, String message) {
        return new CommonResult<>(errorCode.getCode(), null, message);
    }

    // 构建指定状态码
    public static <T> CommonResult<T> get(int code, String message, T data) {
        return new CommonResult<T>(code, data, message);
    }

    /**
     * 响应状态码集合
     *
     * @author xuyuxiang
     * @date 2022/7/25 13:36
     **/
    public static List<Response> responseList() {
        return Arrays.stream(CommonErrorCodeEnum.values()).map(commonExceptionEnum -> new ResponseBuilder()
                .code(String.valueOf(commonExceptionEnum.getCode())).description(commonExceptionEnum.getMessage()).build())
                .collect(Collectors.toList());
    }
}

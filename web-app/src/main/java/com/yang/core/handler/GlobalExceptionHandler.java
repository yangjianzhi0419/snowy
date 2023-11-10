package com.yang.core.handler;

import com.yang.common.pojo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: yangjianzhi
 * @version1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 不同异常返回不同结果
     */
    @ExceptionHandler
    public CommonResult<?> commonExceptionHandler(Exception e) {
        return GlobalExceptionUtil.getCommonResult(e);
    }
}

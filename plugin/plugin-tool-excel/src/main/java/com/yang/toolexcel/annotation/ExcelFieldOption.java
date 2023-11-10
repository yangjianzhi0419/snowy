package com.yang.toolexcel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel字段配置注解
 *
 * @author: yangjianzhi
 * @version1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelFieldOption {
    String value() default "";

    boolean require() default false;
}

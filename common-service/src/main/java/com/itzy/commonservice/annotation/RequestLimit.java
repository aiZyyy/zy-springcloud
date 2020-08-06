package com.itzy.commonservice.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA
 * 请求限制
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface RequestLimit {
    /**
     * @return 是否启用
     */
    boolean enable() default true;

    /**
     * @return 拦截时间
     */
    int value() default 3;
}

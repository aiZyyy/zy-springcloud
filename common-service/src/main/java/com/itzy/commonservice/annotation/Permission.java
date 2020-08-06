package com.itzy.commonservice.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA
 * 权限检测注解
 */
@Documented
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface Permission {
    /**
     * @return 权限名称
     */
    String value() default "";

    /**
     * @return 是否启用
     */
    boolean enable() default true;
}

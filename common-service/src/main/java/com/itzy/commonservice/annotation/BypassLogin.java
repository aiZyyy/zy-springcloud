package com.itzy.commonservice.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA
 * 允许绕过登录拦截 支持类和方法
 *
 */
@Documented
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface BypassLogin {
}

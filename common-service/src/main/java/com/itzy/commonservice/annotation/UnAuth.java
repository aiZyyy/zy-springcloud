package com.itzy.commonservice.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: ZY
 * @Date: 2019/8/2 15:31
 * @Version 1.0
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface UnAuth {
}

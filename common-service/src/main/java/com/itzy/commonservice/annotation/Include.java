package com.itzy.commonservice.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Include {
    /**
     * @return 包含的路径
     */
    String[] value();
}

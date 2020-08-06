package com.itzy.commonservice.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Exclude {
    /**
     * @return 排除的路径
     */
    String[] value();
}

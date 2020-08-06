package com.itzy.commonservice.annotation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

/**
 * @Author: ZY
 * @Date: 2019/8/1 18:23
 * @Version 1.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@EnableAsync
@MapperScan("com.itzy.**.mapper")
@ComponentScan("com.itzy")
@EnableFeignClients("com.itzy")
@SpringCloudApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableTransactionManagement
public @interface ZyMicroServiceApplication {
}

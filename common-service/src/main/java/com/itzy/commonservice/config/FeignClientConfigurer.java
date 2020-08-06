package com.itzy.commonservice.config;

import com.itzy.commonservice.interceptor.FeignInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA
 */
@Configuration
@ConditionalOnClass(name = "feign.RequestInterceptor")
public class FeignClientConfigurer {
    @Bean
    @ConditionalOnProperty(value = "itzy-micro-service-common.feign.proxy.enabled", havingValue = "true")
    public RequestInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }

    @Bean
    @ConditionalOnProperty(value = "itzy-micro-service-common.feign.debug", havingValue = "true")
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}

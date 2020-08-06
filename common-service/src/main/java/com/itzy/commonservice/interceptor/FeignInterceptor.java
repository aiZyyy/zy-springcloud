package com.itzy.commonservice.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA
 */
public class FeignInterceptor implements RequestInterceptor {
    @Value("${itzy-micro-service.feign.proxy.header:Cookie,Authorization}")
    private String[] proxyHeader;

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Optional.ofNullable(attributes)
                .ifPresent(attr -> Arrays
                        .stream(proxyHeader)
                        .forEach(name -> Optional.ofNullable(attr.getRequest().getHeader(name))
                                .ifPresent(value -> template.header(name, value))));
    }
}

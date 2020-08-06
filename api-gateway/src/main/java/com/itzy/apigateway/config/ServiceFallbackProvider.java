package com.itzy.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Author: ZY
 * @Date: 2019/7/31 17:55
 * @Version 1.0
 */
@Component
public class ServiceFallbackProvider implements FallbackProvider {
    @Value("${fallback.message:{\"status\":503, \"msg\":\"服务暂时不可用 请稍候重试!\"}}")
    private String message;

    @Override
    public String getRoute() {
        return null;
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new DefaultFallbackResponse();
    }

    class DefaultFallbackResponse implements ClientHttpResponse {
        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }

        @Override
        public int getRawStatusCode() {
            return HttpStatus.SERVICE_UNAVAILABLE.value();
        }

        @Override
        public String getStatusText() {
            return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
        }

        @Override
        public void close() {

        }

        @Override
        public InputStream getBody() {
            return new ByteArrayInputStream(message.getBytes());
        }

        @Override
        public HttpHeaders getHeaders() {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            return headers;
        }
    }
}

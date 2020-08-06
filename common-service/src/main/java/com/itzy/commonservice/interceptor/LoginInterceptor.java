package com.itzy.commonservice.interceptor;


import com.itzy.commonservice.annotation.BypassLogin;
import com.itzy.commonservice.interfaces.LoginHandler;
import com.itzy.commonservice.utils.KeyValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 */
@Configuration
@ConditionalOnBean(LoginHandler.class)
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Value("${itzy-micro-service-common.message.no-login:User Not Login!}")
    private String message;
    private LoginHandler loginHandler;

    public LoginInterceptor(LoginHandler loginHandler) {this.loginHandler = loginHandler;}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // Only Intercept HandlerMethod
        if (!(o instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod method = (HandlerMethod) o;
        BypassLogin bypassLogin = Optional.ofNullable(method.getMethodAnnotation(BypassLogin.class))
                .orElseGet(() -> method.getBeanType().getAnnotation(BypassLogin.class));
        if (Objects.nonNull(bypassLogin) || loginHandler.isLogin()) {
            return true;
        }
        KeyValue.rd(HttpStatus.UNAUTHORIZED.value(), message).write(response);
        return false;
    }
}

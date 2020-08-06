package com.itzy.commonservice.interceptor;

import com.alibaba.fastjson.JSON;
import com.itzy.commonservice.exception.handler.GlobalExceptionHandler;
import com.itzy.commonservice.utils.KeyValue;
import com.itzy.commonservice.utils.WebUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA
 */
@Slf4j
@Aspect
@Component
public class DebugInterceptor extends HandlerInterceptorAdapter {
    private ThreadLocal<Long> startTime = ThreadLocal.withInitial(System::nanoTime);
    private ThreadLocal<Signature> signature = ThreadLocal.withInitial(() -> null);
    private ThreadLocal<Object[]> paramsCache = ThreadLocal.withInitial(() -> null);
    private ThreadLocal<Object> resultCache = ThreadLocal.withInitial(() -> null);

    @Pointcut("execution(public * com.itzy.*.controller.*.*(..)) ||" +
              "execution(public * com.itzy.*.*.controller.*.*(..)) ||" +
              "execution(public com.itzy.commonservice.utils.KeyValue com.itzy.commonservice.exception.handler.GlobalExceptionHandler.*(..)) ")
    public void resultAop() {}

    @Order(2)
    @Around("resultAop()")
    @SneakyThrows
    public Object resultAround(ProceedingJoinPoint pjp) {
        // 异常拦截不保存参数
        if (!(pjp.getThis() instanceof GlobalExceptionHandler)) {
            signature.set(pjp.getSignature());
            paramsCache.set(pjp.getArgs());
        }
        val result = pjp.proceed();
        resultCache.set(result);
        return result;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        startTime.set(System.nanoTime());
        paramsCache.set(new Object[]{request.getParameterMap()});
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        StringBuilder str = new StringBuilder();
        str.append("\n===================== DebugInterceptor =====================");
        str.append("\nRemote   IP        : ").append(WebUtil.getClientIP());
        str.append("\nRequest  Method    : ").append(request.getMethod());
        str.append("\nRequest  URL       : ")
                .append(request.getRequestURI())
                .append(StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString());
        str.append("\nAction   Signature : ").append(Optional.ofNullable(signature.get()).map(Object::toString).orElse("Empty"));
        str.append("\nRequest  Params    : ");
        Object[] param = paramsCache.get();
        // 当只有一个参数 并且为MAP的时候直接序列化
        if (param != null && param.length == 1 && param[0] instanceof Map) {
            str.append(JSON.toJSONString(param[0]));
        } else {
            // 多个参数 循环判定 是否为朴素对象
            Object[] params = Optional.ofNullable(paramsCache.get()).orElse(new Object[]{});
            for (int i = 0; i < params.length; i++) {
                Object obj = params[i];
                // 需要保证类直接继承Object 并且没有实现任何接口 才能用JSON序列化
                boolean isPlanObject = Optional
                        .ofNullable(obj)
                        .map(o -> o.getClass().getSuperclass().equals(Object.class)
                                  && !o.getClass().isArray()
                                  && o.getClass().getInterfaces().length == 0).orElse(true);
                str.append(i == 0 ? "[" : "\n                     [")
                        .append(i)
                        .append("] => ")
                        .append(isPlanObject ? JSON.toJSONString(obj) : String.valueOf(obj));
            }
        }
        str.append("\nResponse Status    : ").append(response.getStatus());
        str.append("\nResponse Body      : ")
                .append(Optional.ofNullable(resultCache.get()).map(JSON::toJSONString).orElse(KeyValue.cacheResult.get()));
        str.append("\nHandle   Time      : ")
                .append(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime.get())).append("ms");
        str.append("\n============================================================");
        log.info(str.toString());
        KeyValue.cacheResult.remove();
        signature.remove();
        paramsCache.remove();
        resultCache.remove();
    }
}

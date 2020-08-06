package com.itzy.commonservice.exception.handler;

import com.alibaba.fastjson.JSON;
import com.itzy.commonservice.exception.EntityValidException;
import com.itzy.commonservice.exception.ForbiddenException;
import com.itzy.commonservice.utils.KeyValue;
import com.itzy.commonservice.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ForbiddenException.class})
    @ResponseBody
    public KeyValue handleForbiddenException(Exception ex) {
        // 禁止访问不用打印异常
        log.info("原因: {}", ex.getMessage());
        return KeyValue.forbidden(ex.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public KeyValue handleMultipartException(MultipartException ex) {
        doLogger(ex);
        return KeyValue.forbidden("文件大小超出系统限制!");
    }

    @ExceptionHandler(EntityValidException.class)
    @ResponseBody
    public KeyValue handleValidException(EntityValidException ex) {
        // 实体认证异常不需要打印
        return ex.getKeyValue();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(
                ex,
                checkInGlobal(ex.getBindingResult()), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(
                ex,
                checkInGlobal(ex.getBindingResult()), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(
                ex,
                KeyValue.bad_request("不支持的请求数据类型!", ex.getMessage()),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(
                ex,
                KeyValue.bad_request("无法读取请求数据 请检查入参数据是否正确!", ex.getMessage()),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(
                ex,
                KeyValue.bad_request(
                        String.format("参数类型不匹配 值 %s 无法转换为 %s",
                                      ex.getValue(),
                                      ex.getRequiredType().getSimpleName()), ex.getMessage()),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpServletRequest req = ((ServletWebRequest) request).getNativeRequest(HttpServletRequest.class);
        return handleExceptionInternal(
                ex,
                KeyValue.rd(HttpStatus.METHOD_NOT_ALLOWED.value(), "请求METHOD不支持")
                        .add("path", req.getRequestURI())
                        .add("method", req.getMethod())
                        .add("time", System.currentTimeMillis()),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpServletRequest req = ((ServletWebRequest) request).getNativeRequest(HttpServletRequest.class);
        return handleExceptionInternal(
                ex,
                KeyValue.rd(HttpStatus.NOT_FOUND.value(), "路径未找到!")
                        .add("path", req.getRequestURI())
                        .add("method", req.getMethod())
                        .add("time", System.currentTimeMillis()),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body instanceof KeyValue) {((KeyValue) body).cache();}
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public KeyValue handleControllerException(Throwable ex) {
        doLogger(ex);
        return KeyValue.rd(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getClass().getName() + ": " + ex.getMessage());
    }

    private void doLogger(Throwable ex) {
        HttpServletRequest request = WebUtil.getRequest();
        String realIp = WebUtil.getClientIP();
        if (log.isErrorEnabled()) {
            log.info(String.format("\n%-15s %-6s %s %s",
                                   StringUtils.isEmpty(realIp) ? request.getRemoteAddr() : realIp,
                                   request.getMethod(),
                                   request.getRequestURI(),
                                   JSON.toJSONString(request.getParameterMap())));
            log.error("", ex);
        }
    }

    private KeyValue checkInGlobal(BindingResult br) {
        try {
            check(br);
        } catch (EntityValidException ex) {
            return ex.getKeyValue();
        }
        return null;
    }

    private void check(BindingResult br) {
        if (br.hasErrors()) {
            ObjectError err = br.getAllErrors().get(0);
            String data;
            if (err instanceof FieldError) {
                FieldError fErr = (FieldError) err;
                data = String.format("字段 %s 的值 '%s' %s", fErr.getField(), fErr.getRejectedValue(), getErrType(err));
            } else {
                data = String.format("参数: %s 错误 原因: %s", err.getObjectName(), getErrType(err));
            }
            throw new EntityValidException(KeyValue.bad_request(err.getDefaultMessage(), data));
        }
    }

    private String getErrType(ObjectError err) {
        switch (err.getCode()) {
            case "typeMismatch":
                return "类型不匹配!";
            default:
                return err.getDefaultMessage();
        }
    }
}

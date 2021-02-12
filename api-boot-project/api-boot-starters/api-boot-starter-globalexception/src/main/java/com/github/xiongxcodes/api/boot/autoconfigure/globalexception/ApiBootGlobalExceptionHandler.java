package com.github.xiongxcodes.api.boot.autoconfigure.globalexception;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.minbox.framework.api.boot.common.exception.ApiBootException;
import org.minbox.framework.api.boot.common.model.ApiBootResult;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class ApiBootGlobalExceptionHandler implements ApplicationContextAware{
   private List<ApiBootExceptionHandler> apiBootExceptionHandlers = new ArrayList<ApiBootExceptionHandler>();
   /**
     * *方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiBootResult<StackTraceElement> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
        HttpServletRequest request) {
        log.error(String.format("【%s】请求异常 code:-1 reason:%s IP:%s", request.getRequestURL().toString(), e.getMessage(),
            request.getRemoteHost()));
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        ObjectError error = errors.get(0);
        return ApiBootResult.fail("-1",error.getDefaultMessage(),e);
    }
    
    @ExceptionHandler(BindException.class)
    public ApiBootResult<StackTraceElement> handleBindException(BindException e,
        HttpServletRequest request) {
        log.error(String.format("【%s】请求异常 code:-1 reason:%s IP:%s", request.getRequestURL().toString(), e.getMessage(),
            request.getRemoteHost()));
        List<ObjectError> errors = e.getBindingResult().getAllErrors();//FieldError
        ObjectError error = errors.get(0);
        return ApiBootResult.fail("-1",error.getDefaultMessage(),e);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiBootResult<StackTraceElement> handleBindException(ConstraintViolationException e,
        HttpServletRequest request) {
        log.error(String.format("【%s】请求异常 code:-1 reason:%s IP:%s", request.getRequestURL().toString(), e.getMessage(),
            request.getRemoteHost()));
        return ApiBootResult.fail("-1", e.getConstraintViolations().stream()
            .map( cv -> cv == null ? "null" : cv.getPropertyPath() + ": " + cv.getMessage() )
            .collect( Collectors.joining( ", " ) ),e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiBootResult<StackTraceElement> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
        HttpServletRequest request) {
        log.error(String.format("【%s】请求异常 code:%s reason:%s", request.getRequestURL().toString(),
            HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()));
        // return RestRes.failed(-1, "http 400：" + e.getMessage());
        return ApiBootResult.fail(HttpStatus.BAD_REQUEST.value() + "", e);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiBootResult<StackTraceElement>
    handleHttpRequestMethodNotSupportedException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error(String.format("【%s】请求异常 code:%s reason:%s", request.getRequestURL().toString(),
            HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase()));
        return ApiBootResult.fail(HttpStatus.METHOD_NOT_ALLOWED.value() + "", e);
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiBootResult<StackTraceElement>
    handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        log.error(String.format("【%s】请求异常 code:%s reason:%s", request.getRequestURL().toString(),
            HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase()));
        return ApiBootResult.fail(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value() + "", e);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class})
    public ApiBootResult<StackTraceElement> handleAuthenticationException(AuthenticationException e,
        HttpServletRequest request) {
        log.error(String.format("【%s】请求异常 code:%s reason:%s", request.getRequestURL().toString(),
            HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase()));
        return ApiBootResult.fail(HttpStatus.UNAUTHORIZED.value() + "", e);
    }

    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiBootResult<StackTraceElement> handleException(Exception e, HttpServletRequest request) {
        log.error(String.format("【%s】请求异常 code:%s reason:%s", request.getRequestURL().toString(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()), e);
        if (e instanceof ApiBootException) {
            return ApiBootResult.fail(e);
        }
        ApiBootResult<StackTraceElement> apiBootResult = null;
        for(ApiBootExceptionHandler apiBootExceptionHandler:apiBootExceptionHandlers) {
            apiBootResult = apiBootExceptionHandler.handleException(HttpStatus.INTERNAL_SERVER_ERROR,e, request);
            if(null != apiBootResult) {
                return apiBootResult;
            }
        }
        //if(null != apiBootResult) {
        //    return apiBootResult;
        //}
        return ApiBootResult.fail(HttpStatus.INTERNAL_SERVER_ERROR.value() + "", e);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ApiBootExceptionHandler> apiBootExceptionHandlerMap = applicationContext.getBeansOfType(ApiBootExceptionHandler.class);
        apiBootExceptionHandlers.addAll(apiBootExceptionHandlerMap.values());
    }
}

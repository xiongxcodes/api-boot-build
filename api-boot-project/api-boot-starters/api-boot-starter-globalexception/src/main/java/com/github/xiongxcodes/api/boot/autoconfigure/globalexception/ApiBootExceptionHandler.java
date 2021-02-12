package com.github.xiongxcodes.api.boot.autoconfigure.globalexception;

import javax.servlet.http.HttpServletRequest;

import org.minbox.framework.api.boot.common.model.ApiBootResult;
import org.springframework.http.HttpStatus;

public interface ApiBootExceptionHandler {
     public ApiBootResult<StackTraceElement> handleException(HttpStatus httpStatus ,Exception e, HttpServletRequest request) ;
}

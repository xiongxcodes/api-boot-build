package org.minbox.framework.api.boot.common.model;

import java.io.Serializable;

import org.minbox.framework.api.boot.common.exception.ApiBootException;

import lombok.Builder;
import lombok.Data;

/**
 * ApiBoot提供的接口返回对象
 *
 * @author：恒宇少年 - 于起宇
 * <p>
 * DateTime：2019-03-18 10:57
 * Blog：http://blog.yuqiyu.com
 * WebSite：http://www.jianshu.com/u/092df3f77bca
 * Gitee：https://gitee.com/hengboy
 * GitHub：https://github.com/hengboy
 */
@Data
@Builder
public class ApiBootResult<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2747033373367830183L;
    /**
     * 返回数据内容
     */
    private T data;
    /**
     * 遇到业务异常或者系统异常时的错误码
     */
    private String errorCode;
    /**
     * 遇到业务异常或者系统异常时的错误消息提示内容
     */
    private String errorMessage;
    
    private boolean success;
    public ApiBootResult() {
        
    }
    public ApiBootResult(T data,String errorCode,String errorMessage,boolean success) {
        this.data = data;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.success = success;
    }
    public static ApiBootResult<Void> ok() {
        return new ApiBootResult<Void>(null, "0", "ok",true);
    }
    public static ApiBootResult<Void> ok(String errorMessage) {
        return new ApiBootResult<Void>(null, "0", errorMessage,true);
    }
    public static <T> ApiBootResult<T> ok(T data) {
        return new ApiBootResult<T>(data, "0", "ok",true);
    }
    public static <T> ApiBootResult<T> ok(T data,String errorMessage) {
        return new ApiBootResult<T>(data, "0", errorMessage,true);
    }
    public static ApiBootResult<Void> fail(String errorMessage) {
        return new ApiBootResult<Void>(null, "-1", errorMessage,false);
    }
    public static ApiBootResult<Void> fail(String errorCode,String errorMessage) {
        return new ApiBootResult<Void>(null, errorCode, errorMessage,false);
    }
    public static <T> ApiBootResult<T> fail(T data,String errorCode,String errorMessage) {
        return new ApiBootResult<T>(data, errorMessage, errorMessage,false);
    }
    public static ApiBootResult<StackTraceElement> fail(String errorMessage,ApiBootException cause) {
        return new ApiBootResult<StackTraceElement>(getStackTraceElement(cause), cause.getErrorCode(), errorMessage,false);
    }
    public static ApiBootResult<StackTraceElement> fail(ApiBootException cause) {
        return fail(cause.getMessage(),cause);
    }
    public static ApiBootResult<StackTraceElement> fail(String errorCode,String errorMessage,Throwable cause) {
        if(cause instanceof ApiBootException) {
            return  fail(errorMessage,(ApiBootException)cause);
        }
        return new ApiBootResult<StackTraceElement>(getStackTraceElement(cause), errorCode, errorMessage,false);
    }
    public static ApiBootResult<StackTraceElement> fail(String errorCode,Throwable cause) {
        if(cause instanceof ApiBootException) {
            return  fail((ApiBootException)cause);
        }
        return fail(errorCode,cause.getMessage(),cause);
    }
    public static ApiBootResult<StackTraceElement> fail(Throwable cause) {
        if(cause instanceof ApiBootException) {
            return  fail((ApiBootException)cause);
        }
        return fail("-1",cause.getMessage(),cause);
    }
    public static StackTraceElement getStackTraceElement(Throwable cause) {
        if(null != cause.getCause()) {
            cause = cause.getCause();
        }
        return null==cause.getStackTrace()||0==cause.getStackTrace().length?null:cause.getStackTrace()[0];
    }
}

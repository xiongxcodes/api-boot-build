package com.github.xiongxcodes.api.boot.autoconfigure.globalexception;

import javax.servlet.http.HttpServletRequest;

import org.minbox.framework.api.boot.common.model.ApiBootResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;

@ConditionalOnWebApplication
@ConditionalOnClass({FlowException.class, DegradeException.class, ParamFlowException.class, SystemBlockException.class,
    AuthorityException.class})
public class ApiBootSentinelGlobalExceptionConfiguration {
    @Bean
    public ApiBootSentinelExceptionHandler apiBootSentinelExceptionHandler() {
        return new ApiBootSentinelExceptionHandler();
    }
    public static class ApiBootSentinelExceptionHandler implements ApiBootExceptionHandler {
        @Override
        public ApiBootResult<StackTraceElement> handleException(HttpStatus httpStatus, Exception e,
            HttpServletRequest request) {
            Throwable cause = e.getCause();
            ApiBootResult<StackTraceElement> apiBootResult = null;
            String errorCode = httpStatus.value()+"";
            if (e instanceof FlowException || (null != cause && cause instanceof FlowException)) {
                // sentinelErrorMsg.setMsg("接口限流了");
                // sentinelErrorMsg.setStatus(101);
                apiBootResult = ApiBootResult.fail(errorCode, "接口限流了",e);
            } else if (e instanceof DegradeException || (null != cause && cause instanceof FlowException)) {
                // sentinelErrorMsg.setMsg("服务降级了");
                // sentinelErrorMsg.setStatus(102);
                apiBootResult = ApiBootResult.fail(errorCode, "服务降级了",e);
            } else if (e instanceof ParamFlowException || (null != cause && cause instanceof FlowException)) {
                // sentinelErrorMsg.setMsg("热点参数限流了");
                // sentinelErrorMsg.setStatus(103);
                apiBootResult = ApiBootResult.fail(errorCode, "热点参数限流了",e);
            } else if (e instanceof SystemBlockException || (null != cause && cause instanceof FlowException)) {
                // sentinelErrorMsg.setMsg("系统规则（负载/...不满足要求）");
                // sentinelErrorMsg.setStatus(104);
                apiBootResult = ApiBootResult.fail(errorCode, "系统规则（负载/...不满足要求）",e);
            } else if (e instanceof AuthorityException || (null != cause && cause instanceof FlowException)) {
                // sentinelErrorMsg.setMsg("授权规则不通过");
                // sentinelErrorMsg.setStatus(105);
                apiBootResult = ApiBootResult.fail(errorCode, "授权规则不通过",e);
            }
            return apiBootResult;
        }
    }
}

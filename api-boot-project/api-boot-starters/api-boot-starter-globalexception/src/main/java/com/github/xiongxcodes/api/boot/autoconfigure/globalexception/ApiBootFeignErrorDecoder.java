 package com.github.xiongxcodes.api.boot.autoconfigure.globalexception;

import java.io.IOException;

import org.minbox.framework.api.boot.common.exception.ApiBootException;
import org.minbox.framework.api.boot.common.model.ApiBootResult;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
/**
 * feign 调用异常传递
 * @author wmy
 * @date 2021/02/08
 */
@Slf4j
public class ApiBootFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.info("methodKey:"+methodKey);
        log.info("response.status():"+response.status());
        log.info("response.reason():"+response.reason());
        String message = "";
        try {
            message = Util.toString(response.body().asReader());
        } catch (IOException e1) {
             e1.printStackTrace();
             //return new ApiBootException("-1", "unknown error");
        }
        log.info("message:"+message);
        if(StringUtils.isEmpty(message)) {
            return new ApiBootException("-1", "unknown error");
        }
        ApiBootResult<Object> jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(message, ApiBootResult.class);
            return new ApiBootException(jsonObject.getErrorCode(), jsonObject.getErrorMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiBootException("-1", message);
        }
    }
}

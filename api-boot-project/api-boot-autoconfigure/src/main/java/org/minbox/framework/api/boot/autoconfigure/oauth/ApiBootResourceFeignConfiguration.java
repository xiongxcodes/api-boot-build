package org.minbox.framework.api.boot.autoconfigure.oauth;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpHeaders;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@ConditionalOnWebApplication
@ConditionalOnClass(FeignClientsConfiguration.class)
public class ApiBootResourceFeignConfiguration {
    
    @Bean
    public ApiBootFeignRequestInterceptor apiBootFeignRequestInterceptor() {
        return new ApiBootFeignRequestInterceptor();
    }

    public static class ApiBootFeignRequestInterceptor implements RequestInterceptor {
        @Override
        public void apply(RequestTemplate template) {
            try {
                HttpServletRequest request =
                    ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
                String accessToken = request == null ? "" : request.getHeader(HttpHeaders.AUTHORIZATION);
                template.header(HttpHeaders.AUTHORIZATION, accessToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

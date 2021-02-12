package com.github.xiongxcodes.api.boot.autoconfigure.globalexception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
//@ConditionalOnBean({FeignClientFactoryBean.class})
@ConditionalOnWebApplication
@ConditionalOnClass(FeignClientsConfiguration.class)
//@EnableConfigurationProperties({FeignClientProperties.class})
public class ApiBootFeignGlobalExceptionConfiguration {
    @Bean
    public ApiBootFeignErrorDecoder ApiBootFeignErrorDecoder() {
        return new ApiBootFeignErrorDecoder();
    }
    @SuppressWarnings("rawtypes")
    @Bean
    public ApiBootFeignFallbackFactory apiBootFallbackFactory() {
        return new ApiBootFeignFallbackFactory();
    }
}

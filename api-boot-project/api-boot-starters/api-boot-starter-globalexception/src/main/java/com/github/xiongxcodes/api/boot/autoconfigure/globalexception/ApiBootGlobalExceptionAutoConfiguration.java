package com.github.xiongxcodes.api.boot.autoconfigure.globalexception;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(value="com.github.xiongxcodes.api.boot.autoconfigure.globalexception")
@Import({ApiBootFeignGlobalExceptionConfiguration.class,ApiBootSentinelGlobalExceptionConfiguration.class})
public class ApiBootGlobalExceptionAutoConfiguration {

}

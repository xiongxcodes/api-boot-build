package com.github.xiongxcodes.api.boot.autoconfigure.distributedlock;

import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.redisson.spring.starter.RedissonProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;

import com.github.xiongxcodes.distributedlock.handler.DistributedlockHandler;

@Configuration
@ConditionalOnClass({RedissonClient.class, RedisOperations.class})
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@EnableConfigurationProperties({RedissonProperties.class, RedisProperties.class})
public class ApiBootDistributedlockAutoConfiguration {
    @Bean
    public DistributedlockHandler distributedlockHandler(RedissonClient redisson) {
        return new DistributedlockHandler(redisson);
    }
}

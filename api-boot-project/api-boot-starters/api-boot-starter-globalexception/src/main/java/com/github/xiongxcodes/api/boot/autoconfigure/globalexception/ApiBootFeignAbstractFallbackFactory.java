 package com.github.xiongxcodes.api.boot.autoconfigure.globalexception;

import java.lang.reflect.Method;

import feign.hystrix.FallbackFactory;

public abstract class ApiBootFeignAbstractFallbackFactory<T> implements FallbackFactory<T> {

    @Override
    public T create(Throwable cause) {
         return null;
    }
    public abstract T create(Method method,  Throwable cause);

}

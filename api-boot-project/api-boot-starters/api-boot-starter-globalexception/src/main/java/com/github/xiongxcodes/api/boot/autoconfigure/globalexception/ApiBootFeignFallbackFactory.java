package com.github.xiongxcodes.api.boot.autoconfigure.globalexception;

import java.lang.reflect.Method;

public class ApiBootFeignFallbackFactory<T> extends ApiBootFeignAbstractFallbackFactory<T> {
    @Override
    public T create(Method method, Throwable cause) {
/**
        Proxy.newProxyInstance(method.getDeclaringClass().getClassLoader(), new Class[] {method.getDeclaringClass()},
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (cause instanceof ApiBootException) {

                    }
                    return null;
                }
            });**/
        return null;
    }

}

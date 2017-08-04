package com.sunny.engine;


import com.sunny.engine.api.ACTION;
import com.sunny.engine.api.ApiRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * Dynamic Proxy Wrapper for api
 * Created by ning.dai on 16/7/28.
 */
public class ApiWrapper {
    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new
                MyProxy());
    }

    static ApiRequest parserMethodAnnotation(Annotation annotation) {
        if (annotation instanceof ACTION) {
            ACTION action = (ACTION) annotation;
            String value = action.value();
            Boolean post = action.post();
            return ApiRequest.c(value, post ? "POST" : "GET");
        }
        return null;
    }

    private static Type getParameterUpperBound(int index, ParameterizedType type) {
        Type[] types = type.getActualTypeArguments();
        if (index < 0 || index >= types.length) {
            throw new IllegalArgumentException(
                    "Index " + index + " not in range [0," + types.length + ") for " + type);
        }
        Type paramType = types[index];
        if (paramType instanceof WildcardType) {
            return ((WildcardType) paramType).getUpperBounds()[0];
        }
        return paramType;
    }

    private static Type getCallResponseType(Type returnType) {
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException(
                    "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
        }
        return getParameterUpperBound(0, (ParameterizedType) returnType);
    }

    static class MyProxy implements InvocationHandler {

        @SuppressWarnings("unchecked")
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Annotation[] annotations = method.getAnnotations();
//            Type[] genericParameterTypes = method.getGenericParameterTypes();
            //Parser action
            ApiRequest request = null;
            for (Annotation annotation : annotations) {
                request = parserMethodAnnotation(annotation);
                if (request != null) {
                    break;
                }
            }
            if (request == null) {
                throw new Exception("Fail to generate Request, Action is empty!");
            }
            //Parser parameters
            Annotation[][] parameterAnnotationsArray = method.getParameterAnnotations();
            int parameterCount = parameterAnnotationsArray.length;

            for (int i = 0; i < parameterCount; i++) {
                Annotation[] parameterAnnotations = parameterAnnotationsArray[i];
                Object arg = args[i];
                request.add(parameterAnnotations[0], arg);
            }
            //Parser Response.
            return APICC.newTask(request, getCallResponseType(method.getGenericReturnType()));
        }
    }
}

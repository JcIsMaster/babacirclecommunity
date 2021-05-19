package com.example.babacirclecommunity.common.anntation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略全局返回响应包装
 *
 * @Author MQ
 * @Date 2019/8/31
 * @Version 1.0
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {
}

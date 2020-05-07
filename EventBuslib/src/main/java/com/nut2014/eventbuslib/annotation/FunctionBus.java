package com.nut2014.eventbuslib.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//注解在什么之上
//jvm运行时通过反射获取
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionBus {

}

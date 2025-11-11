package com.form.system.formatter.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段格式化器注解
 * 用于标记字段格式化器组件
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface FormFieldFormatter {
    
    /**
     * 字段类型
     * @return 支持的字段类型数组
     */
    String[] value() default {};
    
    /**
     * 优先级，数值越小优先级越高
     * @return 优先级
     */
    int order() default 0;
}
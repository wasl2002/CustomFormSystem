package com.form.system.formatter;

import com.form.system.entity.FormField;

/**
 * 字段格式化器抽象基类
 * 定义字段格式化的通用接口
 */
public abstract class FieldFormatter {
    
    /**
     * 格式化字段值
     * @param value 原始字段值
     * @param field 字段配置
     * @return 格式化后的值
     */
    public abstract Object format(Object value, FormField field);
    
    /**
     * 判断是否支持该字段类型
     * @param fieldType 字段类型
     * @return 是否支持
     */
    public abstract boolean supports(String fieldType);
}
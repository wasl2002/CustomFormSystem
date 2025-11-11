package com.form.system.formatter;

import com.form.system.entity.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 字段格式化器管理器
 * 提供全局访问点来格式化字段值
 */
@Component
public class FieldFormatterManager {
    
    private final FieldFormatterRegistry registry;
    
    @Autowired
    public FieldFormatterManager(FieldFormatterRegistry registry) {
        this.registry = registry;
    }
    
    /**
     * 格式化字段值
     * @param value 原始字段值
     * @param field 字段配置
     * @return 格式化后的值
     */
    public Object formatFieldValue(Object value, FormField field) {
        return registry.formatFieldValue(value, field);
    }
    
    /**
     * 注册新的字段格式化器
     * @param formatter 字段格式化器
     */
    public void registerFormatter(FieldFormatter formatter) {
        registry.register(formatter);
    }
}
package com.form.system.formatter.example;

import com.form.system.entity.FormField;
import com.form.system.formatter.FieldFormatterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 字段格式化器使用示例
 * 展示如何在实际应用中使用字段格式化器
 */
@Service
public class FieldFormatterUsageExample {
    
    @Autowired
    private FieldFormatterManager fieldFormatterManager;
    
    /**
     * 格式化表单数据示例
     * @param value 字段值
     * @param field 字段配置
     * @return 格式化后的值
     */
    public Object formatFormData(Object value, FormField field) {
        // 使用字段格式化器管理器格式化字段值
        return fieldFormatterManager.formatFieldValue(value, field);
    }
    
    /**
     * 动态注册新的字段格式化器示例
     */
    public void registerCustomFormatter() {
        // 创建自定义字段格式化器
        CustomPhoneFieldFormatter phoneFormatter = new CustomPhoneFieldFormatter();
        
        // 注册到管理器
        fieldFormatterManager.registerFormatter(phoneFormatter);
        
        // 现在系统就可以处理电话号码类型的字段了
    }
}
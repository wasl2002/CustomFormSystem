package com.form.system.formatter.example;

import com.form.system.entity.FormField;
import com.form.system.formatter.FieldFormatter;
import com.form.system.formatter.annotation.FormFieldFormatter;
import org.springframework.stereotype.Component;

/**
 * 自定义电话号码字段格式化器示例
 * 展示如何创建和注册自定义字段格式化器
 */
@Component
@FormFieldFormatter(value = {"phone", "tel"}, order = -1) // 高优先级
public class CustomPhoneFieldFormatter extends FieldFormatter {
    
    @Override
    public Object format(Object value, FormField field) {
        if (value == null) return "";
        
        String phone = value.toString().trim();
        // 简单的电话号码格式化，例如将 12345678901 格式化为 123-4567-8901
        if (phone.length() == 11 && phone.matches("\\d+")) {
            return phone.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        }
        return phone;
    }
    
    @Override
    public boolean supports(String fieldType) {
        return "phone".equals(fieldType) || "tel".equals(fieldType);
    }
}
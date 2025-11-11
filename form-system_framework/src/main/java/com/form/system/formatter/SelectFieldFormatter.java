package com.form.system.formatter;

import com.form.system.entity.FormField;
import com.form.system.formatter.annotation.FormFieldFormatter;

import java.util.Map;

/**
 * 下拉框/单选框字段格式化器
 */
@FormFieldFormatter({"select", "select-field", "radio", "radio-field"})
public class SelectFieldFormatter extends FieldFormatter {
    
    @Override
    public Object format(Object value, FormField field) {
        try {
            if (value == null || field.getFieldOptions() == null) return value;
            
            // 解析选项
            Map<String, String> optionMap = FieldFormatterUtils.parseFieldOptions(field.getFieldOptions());
            String valueStr = value.toString();
            
            // 查找对应的标签
            String label = optionMap.get(valueStr);
            return label != null ? label : valueStr;
        } catch (Exception e) {
            return value;
        }
    }
    
    @Override
    public boolean supports(String fieldType) {
        return "select".equals(fieldType) || "select-field".equals(fieldType) || 
               "radio".equals(fieldType) || "radio-field".equals(fieldType);
    }
}
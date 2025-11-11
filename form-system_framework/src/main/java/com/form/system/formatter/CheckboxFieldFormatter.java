package com.form.system.formatter;

import com.form.system.entity.FormField;
import com.form.system.formatter.annotation.FormFieldFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 复选框字段格式化器
 */
@FormFieldFormatter({"checkbox", "checkbox-field"})
public class CheckboxFieldFormatter extends FieldFormatter {
    
    @Override
public Object format(Object value, FormField field) {
        try {
            if (value == null || field.getFieldOptions() == null) return value;
            
            // 解析选项
            Map<String, String> optionMap = FieldFormatterUtils.parseFieldOptions(field.getFieldOptions());
            
            // 处理不同类型的值
            List<String> values = new ArrayList<>();
            if (value instanceof Iterable) {
                for (Object item : (Iterable<?>) value) {
                    values.add(item.toString());
                }
            } else if (value.getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(value);
                for (int i = 0; i < length; i++) {
                    values.add(java.lang.reflect.Array.get(value, i).toString());
                }
            } else {
                values.add(value.toString());
            }
            
            // 转换为标签
            List<String> labels = new ArrayList<>();
            for (String val : values) {
                String label = optionMap.get(val);
                labels.add(label != null ? label : val);
            }
            
            return String.join(", ", labels);
        } catch (Exception e) {
            return value;
        }
    }
    
    @Override
    public boolean supports(String fieldType) {
        return "checkbox".equals(fieldType) || "checkbox-field".equals(fieldType);
    }
}
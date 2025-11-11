package com.form.system.formatter;

import com.form.system.entity.FormField;
import com.form.system.formatter.annotation.FormFieldFormatter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用字段格式化器
 * 处理不特定类型的字段
 */
@FormFieldFormatter
public class GenericFieldFormatter extends FieldFormatter {
    
    @Override
    public Object format(Object value, FormField field) {
        // 处理通用值的显示
        if (value == null) {
            return "";
        }
        if (value instanceof Iterable) {
            List<String> values = new ArrayList<>();
            for (Object item : (Iterable<?>) value) {
                values.add(item.toString());
            }
            return String.join(", ", values);
        }
        if (value.getClass().isArray()) {
            List<String> values = new ArrayList<>();
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                values.add(Array.get(value, i).toString());
            }
            return String.join(", ", values);
        }
        return value.toString();
    }
    
    @Override
    public boolean supports(String fieldType) {
        // 通用格式化器支持所有字段类型，作为默认选项
        return true;
    }
}
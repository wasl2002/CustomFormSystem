package com.form.system.formatter;

import com.form.system.entity.FormField;
import com.form.system.formatter.annotation.FormFieldFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期字段格式化器
 */
@FormFieldFormatter({"date", "date-field"})
public class DateFieldFormatter extends FieldFormatter {
    
    @Override
    public Object format(Object value, FormField field) {
        try {
            if (value == null) return "";
            
            // 解析字段属性获取日期格式
            String dateFormat = "yyyy-MM-dd";
            if (field.getFieldProperties() != null) {
                try {
                    com.alibaba.fastjson.JSONObject properties = com.alibaba.fastjson.JSONObject.parseObject(field.getFieldProperties());
                    String format = properties.getString("dateFormat");
                    if (format != null && !format.isEmpty()) {
                        dateFormat = format;
                    }
                } catch (Exception e) {
                    // 解析失败使用默认格式
                }
            }
            
            // 处理不同类型的日期值
            Date dateValue;
            if (value instanceof Date) {
                dateValue = (Date) value;
            } else if (value instanceof String) {
                // 尝试解析字符串日期
                dateValue = new SimpleDateFormat("yyyy-MM-dd").parse((String) value);
            } else if (value instanceof Number) {
                // 时间戳
                dateValue = new Date(((Number) value).longValue());
            } else {
                return value.toString();
            }
            
            // 格式化日期
            return new SimpleDateFormat(dateFormat).format(dateValue);
        } catch (Exception e) {
            return value.toString();
        }
    }
    
    @Override
    public boolean supports(String fieldType) {
        return "date".equals(fieldType) || "date-field".equals(fieldType);
    }
}
package com.form.system.formatter;

import com.form.system.entity.FormField;
import com.form.system.formatter.annotation.FormFieldFormatter;

import java.util.List;
import java.util.Map;

/**
 * 省市区字段格式化器
 * 示例：展示如何添加新的字段类型支持
 */
@FormFieldFormatter("region-field")
public class RegionFieldFormatter extends FieldFormatter {
    
    @Override
    public Object format(Object value, FormField field) {
        if (value == null) return "";
        
        try {
            // 省市区组件的值是一个数组，每个元素代表省、市、区的选项对象
            if (value instanceof List) {
                List<?> list = (List<?>) value;
                // 过滤掉空值并提取标签
                StringBuilder result = new StringBuilder();
                boolean first = true;
                for (Object item : list) {
                    if (item != null) {
                        if (!first) {
                            result.append("/");
                        }
                        // 如果是对象，优先使用label，其次是value，最后是对象本身
                        if (item instanceof Map) {
                            Map<?, ?> map = (Map<?, ?>) item;
                            Object label = map.get("label");
                            Object val = map.get("value");
                            result.append(label != null ? label.toString() : 
                                         (val != null ? val.toString() : item.toString()));
                        } else {
                            result.append(item.toString());
                        }
                        first = false;
                    }
                }
                return result.toString();
            }
            // 其他情况转换为字符串
            return value.toString();
        } catch (Exception e) {
            return value.toString();
        }
    }
    
    @Override
    public boolean supports(String fieldType) {
        return "region-field".equals(fieldType);
    }
}
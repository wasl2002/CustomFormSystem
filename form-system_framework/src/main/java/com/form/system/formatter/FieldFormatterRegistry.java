package com.form.system.formatter;

import com.form.system.entity.FormField;
import com.form.system.formatter.annotation.FormFieldFormatter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 字段格式化器注册中心
 * 管理所有字段格式化器的注册和查找
 */
public class FieldFormatterRegistry {
    
    private final List<FieldFormatter> formatters = new ArrayList<>();
    
    public FieldFormatterRegistry() {
        // 默认构造函数，格式化器将通过Spring自动注册
    }
    
    /**
     * 构造函数，用于手动注册格式化器
     * @param formatters 格式化器列表
     */
    public FieldFormatterRegistry(List<FieldFormatter> formatters) {
        this.formatters.addAll(formatters);
        // 按优先级排序
        sortFormatters();
    }
    
    /**
     * 注册字段格式化器
     * @param formatter 字段格式化器
     */
    public void register(FieldFormatter formatter) {
        formatters.add(formatter);
        // 每次添加新格式化器后重新排序
        sortFormatters();
    }
    
    /**
     * 根据优先级对格式化器进行排序
     */
    private void sortFormatters() {
        formatters.sort(Comparator.comparingInt(this::getFormatterOrder));
    }
    
    /**
     * 获取格式化器的优先级
     * @param formatter 格式化器
     * @return 优先级数值
     */
    private int getFormatterOrder(FieldFormatter formatter) {
        Class<? extends FieldFormatter> clazz = formatter.getClass();
        if (clazz.isAnnotationPresent(FormFieldFormatter.class)) {
            FormFieldFormatter annotation = clazz.getAnnotation(FormFieldFormatter.class);
            return annotation.order();
        }
        return 0; // 默认优先级
    }
    
    /**
     * 根据字段类型获取对应的格式化器
     * @param fieldType 字段类型
     * @return 对应的格式化器
     */
    public FieldFormatter getFormatter(String fieldType) {
        // 按优先级顺序查找支持该字段类型的格式化器
        for (FieldFormatter formatter : formatters) {
            if (formatter.supports(fieldType)) {
                // 通用格式化器作为默认选项，应该放在最后
                if (!(formatter instanceof GenericFieldFormatter)) {
                    return formatter;
                }
            }
        }
        
        // 如果没有找到特定的格式化器，返回通用格式化器
        for (FieldFormatter formatter : formatters) {
            if (formatter instanceof GenericFieldFormatter) {
                return formatter;
            }
        }
        
        return null;
    }
    
    /**
     * 格式化字段值
     * @param value 原始字段值
     * @param field 字段配置
     * @return 格式化后的值
     */
    public Object formatFieldValue(Object value, FormField field) {
        if (field == null) {
            FieldFormatter genericFormatter = getFormatter(null);
            return genericFormatter != null ? genericFormatter.format(value, field) : value;
        }
        
        FieldFormatter formatter = getFormatter(field.getFieldType());
        if (formatter != null) {
            return formatter.format(value, field);
        }
        
        // 如果没有找到格式化器，返回原始值
        return value;
    }
}
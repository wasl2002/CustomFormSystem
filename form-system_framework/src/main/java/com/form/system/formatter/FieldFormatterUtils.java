package com.form.system.formatter;

import java.util.HashMap;
import java.util.Map;

/**
 * 字段格式化工具类
 */
public class FieldFormatterUtils {
    
    /**
     * 解析字段选项字符串为Map，格式为"key=value"
     * @param optionsStr 字段选项字符串
     * @return 解析后的选项Map，键为选项值，值为选项标签
     */
    public static Map<String, String> parseFieldOptions(String optionsStr) {
        Map<String, String> optionMap = new HashMap<>();
        if (optionsStr == null || optionsStr.isEmpty()) {
            return optionMap;
        }
        
        try {
            String[] lines = optionsStr.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    optionMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (Exception e) {
            // 解析失败忽略
        }
        
        return optionMap;
    }
}
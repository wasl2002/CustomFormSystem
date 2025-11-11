package com.form.system.formatter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 字段格式化器配置属性
 */
@ConfigurationProperties(prefix = "form.field.formatter")
public class FieldFormatterProperties {
    
    /**
     * 是否启用自动注册
     */
    private boolean autoRegister = true;
    
    /**
     * 扫描包路径
     */
    private String[] scanPackages = {"com.form.system.formatter"};
    
    public boolean isAutoRegister() {
        return autoRegister;
    }
    
    public void setAutoRegister(boolean autoRegister) {
        this.autoRegister = autoRegister;
    }
    
    public String[] getScanPackages() {
        return scanPackages;
    }
    
    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }
}
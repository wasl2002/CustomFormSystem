package com.form.system.formatter.config;

import com.form.system.formatter.FieldFormatter;
import com.form.system.formatter.FieldFormatterRegistry;
import com.form.system.formatter.annotation.FormFieldFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 字段格式化器自动配置类
 * 自动扫描并注册所有标记为FormFieldFormatter的格式化器
 */
@Configuration
@EnableConfigurationProperties(FieldFormatterProperties.class)
@ComponentScan(basePackages = "com.form.system.formatter")
public class FieldFormatterAutoConfiguration {
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private FieldFormatterProperties properties;
    
    /**
     * 创建字段格式化器注册中心Bean
     * 自动注册所有标记为FormFieldFormatter的格式化器
     * @return FieldFormatterRegistry实例
     */
    @Bean
    public FieldFormatterRegistry fieldFormatterRegistry() {
        FieldFormatterRegistry registry = new FieldFormatterRegistry();
        
        // 检查是否启用自动注册
        if (properties.isAutoRegister()) {
            // 从Spring容器中获取所有标记为FormFieldFormatter的Bean
            Map<String, Object> formatterBeans = applicationContext.getBeansWithAnnotation(FormFieldFormatter.class);
            
            // 注册所有找到的格式化器
            for (Object bean : formatterBeans.values()) {
                if (bean instanceof FieldFormatter) {
                    registry.register((FieldFormatter) bean);
                }
            }
        }
        
        return registry;
    }
}
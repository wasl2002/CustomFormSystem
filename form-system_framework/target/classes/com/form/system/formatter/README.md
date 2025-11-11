# 字段格式化器系统

## 简介

字段格式化器系统是一个基于Spring Boot的可扩展字段格式化框架，支持自动注册和自定义字段格式化器。

## 快速开始

### 1. 创建自定义字段格式化器

```java
@Component
@FormFieldFormatter("phone")
public class PhoneFieldFormatter extends FieldFormatter {
    
    @Override
    public Object format(Object value, FormField field) {
        // 实现格式化逻辑
        return formattedValue;
    }
    
    @Override
    public boolean supports(String fieldType) {
        return "phone".equals(fieldType);
    }
}
```

### 2. 配置文件

```properties
# 启用自动注册
form.field.formatter.auto-register=true

# 扫描包路径
form.field.formatter.scan-packages=com.form.system.formatter,com.custom.formatters
```

## 使用示例

```java
@Autowired
private FieldFormatterManager fieldFormatterManager;

// 格式化字段值
Object formattedValue = fieldFormatterManager.formatFieldValue(value, field);
```

## 高级用法

### 设置优先级

```java
@FormFieldFormatter(value = "phone", order = -1) // 高优先级
public class HighPriorityPhoneFormatter extends FieldFormatter {
    // ...
}
```

### 禁用自动注册

```properties
form.field.formatter.auto-register=false
```
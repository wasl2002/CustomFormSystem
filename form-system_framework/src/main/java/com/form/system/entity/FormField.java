package com.form.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("form_field")
public class FormField {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 关联表单ID
     */
    private Long formId;
    
    /**
     * 字段唯一标识
     */
    private String fieldKey;
    
    /**
     * 字段名称
     */
    private String fieldName;
    
    /**
     * 字段类型：input-输入框，textarea-文本域，select-下拉框，radio-单选框，checkbox-复选框，date-日期，datetime-日期时间等
     */
    private String fieldType;
    
    /**
     * 字段选项配置(JSON格式)
     */
    private String fieldOptions;
    
    /**
     * 字段校验规则(JSON格式)
     */
    private String fieldValidations;
    
    /**
     * 字段属性配置(JSON格式)
     */
    private String fieldProperties;
    
    /**
     * 是否必填：0-非必填，1-必填
     */
    private Integer isRequired;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 状态：1-启用，2-禁用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
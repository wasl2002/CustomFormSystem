package com.form.system.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FormConfigDTO {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 表单唯一标识
     */
    private String formKey;
    
    /**
     * 表单名称
     */
    private String formName;
    
    /**
     * 表单描述
     */
    private String formDescription;
    
    /**
     * 表单版本
     */
    private Integer formVersion;
    
    /**
     * 状态：1-草稿，2-已发布，3-停用
     */
    private Integer status;
    
    /**
     * 创建人
     */
    private String createdBy;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    
    /**
     * 更新人
     */
    private String updatedBy;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
    
    /**
     * 表单字段列表
     */
    private List<FormFieldDTO> fields;
}
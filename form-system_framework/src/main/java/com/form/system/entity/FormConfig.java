package com.form.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("form_config")
public class FormConfig {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 父版本ID
     */
    private Long parentId;
    
    /**
     * 是否最新版本：0-否，1-是
     */
    private Integer isLatest;
    
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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    /**
     * 更新人
     */
    private String updatedBy;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
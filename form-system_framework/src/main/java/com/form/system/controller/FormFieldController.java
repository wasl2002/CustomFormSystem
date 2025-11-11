package com.form.system.controller;

import com.form.system.entity.FormField;
import com.form.system.entity.FormConfig;
import com.form.system.service.FormFieldService;
import com.form.system.service.FormConfigService;
import com.form.system.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/form/field")
public class FormFieldController {
    
    private static final Logger logger = LoggerFactory.getLogger(FormFieldController.class);
    
    @Autowired
    private FormFieldService formFieldService;
    
    @Autowired
    private FormConfigService formConfigService;
    
    /**
     * 根据表单ID获取所有字段配置
     */
    @GetMapping("/list/{formId}")
    public Result<List<FormField>> getFieldsByFormId(@PathVariable Long formId) {
        List<FormField> formFields = formFieldService.list(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FormField>()
                .eq("form_id", formId)
                .orderByAsc("sort_order")
        );
        return Result.success(formFields);
    }
    
    /**
     * 根据表单Key获取所有字段配置
     */
    @GetMapping("/list/key/{formKey}")
    public Result<List<FormField>> getFieldsByFormKey(@PathVariable String formKey) {
        // 先根据formKey找到formId
        FormConfig formConfig = formConfigService.getOne(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FormConfig>()
                .eq("form_key", formKey)
                .eq("is_latest", 1)
        );
        
        if (formConfig == null) {
            return Result.error("表单不存在");
        }
        
        // 再根据formId获取字段配置
        List<FormField> formFields = formFieldService.list(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FormField>()
                .eq("form_id", formConfig.getId())
                .orderByAsc("sort_order")
        );
        
        return Result.success(formFields);
    }
    
    /**
     * 根据表单Key和版本号获取字段配置
     */
    @GetMapping("/list/key/{formKey}/version/{version}")
    public Result<List<FormField>> getFieldsByFormKeyAndVersion(@PathVariable String formKey, @PathVariable Integer version) {
        // 先根据formKey和版本号找到对应的表单配置
        FormConfig formConfig = formConfigService.getOne(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FormConfig>()
                .eq("form_key", formKey)
                .eq("form_version", version)
        );
        
        if (formConfig == null) {
            return Result.error("表单不存在");
        }
        
        // 再根据formId获取字段配置
        List<FormField> formFields = formFieldService.list(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FormField>()
                .eq("form_id", formConfig.getId())
                .orderByAsc("sort_order")
        );
        
        return Result.success(formFields);
    }
    
    /**
     * 创建字段配置
     */
    @PostMapping("/create")
    public Result<Boolean> createField(@RequestBody FormField formField) {
        boolean result = formFieldService.save(formField);
        return Result.success(result);
    }
    
    /**
     * 批量创建字段配置
     */
    @PostMapping("/batch-create")
    public Result<Boolean> batchCreateFields(@RequestBody List<FormField> formFields) {
        try {
            // 确保新创建的字段不包含ID，让数据库自动生成
            formFields.forEach(field -> {
                // 如果ID不为null（可能是前端传递的已存在字段或临时ID），设置为null让数据库自动生成
                if (field.getId() != null) {
                    field.setId(null);
                }
            });
            
            boolean result = formFieldService.saveBatch(formFields);
            return Result.success(result);
        } catch (Exception e) {
            logger.error("批量创建字段配置失败", e);
            return Result.error("批量创建字段配置失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新字段配置
     */
    @PutMapping("/update")
    public Result<Boolean> updateField(@RequestBody FormField formField) {
        boolean result = formFieldService.updateById(formField);
        return Result.success(result);
    }
    
    /**
     * 删除字段配置
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteField(@PathVariable Long id) {
        boolean result = formFieldService.removeById(id);
        return Result.success(result);
    }
}
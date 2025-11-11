package com.form.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.form.system.entity.FormConfig;
import com.form.system.service.FormConfigService;
import com.form.system.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/form/config")
public class FormConfigController {
    
    @Autowired
    private FormConfigService formConfigService;
    
    /**
     * 获取所有表单配置
     */
    @GetMapping("/list")
    public Result<List<FormConfig>> getAllFormConfigs() {
        List<FormConfig> formConfigs = formConfigService.list(
            new QueryWrapper<FormConfig>()
                .eq("is_latest", 1)
                .orderByDesc("created_time")
        );
        return Result.success(formConfigs);
    }
    
    /**
     * 根据ID获取表单配置
     */
    @GetMapping("/id/{id}")
    public Result<FormConfig> getFormConfigById(@PathVariable Long id) {
        FormConfig formConfig = formConfigService.getById(id);
        return Result.success(formConfig);
    }
    
    /**
     * 根据formKey获取表单配置
     */
    @GetMapping("/{formKey}")
    public Result<FormConfig> getFormConfigByFormKey(@PathVariable String formKey) {
        FormConfig formConfig = formConfigService.getOne(
            new QueryWrapper<FormConfig>()
                .eq("form_key", formKey)
                .eq("is_latest", 1)
        );
        return Result.success(formConfig);
    }
    
    /**
     * 根据formKey获取所有版本的表单配置
     */
    @GetMapping("/versions/{formKey}")
    public Result<List<FormConfig>> getFormConfigVersions(@PathVariable String formKey) {
        // 先找到最新的表单配置
        FormConfig latestConfig = formConfigService.getOne(
            new QueryWrapper<FormConfig>()
                .eq("form_key", formKey)
                .eq("is_latest", 1)
        );
        
        if (latestConfig == null) {
            return Result.error("表单不存在");
        }
        
        // 获取所有版本的表单配置
        List<FormConfig> formConfigs = formConfigService.list(
            new QueryWrapper<FormConfig>()
                .eq("form_key", formKey)
                .orderByDesc("form_version")
        );
        
        return Result.success(formConfigs);
    }
    
    /**
     * 创建表单配置
     */
    @PostMapping("/create")
    public Result<FormConfig> createFormConfig(@RequestBody FormConfig formConfig) {
        // 设置默认值
        if (formConfig.getFormVersion() == null) {
            formConfig.setFormVersion(1);
        }
        if (formConfig.getIsLatest() == null) {
            formConfig.setIsLatest(1);
        }
        if (formConfig.getStatus() == null) {
            formConfig.setStatus(1); // 草稿状态
        }
        
        // 如果没有设置formKey，则生成一个唯一的
        if (formConfig.getFormKey() == null || formConfig.getFormKey().isEmpty()) {
            formConfig.setFormKey("form_" + System.currentTimeMillis());
        }
        
        boolean result = formConfigService.save(formConfig);
        if (result) {
            return Result.success(formConfig);
        } else {
            return Result.error("创建表单失败");
        }
    }
    
    /**
     * 更新表单配置（创建新版本）
     */
    @PostMapping("/update/{formKey}")
    public Result<FormConfig> updateFormConfig(@PathVariable String formKey, @RequestBody FormConfig updatedFormConfig) {
        // 查找当前最新版本
        FormConfig currentLatest = formConfigService.getOne(
            new QueryWrapper<FormConfig>()
                .eq("form_key", formKey)
                .eq("is_latest", 1)
        );
        
        if (currentLatest == null) {
            return Result.error("表单不存在");
        }
        
        // 将当前最新版本标记为非最新
        currentLatest.setIsLatest(0);
        formConfigService.updateById(currentLatest);
        
        // 创建新版本
        FormConfig newVersion = new FormConfig();
        newVersion.setFormKey(formKey);
        newVersion.setFormName(updatedFormConfig.getFormName());
        newVersion.setFormDescription(updatedFormConfig.getFormDescription());
        newVersion.setFormVersion(currentLatest.getFormVersion() + 1);
        newVersion.setParentId(currentLatest.getId());
        newVersion.setIsLatest(1);
        newVersion.setStatus(updatedFormConfig.getStatus() != null ? updatedFormConfig.getStatus() : currentLatest.getStatus());
        newVersion.setCreatedBy(updatedFormConfig.getCreatedBy());
        newVersion.setUpdatedBy(updatedFormConfig.getUpdatedBy());
        
        boolean result = formConfigService.save(newVersion);
        if (result) {
            return Result.success(newVersion);
        } else {
            // 回滚操作
            currentLatest.setIsLatest(1);
            formConfigService.updateById(currentLatest);
            return Result.error("更新表单失败");
        }
    }
    
    /**
     * 删除表单配置
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteFormConfig(@PathVariable Long id) {
        // 检查是否是最新版本
        FormConfig formConfig = formConfigService.getById(id);
        if (formConfig == null) {
            return Result.error("表单不存在");
        }
        
        // 如果是最新版本，需要更新父版本为最新
        if (formConfig.getIsLatest() == 1) {
            if (formConfig.getParentId() != null) {
                FormConfig parentConfig = formConfigService.getById(formConfig.getParentId());
                if (parentConfig != null) {
                    parentConfig.setIsLatest(1);
                    formConfigService.updateById(parentConfig);
                }
            }
        }
        
        boolean result = formConfigService.removeById(id);
        return Result.success(result);
    }
    
    /**
     * 发布表单
     */
    @PutMapping("/publish/{id}")
    public Result<Boolean> publishForm(@PathVariable Long id) {
        FormConfig formConfig = formConfigService.getById(id);
        if (formConfig != null) {
            formConfig.setStatus(2); // 已发布
            boolean result = formConfigService.updateById(formConfig);
            return Result.success(result);
        }
        return Result.error("表单不存在");
    }
    
    /**
     * 停用表单
     */
    @PutMapping("/disable/{id}")
    public Result<Boolean> disableForm(@PathVariable Long id) {
        FormConfig formConfig = formConfigService.getById(id);
        if (formConfig != null) {
            formConfig.setStatus(3); // 停用
            boolean result = formConfigService.updateById(formConfig);
            return Result.success(result);
        }
        return Result.error("表单不存在");
    }
    
    /**
     * 启用表单（将停用的表单重新启用）
     */
    @PutMapping("/enable/{id}")
    public Result<Boolean> enableForm(@PathVariable Long id) {
        FormConfig formConfig = formConfigService.getById(id);
        if (formConfig != null) {
            // 只有停用状态的表单才能被启用
            if (formConfig.getStatus() == 3) {
                formConfig.setStatus(2); // 设置为已发布状态
                boolean result = formConfigService.updateById(formConfig);
                return Result.success(result);
            } else {
                return Result.error("只有停用状态的表单才能被启用");
            }
        }
        return Result.error("表单不存在");
    }
    
    /**
     * 回滚到指定版本
     */
    @PutMapping("/rollback/{formKey}/{version}")
    public Result<FormConfig> rollbackToVersion(@PathVariable String formKey, @PathVariable Integer version) {
        // 查找目标版本
        FormConfig targetVersion = formConfigService.getOne(
            new QueryWrapper<FormConfig>()
                .eq("form_key", formKey)
                .eq("form_version", version)
        );
        
        if (targetVersion == null) {
            return Result.error("指定版本不存在");
        }
        
        // 查找当前最新版本
        FormConfig currentLatest = formConfigService.getOne(
            new QueryWrapper<FormConfig>()
                .eq("form_key", formKey)
                .eq("is_latest", 1)
        );
        
        // 如果目标版本已经是最新版本，则不需要回滚
        if (targetVersion.getId().equals(currentLatest.getId())) {
            return Result.success(targetVersion);
        }
        
        // 将当前最新版本标记为非最新
        if (currentLatest != null) {
            currentLatest.setIsLatest(0);
            formConfigService.updateById(currentLatest);
        }
        
        // 创建一个新的版本，基于目标版本的内容
        FormConfig newVersion = new FormConfig();
        newVersion.setFormKey(formKey);
        newVersion.setFormName(targetVersion.getFormName());
        newVersion.setFormDescription(targetVersion.getFormDescription());
        // 版本号应该是当前最新版本号+1，而不是目标版本号
        newVersion.setFormVersion(currentLatest != null ? currentLatest.getFormVersion() + 1 : targetVersion.getFormVersion() + 1);
        newVersion.setParentId(targetVersion.getId());
        newVersion.setIsLatest(1);
        newVersion.setStatus(targetVersion.getStatus());
        newVersion.setCreatedBy(targetVersion.getCreatedBy());
        newVersion.setUpdatedBy(targetVersion.getUpdatedBy());
        
        boolean result = formConfigService.save(newVersion);
        
        if (result) {
            return Result.success(newVersion);
        } else {
            // 回滚操作
            if (currentLatest != null) {
                currentLatest.setIsLatest(1);
                formConfigService.updateById(currentLatest);
            }
            return Result.error("回滚失败");
        }
    }
    
    /**
     * 根据ID获取表单配置（包括历史版本）
     */
    @GetMapping("/version/id/{id}")
    public Result<FormConfig> getFormConfigByVersionId(@PathVariable Long id) {
        FormConfig formConfig = formConfigService.getById(id);
        return Result.success(formConfig);
    }
}
package com.form.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.form.system.entity.FormConfig;
import com.form.system.entity.FormField;
import com.form.system.service.FormConfigService;
import com.form.system.service.FormDataService;
import com.form.system.utils.ExcelExportUtil;
import com.form.system.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/form/data")
public class FormDataController {

    @Autowired
    private FormDataService formDataService;

    @Autowired
    private FormConfigService formConfigService;

    /**
     * 保存表单数据
     */
    @PostMapping("/save/{formKey}")
    public Result<Boolean> saveFormData(@PathVariable String formKey, @RequestBody JSONObject formData) {
        // 获取最新版本的表单配置
        FormConfig formConfig = formConfigService.getOne(
                new QueryWrapper<FormConfig>()
                        .eq("form_key", formKey)
                        .eq("is_latest", 1)
        );

        if (formConfig == null) {
            return Result.error("表单不存在");
        }

        boolean result = formDataService.saveFormData(formKey, formConfig.getFormVersion(), formData);
        return Result.success(result);
    }

    /**
     * 更新表单数据
     */
    @PutMapping("/update/{formKey}/{dataId}")
    public Result<Boolean> updateFormData(@PathVariable String formKey, @PathVariable Long dataId, @RequestBody JSONObject formData) {
        // 获取最新版本的表单配置
        FormConfig formConfig = formConfigService.getOne(
                new QueryWrapper<FormConfig>()
                        .eq("form_key", formKey)
                        .eq("is_latest", 1)
        );

        if (formConfig == null) {
            return Result.error("表单不存在");
        }

        boolean result = formDataService.updateFormData(formKey, dataId, formConfig.getFormVersion(), formData);
        return Result.success(result);
    }

    /**
     * 删除表单数据
     */
    @DeleteMapping("/delete/{formKey}/{dataId}")
    public Result<Boolean> deleteFormData(@PathVariable String formKey, @PathVariable Long dataId) {
        boolean result = formDataService.deleteFormData(formKey, dataId);
        return Result.success(result);
    }

    /**
     * 根据ID查询表单数据（最新版本）
     */
    @GetMapping("/{formKey}/{dataId}")
    public Result<JSONObject> getFormDataById(@PathVariable String formKey, @PathVariable Long dataId) {
        // 获取最新版本的表单配置
        FormConfig formConfig = formConfigService.getOne(
                new QueryWrapper<FormConfig>()
                        .eq("form_key", formKey)
                        .eq("is_latest", 1)
        );

        if (formConfig == null) {
            return Result.error("表单不存在");
        }

        JSONObject data = formDataService.getFormDataById(formKey, formConfig.getFormVersion(), dataId);
        return Result.success(data);
    }

    /**
     * 根据ID和版本查询表单数据（指定版本）
     */
    @GetMapping("/{formKey}/{dataId}/version/{version}")
    public Result<JSONObject> getFormDataByIdAndVersion(@PathVariable String formKey, @PathVariable Long dataId, @PathVariable Integer version) {
        JSONObject data = formDataService.getFormDataById(formKey, version, dataId);
        return Result.success(data);
    }

    /**
     * 导出表单数据为Excel
     */
    @GetMapping("/export/{formKey}")
    public void exportFormDataToExcel(
            @PathVariable String formKey,
            @RequestParam(required = false) Integer version,
            @RequestParam(required = false) String searchConditions,
            HttpServletResponse response) {
        try {
            List<JSONObject> dataList;

            // 解析多条件搜索参数
            String decodedConditions = java.net.URLDecoder.decode(searchConditions, "UTF-8");
            List<Map<String, String>> conditions = new ArrayList<>();
            List<Map> rawConditions = JSONObject.parseArray(decodedConditions, Map.class);
            for (Map rawCondition : rawConditions) {
                Map<String, String> condition = new HashMap<>();
                for (Object entry : rawCondition.entrySet()) {
                    Map.Entry<String, Object> mapEntry = (Map.Entry<String, Object>) entry;
                    condition.put(mapEntry.getKey(), mapEntry.getValue().toString());
                }
                conditions.add(condition);
            }

            // 使用多条件搜索
            if (version != null) {
                dataList = formDataService.listFormDataWithMultipleConditions(formKey, version, conditions);
            } else {
                // 获取最新版本的表单配置
                FormConfig formConfig = formConfigService.getOne(
                        new QueryWrapper<FormConfig>()
                                .eq("form_key", formKey)
                                .eq("is_latest", 1)
                );

                if (formConfig == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "表单不存在");
                    return;
                }

                dataList = formDataService.listFormDataWithMultipleConditions(formKey, null, conditions);
            }

            // 获取表单字段配置，用于生成表头和数据转义
            Map<String, FormField> headers = formDataService.getFormFieldsForExport(formKey, version);

            // 获取表单字段的完整配置信息，用于数据转义
            List<com.form.system.entity.FormField> fieldConfigs = formDataService.getFormFieldsWithConfig(formKey, version);

            // 格式化导出数据，根据字段配置进行转义
            List<JSONObject> formattedDataList = formDataService.formatExportData(dataList, fieldConfigs);

            // 获取表单名称和版本
            String formName = "表单数据";
            Integer formVersion = version;
            FormConfig formConfig = null;

            if (version != null) {
                formConfig = formConfigService.getOne(
                        new QueryWrapper<FormConfig>()
                                .eq("form_key", formKey)
                                .eq("form_version", version)
                );
            } else {
                formConfig = formConfigService.getOne(
                        new QueryWrapper<FormConfig>()
                                .eq("form_key", formKey)
                                .eq("is_latest", 1)
                );
                if (formConfig != null) {
                    formVersion = formConfig.getFormVersion();
                }
            }

            if (formConfig != null) {
                formName = formConfig.getFormName();
            }

            // 生成文件名：表单名称-版本-下载时间
            String downloadTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String fileName = formName + "-v" + (formVersion != null ? formVersion : "未知") + "-" + downloadTime;

            // 导出Excel
            ExcelExportUtil.exportToExcel(formattedDataList, headers, formName, fileName, response);
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "导出失败: " + e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 多条件查询表单数据（支持分页）
     */
    @PostMapping("/list/search/{formKey}")
    public Result<List<JSONObject>> searchFormDataWithMultipleList(
            @PathVariable String formKey,
            @RequestParam(required = false) Integer version,
            @RequestBody List<Map<String, String>> searchConditions) {

        try {
            List<JSONObject> dataList;

            if (version != null) {
                dataList = formDataService.listFormDataWithMultipleConditions(formKey, version, searchConditions);
            } else {
                // 获取最新版本的表单配置
                FormConfig formConfig = formConfigService.getOne(
                        new QueryWrapper<FormConfig>()
                                .eq("form_key", formKey)
                                .eq("is_latest", 1)
                );

                if (formConfig == null) {
                    return Result.error("表单不存在");
                }

                dataList = formDataService.listFormDataWithMultipleConditions(formKey, null, searchConditions);
            }

            return Result.success(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 多条件查询表单数据（支持分页）
     */
    @PostMapping("/page/search/{formKey}")
    public Result<IPage<JSONObject>> searchFormDataWithMultiplePage(
            @PathVariable String formKey,
            @RequestParam(required = false) Integer version,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestBody List<Map<String, String>> searchConditions) {

        try {
            IPage<JSONObject> dataList;

            if (version != null) {
                dataList = formDataService.pageFormDataWithMultipleConditions(formKey, version, searchConditions, pageNum, pageSize);
            } else {
                // 获取最新版本的表单配置
                FormConfig formConfig = formConfigService.getOne(
                        new QueryWrapper<FormConfig>()
                                .eq("form_key", formKey)
                                .eq("is_latest", 1)
                );

                if (formConfig == null) {
                    return Result.error("表单不存在");
                }

                dataList = formDataService.pageFormDataWithMultipleConditions(formKey, null, searchConditions, pageNum, pageSize);
            }

            return Result.success(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败: " + e.getMessage());
        }
    }
}
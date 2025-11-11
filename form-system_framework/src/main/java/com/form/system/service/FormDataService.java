package com.form.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.form.system.entity.FormField;

import java.util.List;
import java.util.Map;

public interface FormDataService {

    boolean saveFormData(String formKey, Integer formVersion, JSONObject formData);

    boolean updateFormData(String formKey, Long dataId, Integer formVersion, JSONObject formData);

    boolean deleteFormData(String formKey, Long dataId);

    IPage<JSONObject> pageFormData(String formKey, Integer formVersion, int pageNum, int pageSize);


    List<JSONObject> listFormData(String formKey);

    List<JSONObject> listFormData(String formKey, Integer formVersion);

    /**
     * 多条件查询表单数据
     * @param formKey 表单key
     * @param formVersion 表单版本
     * @param searchConditions 搜索条件列表，每个条件包含字段名和搜索值
     * @return 符合条件的数据列表
     */
    List<JSONObject> listFormDataWithMultipleConditions(String formKey, Integer formVersion, List<Map<String, String>> searchConditions);

    /**
     * 分页多条件查询表单数据
     * @param formKey 表单key
     * @param formVersion 表单版本
     * @param searchConditions 搜索条件列表，每个条件包含字段名和搜索值
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小
     * @return 符合条件的数据列表
     */
    IPage<JSONObject> pageFormDataWithMultipleConditions(String formKey, Integer formVersion, List<Map<String, String>> searchConditions, int pageNum, int pageSize);


    JSONObject getFormDataById(String formKey, Integer formVersion, Long dataId);
    
    JSONObject getFormDataById(String formKey, Long dataId);
    
    /**
     * 获取表单字段用于导出
     * @param formKey 表单key
     * @param formVersion 表单版本，如果为null则获取最新版本
     * @return 字段映射（字段key -> 字段名称）
     */
    Map<String, FormField> getFormFieldsForExport(String formKey, Integer formVersion);
    
    /**
     * 获取表单字段的完整配置信息，用于导出时的数据转义
     * @param formKey 表单key
     * @param formVersion 表单版本，如果为null则获取最新版本
     * @return 字段配置列表
     */
    List<FormField> getFormFieldsWithConfig(String formKey, Integer formVersion);
    
    /**
     * 格式化导出数据，根据字段配置进行转义
     * @param dataList 原始数据列表
     * @param fields 字段配置列表
     * @return 格式化后的数据列表
     */
    List<JSONObject> formatExportData(List<JSONObject> dataList, List<FormField> fields);

    List<JSONObject> listFormDataWithMultipleConditions(String formKey, Integer formVersion, List<Map<String, String>> searchConditions, int pageNum, int pageSize);
}
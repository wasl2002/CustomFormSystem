package com.form.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.form.system.entity.FormData;
import com.form.system.entity.FormField;
import com.form.system.formatter.FieldFormatterManager;
import com.form.system.mapper.FormDataMapper;
import com.form.system.service.FormConfigService;
import com.form.system.service.FormDataService;
import com.form.system.service.FormFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表单数据服务实现类
 */
@Service
public class FormDataServiceImpl implements FormDataService {

    @Autowired
    private FormDataMapper formDataMapper;

    @Autowired
    private FormFieldService formFieldService;

    @Autowired
    private FormConfigService formConfigService;

    @Autowired
    private FieldFormatterManager fieldFormatterManager;

    /**
     * 保存表单数据
     *
     * @param formKey     表单键
     * @param formVersion 表单版本
     * @param formData    表单数据
     * @return 是否保存成功
     */
    @Override
    public boolean saveFormData(String formKey, Integer formVersion, JSONObject formData) {
        try {
            FormData data = new FormData();
            data.setFormKey(formKey);
            data.setFormVersion(formVersion);
            data.setFormData(formData.toJSONString());
            return formDataMapper.insert(data) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新表单数据
     *
     * @param formKey     表单键
     * @param dataId      数据ID
     * @param formVersion 表单版本
     * @param formData    表单数据
     * @return 是否更新成功
     */
    @Override
    public boolean updateFormData(String formKey, Long dataId, Integer formVersion, JSONObject formData) {
        try {
            FormData data = new FormData();
            data.setId(dataId);
            data.setFormKey(formKey);
            data.setFormVersion(formVersion);
            data.setFormData(formData.toJSONString());
            return formDataMapper.updateById(data) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除表单数据
     *
     * @param formKey 表单键
     * @param dataId  数据ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteFormData(String formKey, Long dataId) {
        try {
            return formDataMapper.deleteById(dataId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询表单数据
     *
     * @param formKey     表单键
     * @param formVersion 表单版本
     * @return 表单数据列表
     */
    @Override
    public List<JSONObject> listFormData(String formKey, Integer formVersion) {
        try {
            QueryWrapper<FormData> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FormData::getFormKey, formKey)
                    .eq(formVersion != null, FormData::getFormVersion, formVersion)
                    .orderByDesc(FormData::getUpdatedTime);
            List<FormData> dataList = formDataMapper.selectList(queryWrapper);

            List<JSONObject> result = new ArrayList<>();
            for (FormData data : dataList) {
                JSONObject jsonObject = JSONObject.parseObject(data.getFormData());
                jsonObject.put("id", data.getId());
                jsonObject.put("createdTime", data.getCreatedTime());
                jsonObject.put("updatedTime", data.getUpdatedTime());
                result.add(jsonObject);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 分页查询表单数据
     *
     * @param formKey     表单键
     * @param formVersion 表单版本
     * @param pageNum     页码
     * @param pageSize    每页数量
     * @return 分页后的表单数据列表
     */
    @Override
    public IPage<JSONObject> pageFormData(String formKey, Integer formVersion, int pageNum, int pageSize) {
        try {
            // 1. 创建分页对象
            QueryWrapper<FormData> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FormData::getFormKey, formKey)
                    .eq(formVersion != null, FormData::getFormVersion, formVersion)
                    .orderByDesc(FormData::getUpdatedTime);
            Page<FormData> formDataPage = formDataMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);

            List<JSONObject> records = new ArrayList<>();
            for (FormData data : formDataPage.getRecords()) {
                JSONObject jsonObject = JSONObject.parseObject(data.getFormData());
                jsonObject.put("id", data.getId());
                jsonObject.put("createdTime", data.getCreatedTime());
                jsonObject.put("updatedTime", data.getUpdatedTime());
                records.add(jsonObject);
            }
            // 5. 创建返回的分页对象并设置属性
            Page<JSONObject> resultPage = new Page<>();
            resultPage.setRecords(records);
            resultPage.setTotal(formDataPage.getTotal());
            resultPage.setSize(formDataPage.getSize());
            resultPage.setCurrent(formDataPage.getCurrent());
            resultPage.setPages(formDataPage.getPages());

            return resultPage;
        } catch (Exception e) {
            e.printStackTrace();
            return new Page<>(pageNum, pageSize, 0);
        }
    }

    /**
     * 查询所有版本的表单数据
     *
     * @param formKey 表单键
     * @return 表单数据列表
     */
    @Override
    public List<JSONObject> listFormData(String formKey) {
        try {
            QueryWrapper<FormData> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FormData::getFormKey, formKey)
                    .orderByDesc(FormData::getFormVersion);
            List<FormData> dataList = formDataMapper.selectList(queryWrapper);

            List<JSONObject> result = new ArrayList<>();
            for (FormData data : dataList) {
                JSONObject jsonObject = JSONObject.parseObject(data.getFormData());
                jsonObject.put("id", data.getId());
                jsonObject.put("formVersion", data.getFormVersion()); // 添加版本信息
                jsonObject.put("createdTime", data.getCreatedTime());
                jsonObject.put("updatedTime", data.getUpdatedTime());
                result.add(jsonObject);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 多条件查询表单数据
     *
     * @param formKey          表单key
     * @param formVersion      表单版本
     * @param searchConditions 搜索条件列表，每个条件包含字段名和搜索值
     * @return 符合条件的数据列表
     */
    @Override
    public List<JSONObject> listFormDataWithMultipleConditions(String formKey, Integer formVersion, List<Map<String, String>> searchConditions) {
        try {
            // 如果没有搜索条件，使用原有的查询方法
            if (searchConditions == null || searchConditions.isEmpty()) {
                return listFormData(formKey, formVersion);
            }

            // 使用MyBatis Plus的自定义SQL查询
            // 构建查询条件
            QueryWrapper<FormData> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FormData::getFormKey, formKey)
                    .eq(formVersion != null, FormData::getFormVersion, formVersion);

            // 添加JSON字段的查询条件
            boolean hasValidConditions = false;
            for (Map<String, String> condition : searchConditions) {
                String fieldKey = condition.get("fieldKey");
                String searchValue = condition.get("searchValue");

                // 如果字段名或搜索值为空，跳过这个条件
                if (fieldKey == null || fieldKey.isEmpty() || searchValue == null || searchValue.isEmpty()) {
                    continue;
                }

                // 使用MySQL的JSON查询语法
                // 注意：这需要数据库支持JSON查询功能，字段名是form_data而不是data
                queryWrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(form_data, '$." + fieldKey + "')) LIKE {0}", "%" + searchValue + "%");
                hasValidConditions = true;
            }

            // 如果没有有效的查询条件，使用基础查询
            if (!hasValidConditions) {
                return listFormData(formKey, formVersion);
            }

            // 执行查询
            List<FormData> dataList = formDataMapper.selectList(queryWrapper);

            // 转换为JSONObject列表
            return convertToJSONObjectList(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常时回退到内存过滤方式
            return new ArrayList<>();
        }
    }

    /**
     * 分页查询表单数据，支持多条件查询
     *
     * @param formKey          表单key
     * @param formVersion      表单版本
     * @param searchConditions 搜索条件列表，每个条件包含字段名和搜索值
     * @param pageNum          页码
     * @param pageSize         每页数量
     * @return 分页后的符合条件的数据列表
     */
    @Override
    public IPage<JSONObject> pageFormDataWithMultipleConditions(String formKey, Integer formVersion, List<Map<String, String>> searchConditions, int pageNum, int pageSize) {
        try {
            // 构建查询条件
            QueryWrapper<FormData> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FormData::getFormKey, formKey)
                    .eq(formVersion != null, FormData::getFormVersion, formVersion)
                    .orderByDesc(FormData::getUpdatedTime);
            // 添加多条件搜索
            if (searchConditions != null && !searchConditions.isEmpty()) {
                for (Map<String, String> condition : searchConditions) {
                    String fieldKey = condition.get("fieldKey");
                    String searchValue = condition.get("searchValue");
                    if (fieldKey != null && !fieldKey.isEmpty() && searchValue != null && !searchValue.isEmpty()) {
                        // 使用JSON函数查询JSON字段中的值，注意字段名是form_data而不是data
                        queryWrapper.apply("JSON_UNQUOTE(JSON_EXTRACT(form_data, '$." + fieldKey + "')) LIKE {0}", "%" + searchValue + "%");
                    }
                }
            }

            // 执行分页查询
            IPage<FormData> formDataPage = formDataMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);

            // 转换为JSONObject列表
            List<JSONObject> records = convertToJSONObjectList(formDataPage.getRecords());

            // 创建返回的分页对象并设置属性
            Page<JSONObject> resultPage = new Page<>();
            resultPage.setRecords(records);
            resultPage.setTotal(formDataPage.getTotal());
            resultPage.setSize(formDataPage.getSize());
            resultPage.setCurrent(formDataPage.getCurrent());
            resultPage.setPages(formDataPage.getPages());

            return resultPage;
        } catch (Exception e) {
            e.printStackTrace();
            return new Page<>(pageNum, pageSize, 0);
        }
    }

    /**
     * 根据ID查询表单数据
     *
     * @param formKey     表单key
     * @param formVersion 表单版本
     * @param dataId      数据ID
     * @return 符合条件的表单数据 JSONObject
     */
    @Override
    public JSONObject getFormDataById(String formKey, Integer formVersion, Long dataId) {
        try {
            QueryWrapper<FormData> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FormData::getFormKey, formKey)
                    .eq(FormData::getFormVersion, formVersion)
                    .eq(FormData::getId, dataId);
            FormData data = formDataMapper.selectOne(queryWrapper);

            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.getFormData());
                jsonObject.put("id", data.getId());
                jsonObject.put("createdTime", data.getCreatedTime());
                jsonObject.put("updatedTime", data.getUpdatedTime());
                return jsonObject;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ID查询表单数据（默认最新版本）
     *
     * @param formKey 表单key
     * @param dataId  数据ID
     * @return 符合条件的表单数据 JSONObject
     */
    @Override
    public JSONObject getFormDataById(String formKey, Long dataId) {
        try {
            QueryWrapper<FormData> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FormData::getFormKey, formKey)
                    .eq(FormData::getId, dataId).orderByDesc(FormData::getFormVersion);
            FormData data = formDataMapper.selectOne(queryWrapper);

            if (data != null) {
                JSONObject jsonObject = JSONObject.parseObject(data.getFormData());
                jsonObject.put("id", data.getId());
                jsonObject.put("formVersion", data.getFormVersion()); // 添加版本信息
                jsonObject.put("createdTime", data.getCreatedTime());
                jsonObject.put("updatedTime", data.getUpdatedTime());
                return jsonObject;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取表单字段（用于导出）
     *
     * @param formKey     表单key
     * @param formVersion 表单版本
     * @return 表单字段映射，键为字段key，值为FormField对象
     */
    @Override
    public Map<String, FormField> getFormFieldsForExport(String formKey, Integer formVersion) {
        try {
            // 获取表单ID
            Long formId = null;
            if (formVersion != null) {
                // 获取指定版本的表单配置
                com.form.system.entity.FormConfig formConfig = formConfigService.getOne(
                        new QueryWrapper<com.form.system.entity.FormConfig>()
                                .eq("form_key", formKey)
                                .eq("form_version", formVersion)
                );
                if (formConfig != null) {
                    formId = formConfig.getId();
                }
            } else {
                // 获取最新版本的表单配置
                com.form.system.entity.FormConfig formConfig = formConfigService.getOne(
                        new QueryWrapper<com.form.system.entity.FormConfig>()
                                .eq("form_key", formKey)
                                .eq("is_latest", 1)
                );
                if (formConfig != null) {
                    formId = formConfig.getId();
                }
            }

            if (formId == null) {
                return new HashMap<>();
            }

            // 获取表单字段
            List<FormField> fields = formFieldService.list(
                    new QueryWrapper<FormField>()
                            .eq("form_id", formId)
                            .orderByAsc("sort_order")
            );

            // 构建字段映射
            Map<String, FormField> fieldMap = new HashMap<>();
            for (FormField field : fields) {
                fieldMap.put(field.getFieldKey(), field);
            }

            return fieldMap;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String, FormField>();
        }
    }

    /**
     * 获取表单字段的完整配置信息，用于导出时的数据转义
     *
     * @param formKey     表单key
     * @param formVersion 表单版本
     * @return 包含字段配置的 FormField 列表
     */
    @Override
    public List<FormField> getFormFieldsWithConfig(String formKey, Integer formVersion) {
        try {
            // 获取表单ID
            Long formId = null;
            if (formVersion != null) {
                // 获取指定版本的表单配置
                com.form.system.entity.FormConfig formConfig = formConfigService.getOne(
                        new QueryWrapper<com.form.system.entity.FormConfig>()
                                .eq("form_key", formKey)
                                .eq("form_version", formVersion)
                );
                if (formConfig != null) {
                    formId = formConfig.getId();
                }
            } else {
                // 获取最新版本的表单配置
                com.form.system.entity.FormConfig formConfig = formConfigService.getOne(
                        new QueryWrapper<com.form.system.entity.FormConfig>()
                                .eq("form_key", formKey)
                                .eq("is_latest", 1)
                );
                if (formConfig != null) {
                    formId = formConfig.getId();
                }
            }

            if (formId == null) {
                return new ArrayList<>();
            }

            // 获取表单字段
            return formFieldService.list(
                    new QueryWrapper<FormField>()
                            .eq("form_id", formId)
                            .orderByAsc("sort_order")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 格式化导出数据，根据字段配置进行转义
     *
     * @param dataList 原始数据列表，每个元素为一个 JSONObject
     * @param fields   表单字段配置列表
     * @return 格式化后的数据列表，每个元素为一个 JSONObject
     */
    @Override
    public List<JSONObject> formatExportData(List<JSONObject> dataList, List<FormField> fields) {
        // 创建字段配置映射
        Map<String, FormField> fieldMap = new HashMap<>();
        for (FormField field : fields) {
            fieldMap.put(field.getFieldKey(), field);
        }

        // 格式化每条数据
        List<JSONObject> formattedData = new ArrayList<>();
        for (JSONObject data : dataList) {
            JSONObject formattedRow = new JSONObject();

            // 复制原始数据
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                formattedRow.put(entry.getKey(), entry.getValue());
            }

            // 根据字段配置格式化值
            for (FormField field : fields) {
                String fieldKey = field.getFieldKey();
                Object value = data.get(fieldKey);

                if (value != null) {
                    // 根据字段类型进行转义
                    Object formattedValue = fieldFormatterManager.formatFieldValue(value, field);
                    formattedRow.put(fieldKey, formattedValue);
                }
            }

            formattedData.add(formattedRow);
        }

        return formattedData;
    }

    /**
     * 根据字段配置格式化字段值
     *
     * @param value 原始字段值
     * @param field 表单字段配置
     * @return 格式化后的字段值
     */
    private Object formatFieldValue(Object value, FormField field) {
        // 使用统一的字段格式化工具
        return fieldFormatterManager.formatFieldValue(value, field);
    }

    /**
     * 解析字段选项字符串为Map，格式为"key=value"
     *
     * @param optionsStr 字段选项字符串
     * @return 解析后的选项Map，键为选项值，值为选项标签
     */
    private Map<String, String> parseFieldOptions(String optionsStr) {
        return com.form.system.formatter.FieldFormatterUtils.parseFieldOptions(optionsStr);
    }

    /**
     * 将FormData列表转换为JSONObject列表，包含ID、创建时间、更新时间
     *
     * @param dataList FormData列表
     * @return JSONObject列表
     */
    private List<JSONObject> convertToJSONObjectList(List<FormData> dataList) {
        List<JSONObject> result = new ArrayList<>();
        for (FormData data : dataList) {
            JSONObject jsonObject = JSONObject.parseObject(data.getFormData());
            jsonObject.put("id", data.getId());
            jsonObject.put("createdTime", data.getCreatedTime());
            jsonObject.put("updatedTime", data.getUpdatedTime());
            result.add(jsonObject);
        }
        return result;
    }

    /**
     * 根据表单键、版本、多条件搜索参数、分页参数查询表单数据
     *
     * @param formKey          表单键
     * @param formVersion      表单版本
     * @param searchConditions 多条件搜索参数，每个Map包含fieldKey和searchValue
     * @param pageNum          页码，从1开始
     * @param pageSize         每页数量
     * @return 符合条件的表单数据JSONObject列表，包含ID、创建时间、更新时间
     */
    @Override
    public List<JSONObject> listFormDataWithMultipleConditions(String formKey, Integer formVersion, List<Map<String, String>> searchConditions, int pageNum, int pageSize) {
        try {
            // 获取表单ID
            Long formId = getFormId(formKey, formVersion);
            if (formId == null) {
                return new ArrayList<>();
            }

            // 构建查询SQL
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT * FROM form_data WHERE form_id = ").append(formId);

            // 添加多条件搜索
            if (searchConditions != null && !searchConditions.isEmpty()) {
                for (Map<String, String> condition : searchConditions) {
                    String fieldKey = condition.get("fieldKey");
                    String searchValue = condition.get("searchValue");
                    if (fieldKey != null && !fieldKey.isEmpty() && searchValue != null && !searchValue.isEmpty()) {
                        // 使用JSON函数查询JSON字段中的值
                        sqlBuilder.append(" AND JSON_UNQUOTE(JSON_EXTRACT(data, '$.").append(fieldKey).append("')) LIKE '%").append(searchValue).append("%'");
                    }
                }
            }

            // 添加分页
            sqlBuilder.append(" ORDER BY id DESC LIMIT ").append((pageNum - 1) * pageSize).append(", ").append(pageSize);

            // 执行查询
            List<FormData> dataList = formDataMapper.selectListByDynamicSql(sqlBuilder.toString());

            // 转换为JSONObject列表
            return convertToJSONObjectList(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 获取表单ID，根据表单键和版本号
     *
     * @param formKey     表单键
     * @param formVersion 表单版本号
     * @return 表单ID，若不存在则返回null
     */
    private Long getFormId(String formKey, Integer formVersion) {
        try {
            if (formVersion != null) {
                // 获取指定版本的表单配置
                com.form.system.entity.FormConfig formConfig = formConfigService.getOne(
                        new QueryWrapper<com.form.system.entity.FormConfig>()
                                .eq("form_key", formKey)
                                .eq("form_version", formVersion)
                );
                if (formConfig != null) {
                    return formConfig.getId();
                }
            } else {
                // 获取最新版本的表单配置
                com.form.system.entity.FormConfig formConfig = formConfigService.getOne(
                        new QueryWrapper<com.form.system.entity.FormConfig>()
                                .eq("form_key", formKey)
                                .eq("is_latest", 1)
                );
                if (formConfig != null) {
                    return formConfig.getId();
                }
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
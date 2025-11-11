package com.form.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.form.system.entity.FormConfig;
import com.form.system.service.FormDataService;
import com.form.system.service.FormConfigService;
import com.form.system.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/form/statistics")
public class FormStatisticsController {
    
    @Autowired
    private FormDataService formDataService;
    
    @Autowired
    private FormConfigService formConfigService;
    
    /**
     * 获取表单数据统计信息，包括总数据量、今日新增数据量
     * @param formKey 表单键
     * @return 包含统计信息的Map，键包括formKey、formVersion、totalDataCount、todayDataCount
     */
    @GetMapping("/overview/{formKey}")
    public Result<Map<String, Object>> getFormStatistics(@PathVariable String formKey) {
        try {
            // 获取最新版本的表单配置
            FormConfig formConfig = formConfigService.getOne(
                new QueryWrapper<FormConfig>()
                    .eq("form_key", formKey)
                    .eq("is_latest", 1)
            );
            
            if (formConfig == null) {
                return Result.error("表单不存在");
            }
            
            // 获取表单数据列表
            List<JSONObject> dataList = formDataService.listFormData(formKey, formConfig.getFormVersion());
            
            // 计算统计数据
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("formKey", formKey);
            statistics.put("formVersion", formConfig.getFormVersion());
            statistics.put("totalDataCount", dataList != null ? dataList.size() : 0);
            
            // 计算今日新增数据量
            int todayCount = 0;
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayStr = today.format(formatter);
            
            if (dataList != null) {
                for (JSONObject data : dataList) {
                    Object createdTimeObj = data.get("createdTime");
                    if (createdTimeObj != null) {
                        String createdTimeStr = createdTimeObj.toString();
                        if (createdTimeStr.startsWith(todayStr)) {
                            todayCount++;
                        }
                    }
                }
            }
            
            statistics.put("todayDataCount", todayCount);
            
            return Result.success(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取统计数据失败");
        }
    }
    
    /**
     * 获取表单数据统计信息（所有版本），包括总数据量、今日新增数据量
     * @param formKey 表单键
     * @return 包含统计信息的Map，键包括formKey、totalDataCount、todayDataCount
     */
    @GetMapping("/overview-all/{formKey}")
    public Result<Map<String, Object>> getAllFormStatistics(@PathVariable String formKey) {
        try {
            // 获取表单数据列表（所有版本）
            List<JSONObject> dataList = formDataService.listFormData(formKey);
            
            // 计算统计数据
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("formKey", formKey);
            statistics.put("totalDataCount", dataList != null ? dataList.size() : 0);
            
            // 计算今日新增数据量
            int todayCount = 0;
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayStr = today.format(formatter);
            
            if (dataList != null) {
                for (JSONObject data : dataList) {
                    Object createdTimeObj = data.get("createdTime");
                    if (createdTimeObj != null) {
                        String createdTimeStr = createdTimeObj.toString();
                        if (createdTimeStr.startsWith(todayStr)) {
                            todayCount++;
                        }
                    }
                }
            }
            
            statistics.put("todayDataCount", todayCount);
            
            return Result.success(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取统计数据失败");
        }
    }
    
    /**
     * 获取指定版本的表单数据统计信息，包括总数据量、今日新增数据量
     * @param formKey 表单键
     * @param version 表单版本号
     * @return 包含统计信息的Map，键包括formKey、formVersion、totalDataCount、todayDataCount
     */
    @GetMapping("/overview/{formKey}/version/{version}")
    public Result<Map<String, Object>> getFormStatisticsByVersion(@PathVariable String formKey, @PathVariable Integer version) {
        try {
            // 获取指定版本的表单配置
            FormConfig formConfig = formConfigService.getOne(
                new QueryWrapper<FormConfig>()
                    .eq("form_key", formKey)
                    .eq("form_version", version)
            );
            
            if (formConfig == null) {
                return Result.error("表单不存在");
            }
            
            // 获取表单数据列表
            List<JSONObject> dataList = formDataService.listFormData(formKey, version);
            
            // 计算统计数据
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("formKey", formKey);
            statistics.put("formVersion", version);
            statistics.put("totalDataCount", dataList != null ? dataList.size() : 0);
            
            // 计算今日新增数据量
            int todayCount = 0;
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayStr = today.format(formatter);
            
            if (dataList != null) {
                for (JSONObject data : dataList) {
                    Object createdTimeObj = data.get("createdTime");
                    if (createdTimeObj != null) {
                        String createdTimeStr = createdTimeObj.toString();
                        if (createdTimeStr.startsWith(todayStr)) {
                            todayCount++;
                        }
                    }
                }
            }
            
            statistics.put("todayDataCount", todayCount);
            
            return Result.success(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取统计数据失败");
        }
    }
    
    /**
     * 获取图表数据，包括饼图数据（指定字段）
     * @param formKey 表单键
     * @param chartType 图表类型，可选值为"pie"
     * @param fieldKey 字段键，仅当chartType为"pie"时必填
     * @return 包含图表数据的Map，键包括chartType、formKey、formVersion、fieldKey、data
     */
    @GetMapping("/chart/{formKey}")
    public Result<Map<String, Object>> getChartData(@PathVariable String formKey, 
                                                   @RequestParam String chartType,
                                                   @RequestParam(required = false) String fieldKey) {
        try {
            // 获取最新版本的表单配置
            FormConfig formConfig = formConfigService.getOne(
                new QueryWrapper<FormConfig>()
                    .eq("form_key", formKey)
                    .eq("is_latest", 1)
            );
            
            if (formConfig == null) {
                return Result.error("表单不存在");
            }
            
            // 获取表单数据列表
            List<JSONObject> dataList = formDataService.listFormData(formKey, formConfig.getFormVersion());
            
            Map<String, Object> chartData = new HashMap<>();
            chartData.put("chartType", chartType);
            chartData.put("formKey", formKey);
            chartData.put("formVersion", formConfig.getFormVersion());
            chartData.put("fieldKey", fieldKey);
            
            // 根据图表类型生成数据
            if ("pie".equals(chartType) && fieldKey != null && dataList != null) {
                // 饼图数据统计
                Map<String, Integer> pieData = new HashMap<>();
                
                for (JSONObject data : dataList) {
                    Object fieldValueObj = data.get(fieldKey);
                    if (fieldValueObj != null) {
                        String fieldValue = fieldValueObj.toString();
                        pieData.put(fieldValue, pieData.getOrDefault(fieldValue, 0) + 1);
                    }
                }
                
                chartData.put("data", pieData);
            } else if ("bar".equals(chartType) && fieldKey != null && dataList != null) {
                // 柱状图数据统计
                Map<String, Integer> barData = new HashMap<>();
                
                for (JSONObject data : dataList) {
                    Object fieldValueObj = data.get(fieldKey);
                    if (fieldValueObj != null) {
                        String fieldValue = fieldValueObj.toString();
                        barData.put(fieldValue, barData.getOrDefault(fieldValue, 0) + 1);
                    }
                }
                
                chartData.put("data", barData);
            } else if ("line".equals(chartType) && dataList != null) {
                // 折线图数据统计（按日期统计）
                Map<String, Integer> lineData = new HashMap<>();
                
                for (JSONObject data : dataList) {
                    Object createdTimeObj = data.get("createdTime");
                    if (createdTimeObj != null) {
                        String createdTimeStr = createdTimeObj.toString();
                        // 提取日期部分
                        if (createdTimeStr.length() >= 10) {
                            String dateStr = createdTimeStr.substring(0, 10);
                            lineData.put(dateStr, lineData.getOrDefault(dateStr, 0) + 1);
                        }
                    }
                }
                
                chartData.put("data", lineData);
            }
            
            return Result.success(chartData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取图表数据失败");
        }
    }
    
    /**
     * 获取图表数据（所有版本），包括饼图数据（指定字段）
     * @param formKey 表单键
     * @param chartType 图表类型，可选值为"pie"
     * @param fieldKey 字段键，仅当chartType为"pie"时必填
     * @return 包含图表数据的Map，键包括chartType、formKey、fieldKey、data
     */
    @GetMapping("/chart-all/{formKey}")
    public Result<Map<String, Object>> getAllChartData(@PathVariable String formKey, 
                                                   @RequestParam String chartType,
                                                   @RequestParam(required = false) String fieldKey) {
        try {
            // 获取表单数据列表（所有版本）
            List<JSONObject> dataList = formDataService.listFormData(formKey);
            
            Map<String, Object> chartData = new HashMap<>();
            chartData.put("chartType", chartType);
            chartData.put("formKey", formKey);
            chartData.put("fieldKey", fieldKey);
            
            // 根据图表类型生成数据
            if ("pie".equals(chartType) && fieldKey != null && dataList != null) {
                // 饼图数据统计
                Map<String, Integer> pieData = new HashMap<>();
                
                for (JSONObject data : dataList) {
                    Object fieldValueObj = data.get(fieldKey);
                    if (fieldValueObj != null) {
                        String fieldValue = fieldValueObj.toString();
                        pieData.put(fieldValue, pieData.getOrDefault(fieldValue, 0) + 1);
                    }
                }
                
                chartData.put("data", pieData);
            } else if ("bar".equals(chartType) && fieldKey != null && dataList != null) {
                // 柱状图数据统计
                Map<String, Integer> barData = new HashMap<>();
                
                for (JSONObject data : dataList) {
                    Object fieldValueObj = data.get(fieldKey);
                    if (fieldValueObj != null) {
                        String fieldValue = fieldValueObj.toString();
                        barData.put(fieldValue, barData.getOrDefault(fieldValue, 0) + 1);
                    }
                }
                
                chartData.put("data", barData);
            } else if ("line".equals(chartType) && dataList != null) {
                // 折线图数据统计（按日期统计）
                Map<String, Integer> lineData = new HashMap<>();
                
                for (JSONObject data : dataList) {
                    Object createdTimeObj = data.get("createdTime");
                    if (createdTimeObj != null) {
                        String createdTimeStr = createdTimeObj.toString();
                        // 提取日期部分
                        if (createdTimeStr.length() >= 10) {
                            String dateStr = createdTimeStr.substring(0, 10);
                            lineData.put(dateStr, lineData.getOrDefault(dateStr, 0) + 1);
                        }
                    }
                }
                
                chartData.put("data", lineData);
            }
            
            return Result.success(chartData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取图表数据失败");
        }
    }
    
    /**
     * 获取指定版本的图表数据，包括饼图数据（指定字段）
     * @param formKey 表单键
     * @param version 表单版本号
     * @param chartType 图表类型，可选值为"pie"
     * @param fieldKey 字段键，仅当chartType为"pie"时必填
     * @return 包含图表数据的Map，键包括chartType、formKey、formVersion、fieldKey、data
     */
    @GetMapping("/chart/{formKey}/version/{version}")
    public Result<Map<String, Object>> getChartDataByVersion(@PathVariable String formKey, 
                                                           @PathVariable Integer version,
                                                           @RequestParam String chartType,
                                                           @RequestParam(required = false) String fieldKey) {
        try {
            // 获取表单数据列表
            List<JSONObject> dataList = formDataService.listFormData(formKey, version);
            
            Map<String, Object> chartData = new HashMap<>();
            chartData.put("chartType", chartType);
            chartData.put("formKey", formKey);
            chartData.put("formVersion", version);
            chartData.put("fieldKey", fieldKey);
            
            // 根据图表类型生成数据
            if ("pie".equals(chartType) && fieldKey != null && dataList != null) {
                // 饼图数据统计
                Map<String, Integer> pieData = new HashMap<>();
                
                for (JSONObject data : dataList) {
                    Object fieldValueObj = data.get(fieldKey);
                    if (fieldValueObj != null) {
                        String fieldValue = fieldValueObj.toString();
                        pieData.put(fieldValue, pieData.getOrDefault(fieldValue, 0) + 1);
                    }
                }
                
                chartData.put("data", pieData);
            } else if ("bar".equals(chartType) && fieldKey != null && dataList != null) {
                // 柱状图数据统计
                Map<String, Integer> barData = new HashMap<>();
                
                for (JSONObject data : dataList) {
                    Object fieldValueObj = data.get(fieldKey);
                    if (fieldValueObj != null) {
                        String fieldValue = fieldValueObj.toString();
                        barData.put(fieldValue, barData.getOrDefault(fieldValue, 0) + 1);
                    }
                }
                
                chartData.put("data", barData);
            } else if ("line".equals(chartType) && dataList != null) {
                // 折线图数据统计（按日期统计）
                Map<String, Integer> lineData = new HashMap<>();
                
                for (JSONObject data : dataList) {
                    Object createdTimeObj = data.get("createdTime");
                    if (createdTimeObj != null) {
                        String createdTimeStr = createdTimeObj.toString();
                        // 提取日期部分
                        if (createdTimeStr.length() >= 10) {
                            String dateStr = createdTimeStr.substring(0, 10);
                            lineData.put(dateStr, lineData.getOrDefault(dateStr, 0) + 1);
                        }
                    }
                }
                
                chartData.put("data", lineData);
            }
            
            return Result.success(chartData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取图表数据失败");
        }
    }
}
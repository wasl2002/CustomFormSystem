<template>
  <a-layout>
    <a-layout-content>
      <a-card :title="formConfig ? `数据统计 - ${formConfig.formName}` : '数据统计'">
        <a-row :gutter="16">
          <!-- 左侧配置面板 -->
          <a-col :span="6">
            <a-card title="统计配置" size="small">
              <!-- 版本选择 -->
              <a-form-item label="版本选择">
                <a-select 
                  v-model:value="selectedVersion" 
                  style="width: 100%"
                  @change="onVersionChange"
                >
                  <a-select-option value="">全部版本</a-select-option>
                  <a-select-option 
                    v-for="version in formVersions" 
                    :key="version.formVersion" 
                    :value="version.formVersion"
                  >
                    v{{ version.formVersion }} <a-tag v-if="version.isLatest === 1" color="green">最新</a-tag>
                  </a-select-option>
                </a-select>
              </a-form-item>
              
              <!-- 图表类型选择 -->
              <a-form-item label="图表类型">
                <a-select v-model:value="chartForm.chartType" style="width: 100%">
                  <a-select-option value="pie">饼图</a-select-option>
                  <a-select-option value="bar">柱状图</a-select-option>
                  <a-select-option value="line">折线图</a-select-option>
                </a-select>
              </a-form-item>
              
              <!-- 字段选择 -->
              <a-form-item label="统计字段">
                <a-select 
                  v-model:value="chartForm.fieldKey" 
                  style="width: 100%"
                  placeholder="请选择字段"
                >
                  <a-select-option 
                    v-for="field in formFields" 
                    :key="field.fieldKey" 
                    :value="field.fieldKey"
                  >
                    {{ field.fieldName }}
                  </a-select-option>
                </a-select>
              </a-form-item>
              
              <a-form-item>
                <a-button type="primary" @click="loadChartData" style="width: 100%">生成图表</a-button>
              </a-form-item>
              
              <!-- 统计信息 -->
              <a-divider orientation="left">数据统计</a-divider>
              <a-descriptions :column="1" size="small">
                <a-descriptions-item label="总数据量">
                  {{ statistics.totalDataCount || 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="今日新增">
                  {{ statistics.todayDataCount || 0 }}
                </a-descriptions-item>
              </a-descriptions>
            </a-card>
          </a-col>
          
          <!-- 右侧图表展示区域 -->
          <a-col :span="18">
            <a-card title="数据图表" size="small">
              <div ref="chartContainer" style="width: 100%; height: 500px;"></div>
            </a-card>
          </a-col>
        </a-row>
      </a-card>
    </a-layout-content>
  </a-layout>
</template>

<script>
import { getFormConfigByFormKey, getFormConfigVersions } from '../api/formConfig';
import { getFieldsByFormKey, getFieldsByFormKeyAndVersion } from '../api/formField';
import { getFormStatistics, getChartData, getFormStatisticsByVersion, getChartDataByVersion, getAllFormStatistics, getAllChartData } from '../api/formStatistics';
import * as echarts from 'echarts';
import { message } from 'ant-design-vue';
import { formatFieldValue } from '../utils/fieldFormatter.js';

export default {
  name: 'FormStatistics',
  props: {
    formKey: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      formConfig: null,
      formFields: [],
      formVersions: [],
      selectedVersion: null, // 默认不选择特定版本
      statistics: {
        totalDataCount: 0,
        todayDataCount: 0
      },
      chartForm: {
        chartType: 'pie',
        fieldKey: null
      },
      chartInstance: null
    };
  },
  mounted() {
    this.loadFormConfig();
    this.loadFormVersions();
    this.loadStatistics();
    
    // 初始化图表
    this.$nextTick(() => {
      this.initChart();
    });
  },
  beforeUnmount() {
    if (this.chartInstance) {
      this.chartInstance.dispose();
    }
  },
  methods: {
    async loadFormConfig() {
      try {
        // 获取表单配置
        const formResponse = await getFormConfigByFormKey(this.formKey);
        this.formConfig = formResponse.data.data;
        
        // 获取字段配置（默认获取最新版本）
        await this.loadFormFields();
      } catch (error) {
        console.error('加载表单配置失败:', error);
        message.error('加载表单配置失败');
      }
    },
    async loadFormFields() {
      try {
        let fieldResponse;
        if (this.selectedVersion) {
          // 获取指定版本的字段配置
          fieldResponse = await getFieldsByFormKeyAndVersion(this.formKey, this.selectedVersion);
        } else {
          // 获取最新版本的字段配置
          fieldResponse = await getFieldsByFormKey(this.formKey);
        }
        this.formFields = fieldResponse.data.data || [];
        
        // 默认选择第一个字段
        if (this.formFields.length > 0) {
          this.chartForm.fieldKey = this.formFields[0].fieldKey;
        }
      } catch (error) {
        console.error('加载字段配置失败:', error);
        message.error('加载字段配置失败');
      }
    },
    async loadFormVersions() {
      try {
        // 获取表单所有版本
        const response = await getFormConfigVersions(this.formKey);
        if (response.data.data) {
          this.formVersions = response.data.data;
          
          // 默认不选择特定版本（显示全部版本数据）
          this.selectedVersion = null;
        } else {
          message.error('获取表单版本失败');
        }
      } catch (error) {
        console.error('加载表单版本失败:', error);
        message.error('加载表单版本失败');
      }
    },
    async loadStatistics() {
      try {
        let response;
        if (this.selectedVersion) {
          // 获取指定版本的统计数据
          response = await getFormStatisticsByVersion(this.formKey, this.selectedVersion);
        } else {
          // 获取所有版本的统计数据
          response = await getAllFormStatistics(this.formKey);
        }
        this.statistics = response.data.data || {};
      } catch (error) {
        console.error('加载统计信息失败:', error);
        message.error('加载统计信息失败');
      }
    },
    getFormStatusText() {
      if (!this.formConfig) return '未知';
      
      const statusMap = {
        1: '启用',
        2: '停用',
        3: '已删除'
      };
      
      return statusMap[this.formConfig.status] || '未知';
    },
    // 初始化图表
    initChart() {
      if (this.$refs.chartContainer) {
        this.chartInstance = echarts.init(this.$refs.chartContainer);
        // 显示默认提示信息
        this.chartInstance.setOption({
          title: {
            text: '请选择字段并点击生成图表',
            left: 'center',
            top: 'center'
          }
        });
      }
    },
    // 加载图表数据
    async loadChartData() {
      if (!this.chartForm.fieldKey) {
        message.warning('请选择统计字段');
        return;
      }
      
      try {
        let response;
        if (this.selectedVersion) {
          // 获取指定版本的图表数据
          response = await getChartDataByVersion(this.formKey, this.selectedVersion, this.chartForm.chartType, this.chartForm.fieldKey);
        } else {
          // 获取所有版本的图表数据
          response = await getAllChartData(this.formKey, this.chartForm.chartType, this.chartForm.fieldKey);
        }
        
        // 从响应中提取实际的图表数据
        let chartData = [];
        if (response.data.data && response.data.data.data) {
          // 将 Map 转换为数组格式
          const rawData = response.data.data.data;
          if (rawData && typeof rawData === 'object' && !Array.isArray(rawData)) {
            chartData = Object.keys(rawData).map(key => ({
              label: key,
              count: rawData[key]
            }));
          } else if (Array.isArray(rawData)) {
            chartData = rawData;
          }
        }
        
        this.renderChart(chartData);
      } catch (error) {
        console.error('加载图表数据失败:', error);
        message.error('加载图表数据失败');
      }
    },
    
    // 渲染图表
    renderChart(chartData) {
      // 确保 chartData 是数组
      if (!Array.isArray(chartData)) {
        chartData = [];
      }
      
      if (!this.chartInstance || chartData.length === 0) {
        // 显示无数据提示
        this.chartInstance.setOption({
          title: {
            text: '暂无数据',
            left: 'center',
            top: 'center'
          }
        }, true);
        return;
      }
      
      // 准备图表数据
      const xAxisData = chartData.map(item => item.label || '未知');
      const seriesData = chartData.map(item => ({
        value: item.count,
        name: item.label || '未知'
      }));
      
      // 根据图表类型渲染
      let option = {};
      
      switch (this.chartForm.chartType) {
        case 'pie':
          option = {
            title: {
              text: '数据分布统计',
              left: 'center'
            },
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left'
            },
            series: [
              {
                name: '数据统计',
                type: 'pie',
                radius: '50%',
                data: seriesData,
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          };
          break;
          
        case 'bar':
          option = {
            title: {
              text: '数据分布统计',
              left: 'center'
            },
            tooltip: {
              trigger: 'axis'
            },
            xAxis: {
              type: 'category',
              data: xAxisData
            },
            yAxis: {
              type: 'value'
            },
            series: [
              {
                name: '数量',
                type: 'bar',
                data: seriesData.map(item => item.value),
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          };
          break;
          
        case 'line':
          option = {
            title: {
              text: '数据分布统计',
              left: 'center'
            },
            tooltip: {
              trigger: 'axis'
            },
            xAxis: {
              type: 'category',
              data: xAxisData
            },
            yAxis: {
              type: 'value'
            },
            series: [
              {
                name: '数量',
                type: 'line',
                data: seriesData.map(item => item.value),
                smooth: true,
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          };
          break;
          
        default:
          option = {
            title: {
              text: '请选择有效的图表类型',
              left: 'center',
              top: 'center'
            }
          };
      }
      
      this.chartInstance.setOption(option, true);
    },
    // 版本改变时的处理
    async onVersionChange() {
      await this.loadFormFields();
      this.loadStatistics();
      // 清空图表
      if (this.chartInstance) {
        this.chartInstance.clear();
        this.chartInstance.setOption({
          title: {
            text: '请选择字段并点击生成图表',
            left: 'center',
            top: 'center'
          }
        });
      }
    },
    
    // 添加字段格式化方法
    formatFieldValue
  }
};
</script>
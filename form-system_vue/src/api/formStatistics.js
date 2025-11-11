import request from '../utils/request';

// 获取表单统计数据（最新版本）
export function getFormStatistics(formKey) {
  return request({
    url: `/statistics/overview/${formKey}`,
    method: 'get'
  });
}

// 获取表单统计数据（指定版本）
export function getFormStatisticsByVersion(formKey, version) {
  return request({
    url: `/statistics/overview/${formKey}/version/${version}`,
    method: 'get'
  });
}

// 获取表单统计数据（所有版本）
export function getAllFormStatistics(formKey) {
  return request({
    url: `/statistics/overview-all/${formKey}`,
    method: 'get'
  });
}

// 获取图表数据（最新版本）
export function getChartData(formKey, chartType, fieldKey) {
  return request({
    url: `/statistics/chart/${formKey}`,
    method: 'get',
    params: {
      chartType,
      fieldKey: fieldKey || undefined
    }
  });
}

// 获取图表数据（指定版本）
export function getChartDataByVersion(formKey, version, chartType, fieldKey) {
  return request({
    url: `/statistics/chart/${formKey}/version/${version}`,
    method: 'get',
    params: {
      chartType,
      fieldKey: fieldKey || undefined
    }
  });
}

// 获取图表数据（所有版本）
export function getAllChartData(formKey, chartType, fieldKey) {
  return request({
    url: `/statistics/chart-all/${formKey}`,
    method: 'get',
    params: {
      chartType,
      fieldKey: fieldKey || undefined
    }
  });
}
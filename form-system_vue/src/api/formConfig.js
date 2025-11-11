import request from '../utils/request';

// 获取所有表单配置
export function getAllFormConfigs() {
  return request({
    url: '/config/list',
    method: 'get'
  });
}

// 根据ID获取表单配置
export function getFormConfigById(id) {
  return request({
    url: `/config/id/${id}`,
    method: 'get'
  });
}

// 根据formKey获取表单配置
export function getFormConfigByFormKey(formKey) {
  return request({
    url: `/config/${formKey}`,
    method: 'get'
  });
}

// 根据formKey获取所有版本的表单配置
export function getFormConfigVersions(formKey) {
  return request({
    url: `/config/versions/${formKey}`,
    method: 'get'
  });
}

// 创建表单配置
export function createFormConfig(data) {
  return request({
    url: '/config/create',
    method: 'post',
    data
  });
}

// 更新表单配置（创建新版本）
export function updateFormConfig(formKey, data) {
  return request({
    url: `/config/update/${formKey}`,
    method: 'post',
    data
  });
}

// 删除表单配置
export function deleteFormConfig(id) {
  return request({
    url: `/config/delete/${id}`,
    method: 'delete'
  });
}

// 发布表单
export function publishForm(id) {
  return request({
    url: `/config/publish/${id}`,
    method: 'put'
  });
}

// 停用表单
export function disableForm(id) {
  return request({
    url: `/config/disable/${id}`,
    method: 'put'
  });
}

// 启用表单
export function enableForm(id) {
  return request({
    url: `/config/enable/${id}`,
    method: 'put'
  });
}

// 回滚到指定版本
export function rollbackToVersion(formKey, version) {
  return request({
    url: `/config/rollback/${formKey}/${version}`,
    method: 'put'
  });
}

// 根据ID获取表单配置（包括历史版本）
export function getFormConfigByVersionId(id) {
  return request({
    url: `/config/version/id/${id}`,
    method: 'get'
  });
}

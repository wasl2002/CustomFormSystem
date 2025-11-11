import request from '../utils/request';

// 根据表单ID获取所有字段配置
export function getFieldsByFormId(formId) {
  return request({
    url: `/field/list/${formId}`,
    method: 'get'
  });
}

// 根据表单Key获取所有字段配置
export function getFieldsByFormKey(formKey) {
  return request({
    url: `/field/list/key/${formKey}`,
    method: 'get'
  });
}

// 根据表单Key和版本号获取字段配置
export function getFieldsByFormKeyAndVersion(formKey, version) {
  return request({
    url: `/field/list/key/${formKey}/version/${version}`,
    method: 'get'
  });
}

// 创建字段配置
export function createField(data) {
  return request({
    url: '/field/create',
    method: 'post',
    data
  });
}

// 批量创建字段配置
export function batchCreateFields(data) {
  return request({
    url: '/field/batch-create',
    method: 'post',
    data
  });
}

// 更新字段配置
export function updateField(data) {
  return request({
    url: '/field/update',
    method: 'put',
    data
  });
}

// 删除字段配置
export function deleteField(id) {
  return request({
    url: `/field/delete/${id}`,
    method: 'delete'
  });
}

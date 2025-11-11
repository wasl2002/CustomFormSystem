import request from '../utils/request';

// 保存表单数据
export function saveFormData(formKey, formData) {
  return request({
    url: `/data/save/${formKey}`,
    method: 'post',
    data: formData
  });
}

// 更新表单数据
export function updateFormData(formKey, dataId, formData) {
  return request({
    url: `/data/update/${formKey}/${dataId}`,
    method: 'put',
    data: formData
  });
}

// 删除表单数据
export function deleteFormData(formKey, dataId) {
  return request({
    url: `/data/delete/${formKey}/${dataId}`,
    method: 'delete'
  });
}


// 多条件搜索导出Excel
export function exportFormDataWithMultipleConditions(formKey, version, searchConditions) {
  // 将搜索条件转换为URL参数
  const conditionsParam = encodeURIComponent(JSON.stringify(searchConditions));

  // 构建URL
  let url = `/data/export/${formKey}?searchConditions=${conditionsParam}`;
  if (version) {
    url += `&version=${version}`;
  }

  // 创建隐藏的iframe下载文件
  const iframe = document.createElement('iframe');
  iframe.style.display = 'none';
  iframe.src = url;
  document.body.appendChild(iframe);

  // 下载完成后移除iframe
  setTimeout(() => {
    document.body.removeChild(iframe);
  }, 1000);
}

// 分页查询指定版本的表单数据列表（支持搜索和分页）
export function pageFormDataByVersion(formKey, version, searchField, searchValue, pageNum = 1, pageSize = 10) {
  return request({
    url: `/data/page/${formKey}/version/${version}`,
    method: 'get',
    params: {
      searchField,
      searchValue,
      pageNum,
      pageSize
    }
  });
}

// 分页多条件查询表单数据
export function pageFormDataWithMultipleConditions(formKey, version, searchConditions, pageNum = 1, pageSize = 10) {
  return request({
    url: `/data/page/search/${formKey}`,
    method: 'post',
    params: {
      version,
      pageNum,
      pageSize
    },
    data: searchConditions
  });
}

/**
 * 复选框字段格式化工具
 */

import { parseFieldOptions } from './commonUtils.js';

/**
 * 格式化复选框显示
 */
export function formatCheckboxValue(value, field) {
  if (!value || !field) return '';
  const options = parseFieldOptions(field.fieldOptions);

  // 处理不同类型的值
  let selectedOptions = [];
  if (Array.isArray(value)) {
    selectedOptions = value;
  } else if (typeof value === 'string') {
    // 如果是逗号分隔的字符串，分割成数组
    selectedOptions = value.split(',').map(v => v.trim()).filter(v => v);
  } else {
    selectedOptions = [value];
  }

  const labels = selectedOptions.map(val => {
    const option = options.find(opt => opt.value == val); // 使用 == 而不是 === 以支持类型转换
    return option ? option.label : val;
  });
  return labels.join(', ');
}
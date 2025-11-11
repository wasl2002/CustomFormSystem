/**
 * 下拉框/单选框字段格式化工具
 */

import { parseFieldOptions } from './commonUtils.js';

/**
 * 格式化下拉框/单选框显示
 */
export function formatSelectValue(value, field) {
  if (!value || !field) return value;
  const options = parseFieldOptions(field.fieldOptions);
  const option = options.find(opt => opt.value == value); // 使用 == 而不是 === 以支持类型转换
  return option ? option.label : value;
}
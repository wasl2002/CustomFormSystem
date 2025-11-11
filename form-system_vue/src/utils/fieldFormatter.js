/**
 * 字段显示格式化工具类入口
 * 用于统一处理各种字段类型的显示格式
 */

import { formatDateValue } from './formatters/dateFormatter.js';
import { formatCheckboxValue } from './formatters/checkboxFormatter.js';
import { formatSelectValue } from './formatters/selectFormatter.js';
import { formatRegionValue } from './formatters/regionFormatter.js';
import { formatGenericValue } from './formatters/commonUtils.js';
import { parseFieldProperties } from './formatters/dateFormatter.js';
import { parseFieldOptions } from './formatters/commonUtils.js';

/**
 * 格式化字段显示值
 * @param {any} value - 字段值
 * @param {Object} field - 字段配置对象
 * @returns {string} 格式化后的显示值
 */
export function formatFieldValue(value, field) {
  if (!field || value === null || value === undefined) {
    return formatGenericValue(value);
  }

  // 根据字段类型调用相应的格式化方法
  switch (field.fieldType) {
    case 'date':
    case 'date-field':
      return formatDateValue(value, field);
    
    case 'checkbox':
    case 'checkbox-field':
      return formatCheckboxValue(value, field);
    
    case 'select':
    case 'select-field':
    case 'radio':
    case 'radio-field':
      return formatSelectValue(value, field);
    
    case 'region-field':
      return formatRegionValue(value, field);
    
    default:
      return formatGenericValue(value);
  }
}

// 导出所有单独的格式化方法，以供需要直接使用特定格式化方法的场景
export { formatDateValue } from './formatters/dateFormatter.js';
export { formatCheckboxValue } from './formatters/checkboxFormatter.js';
export { formatSelectValue } from './formatters/selectFormatter.js';
export { formatRegionValue } from './formatters/regionFormatter.js';
export { formatGenericValue } from './formatters/commonUtils.js';
export { parseFieldProperties } from './formatters/dateFormatter.js';
export { parseFieldOptions } from './formatters/commonUtils.js';

// 默认导出所有方法
export default {
  formatFieldValue,
  formatDateValue,
  formatCheckboxValue,
  formatSelectValue,
  formatRegionValue,
  formatGenericValue,
  parseFieldProperties,
  parseFieldOptions
};
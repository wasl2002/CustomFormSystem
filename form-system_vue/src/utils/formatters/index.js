/**
 * 字段格式化器索引文件
 * 提供统一的导入入口
 */

import { formatFieldValue } from '../fieldFormatter.js';
import { formatDateValue } from './dateFormatter.js';
import { formatCheckboxValue } from './checkboxFormatter.js';
import { formatSelectValue } from './selectFormatter.js';
import { formatRegionValue } from './regionFormatter.js';
import { formatGenericValue } from './commonUtils.js';
import { parseFieldProperties } from './dateFormatter.js';
import { parseFieldOptions } from './commonUtils.js';

// 默认导出所有格式化方法
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

// 同时保留命名导出以保持向后兼容
export { formatFieldValue } from '../fieldFormatter.js';
export { formatDateValue } from './dateFormatter.js';
export { formatCheckboxValue } from './checkboxFormatter.js';
export { formatSelectValue } from './selectFormatter.js';
export { formatRegionValue } from './regionFormatter.js';
export { formatGenericValue } from './commonUtils.js';
export { parseFieldProperties } from './dateFormatter.js';
export { parseFieldOptions } from './commonUtils.js';
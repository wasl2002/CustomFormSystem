/**
 * 通用工具函数
 */

/**
 * 通用值格式化
 */
export function formatGenericValue(value) {
  // 处理通用值的显示
  if (value === null || value === undefined) {
    return '';
  }
  if (Array.isArray(value)) {
    return value.join(', ');
  }
  return String(value);
}

/**
 * 解析字段选项
 */
export function parseFieldOptions(optionsStr) {
  if (!optionsStr) return [];

  try {
    const options = [];
    const lines = optionsStr.split('\n');

    lines.forEach(line => {
      if (line.trim()) {
        const [value, label] = line.split('=');
        if (value && label) {
          options.push({
            value: value.trim(),
            label: label.trim()
          });
        }
      }
    });

    return options;
  } catch (error) {
    console.error('解析字段选项失败:', error);
    return [];
  }
}
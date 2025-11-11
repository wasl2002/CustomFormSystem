/**
 * 日期字段格式化工具
 */
import dayjs from 'dayjs';

/**
 * 格式化日期显示
 */
export function formatDateValue(dateString, field) {
  if (!dateString) return '';
  try {
    // 获取字段属性中的日期格式配置
    const fieldProperties = parseFieldProperties(field);
    const displayFormat = fieldProperties.dateFormat || 'YYYY-MM-DD';
    const dateType = fieldProperties.dateType || 'date';

    // 处理不同格式的日期字符串
    let date;

    // 如果是 dayjs 对象
    if (dateString && dateString.$d) {
      date = dayjs(dateString.$d);
    }
    // 如果已经是Date对象
    else if (dateString instanceof Date) {
      date = dayjs(dateString);
    }
    // 如果是ISO格式的字符串 (2023-01-01T00:00:00.000Z)
    else if (typeof dateString === 'string' && dateString.includes('T')) {
      date = dayjs(dateString);
    }
    // 如果是日期字符串 (2023-01-01)
    else if (typeof dateString === 'string' && /^\d{4}-\d{2}-\d{2}/.test(dateString)) {
      date = dayjs(dateString);
    }
    // 如果是时间戳
    else if (typeof dateString === 'number') {
      date = dayjs(dateString);
    }
    // 其他情况尝试直接创建dayjs对象
    else {
      date = dayjs(dateString);
    }

    // 检查日期是否有效
    if (!date.isValid()) {
      return String(dateString);
    }

    // 根据日期类型和格式进行格式化
    switch (dateType) {
      case 'datetime':
        // 日期时间格式
        return date.format('YYYY-MM-DD HH:mm:ss');

      case 'week':
        // 周格式
        return date.format('YYYY-WW');

      case 'month':
        // 月格式
        return date.format('YYYY-MM');

      case 'quarter':
        // 季度格式
        return date.format('YYYY-Q');

      case 'year':
        // 年格式
        return date.format('YYYY');

      default:
        // 使用字段配置的格式或默认格式
        return date.format(displayFormat);
    }
  } catch (error) {
    console.error('日期格式化失败:', error);
    return String(dateString);
  }
}

/**
 * 解析字段属性
 */
export function parseFieldProperties(field) {
  if (!field.fieldProperties) {
    return {};
  }

  try {
    return JSON.parse(field.fieldProperties);
  } catch (error) {
    console.error('解析字段属性失败:', error);
    return {};
  }
}
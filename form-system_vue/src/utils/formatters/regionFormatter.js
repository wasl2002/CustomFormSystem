/**
 * 省市区选择组件格式化工具
 */

/**
 * 格式化省市区选择组件显示
 */
export function formatRegionValue(value, field) {
  if (!value) return '';
  
  // 省市区组件的值是一个数组，每个元素代表省、市、区的选项对象
  if (Array.isArray(value)) {
    // 过滤掉空值并提取标签
    return value
      .filter(item => item && (item.label || item.value || item))
      .map(item => {
        // 如果是对象，优先使用label，其次是value，最后是对象本身
        if (typeof item === 'object') {
          return item.label || item.value || JSON.stringify(item);
        }
        // 如果是字符串或数字，直接返回
        return item;
      })
      .join('/');
  }
  // 如果是对象格式（可能是保存到数据库后的简化格式）
  else if (typeof value === 'object') {
    // 如果是完整的省市区对象
    if (value.province && value.city && value.area) {
      const provinceLabel = typeof value.province === 'object' ? (value.province.label || value.province.value || JSON.stringify(value.province)) : value.province;
      const cityLabel = typeof value.city === 'object' ? (value.city.label || value.city.value || JSON.stringify(value.city)) : value.city;
      const areaLabel = typeof value.area === 'object' ? (value.area.label || value.area.value || JSON.stringify(value.area)) : value.area;
      return `${provinceLabel}/${cityLabel}/${areaLabel}`;
    } 
    // 如果只有省市
    else if (value.province && value.city) {
      const provinceLabel = typeof value.province === 'object' ? (value.province.label || value.province.value || JSON.stringify(value.province)) : value.province;
      const cityLabel = typeof value.city === 'object' ? (value.city.label || value.city.value || JSON.stringify(value.city)) : value.city;
      return `${provinceLabel}/${cityLabel}`;
    }
    // 如果只有省
    else if (value.province) {
      const provinceLabel = typeof value.province === 'object' ? (value.province.label || value.province.value || JSON.stringify(value.province)) : value.province;
      return provinceLabel;
    }
    // 其他对象格式，尝试转换为字符串
    else {
      return Object.values(value).filter(v => v).join('/');
    }
  }
  // 如果是字符串格式（可能是逗号分隔的省市区）
  else if (typeof value === 'string') {
    // 如果包含逗号，按逗号分割
    if (value.includes(',')) {
      return value.split(',').map(v => v.trim()).filter(v => v).join('/');
    }
    // 如果包含斜杠，按斜杠分割
    else if (value.includes('/')) {
      return value.split('/').map(v => v.trim()).filter(v => v).join('/');
    }
    // 其他情况直接返回
    else {
      return value;
    }
  }
  // 其他情况转换为字符串
  else {
    return String(value);
  }
}
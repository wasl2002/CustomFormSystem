<template>
  <a-date-picker
    v-model:value="localValue"
    v-bind="dateProps"
    :placeholder="field.fieldName"
    style="width: 100%;"
  />
</template>

<script>
import dayjs from 'dayjs';
export default {
  name: 'DateField',
  props: {
    field: {
      type: Object,
      required: true
    },
    value: {
      type: [String, Object],
      default: null
    }
  },
  emits: ['update:value'],
  computed: {
    localValue: {
      get() {
        // 处理日期值，确保正确格式
        if (!this.value) return undefined;

        // 如果已经是 dayjs 对象
        if (this.value && this.value.$d) {
          return this.value;
        }

        // 如果是字符串，转换为 dayjs 对象
        if (typeof this.value === 'string') {
          return dayjs(this.value);
        }

        // 如果是 Date 对象，转换为 dayjs 对象
        if (this.value instanceof Date) {
          return dayjs(this.value);
        }
        return this.value;
      },
      set(val) {
        // 发送原始值而不是 dayjs 对象
        let emitValue = val;
        if (val && val.$d) {
          emitValue = val.$d.toISOString();
        } else if (val instanceof Date) {
          emitValue = val.toISOString();
        }
        this.$emit('update:value', val);
      }
    },
    dateProps() {
      const props = {};

      // 从字段属性中获取组件属性
      if (this.field.fieldProperties) {
        try {
          const fieldProperties = JSON.parse(this.field.fieldProperties);
          Object.assign(props, fieldProperties);

          // 根据日期类型设置特定属性
          const dateType = fieldProperties.dateType || 'date';
          const valueFormat = fieldProperties.valueFormat || 'YYYY-MM-DD';

          // 根据日期类型设置组件属性
          switch (dateType) {
            case 'datetime':
              props.showTime = true;
              props.format = fieldProperties.dateFormat || 'YYYY-MM-DD HH:mm:ss';
              props.valueFormat = valueFormat;
              break;
            case 'week':
              props.picker = 'week';
              props.format = fieldProperties.dateFormat || 'YYYY-WW';
              props.valueFormat = valueFormat;
              break;
            case 'month':
              props.picker = 'month';
              props.format = fieldProperties.dateFormat || 'YYYY-MM';
              props.valueFormat = valueFormat;
              break;
            case 'quarter':
              props.picker = 'quarter';
              props.format = fieldProperties.dateFormat || 'YYYY-Q';
              props.valueFormat = valueFormat;
              break;
            case 'year':
              props.picker = 'year';
              props.format = fieldProperties.dateFormat || 'YYYY';
              props.valueFormat = valueFormat;
              break;
            default:
              props.format = fieldProperties.dateFormat || 'YYYY-MM-DD';
              props.valueFormat = valueFormat;
          }
        } catch (e) {
          console.error('解析字段属性失败:', e);
        }
      }

      return props;
    }
  }
};
</script>

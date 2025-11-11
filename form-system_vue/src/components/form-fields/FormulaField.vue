<template>
  <a-input-number
    v-model:value="localValue"
    v-bind="numberProps"
    :placeholder="field.fieldName"
    :disabled="true"
  />
</template>

<script>
export default {
  name: 'FormulaField',
  props: {
    field: {
      type: Object,
      required: true
    },
    value: {
      type: Number,
      default: 0
    }
  },
  emits: ['update:value'],
  computed: {
    localValue: {
      get() {
        return this.value;
      },
      set(val) {
        this.$emit('update:value', val);
      }
    },
    numberProps() {
      const props = {};
      
      // 从字段属性中获取数字输入框配置
      if (this.field.fieldProperties) {
        try {
          const fieldProperties = JSON.parse(this.field.fieldProperties);
          if (fieldProperties.precision !== undefined) {
            props.precision = fieldProperties.precision;
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
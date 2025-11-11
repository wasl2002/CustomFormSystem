<template>
  <a-textarea 
    v-model:value="localValue"
    v-bind="fieldProps"
    :placeholder="field.fieldName"
  />
</template>

<script>
export default {
  name: 'TextareaField',
  props: {
    field: {
      type: Object,
      required: true
    },
    value: {
      type: [String, Number],
      default: ''
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
    fieldProps() {
      const props = {};
      if (this.field.fieldProperties) {
        try {
          Object.assign(props, JSON.parse(this.field.fieldProperties));
        } catch (e) {
          console.error('解析字段属性失败:', e);
        }
      }
      return props;
    }
  }
};
</script>
<template>
  <component
    :is="fieldComponent"
    v-bind="fieldProps"
    @update:value="handleValueUpdate"
  />
</template>

<script>
import { formFieldComponents, formFieldComponentNames } from './form-fields';

export default {
  name: 'FormField',
  components: {
    ...formFieldComponents
  },
  props: {
    field: {
      type: Object,
      required: true
    },
    value: {
      type: [String, Number, Array, Object],
      default: undefined
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
    fieldComponent() {

      // 使用新的组件名称映射，同时兼容旧的字段类型名称
      let fieldType = this.field.fieldType;

      // 如果是旧的字段类型名称，转换为新的名称
      if (fieldType === 'input') {
        fieldType = 'input-field';
      } else if (fieldType === 'textarea') {
        fieldType = 'textarea-field';
      } else if (fieldType === 'select') {
        fieldType = 'select-field';
      } else if (fieldType === 'radio') {
        fieldType = 'radio-field';
      } else if (fieldType === 'checkbox') {
        fieldType = 'checkbox-field';
      } else if (fieldType === 'date') {
        fieldType = 'date-field';
      } else if (fieldType === 'attachment') {
        fieldType = 'attachment-field';
      } else if (fieldType === 'region') {
        fieldType = 'region-field';
      } else if (fieldType === 'rating') {
        fieldType = 'rating-field';
      } else if (fieldType === 'formula') {
        fieldType = 'formula-field';
      }
      console.log(this.field.fieldType,fieldType || 'input-field')
      return fieldType || 'input-field';
      // 使用新的组件名称映射
      // const componentName = formFieldComponentNames[this.field.fieldType];
      // return componentName || 'InputField';
    },
    fieldProps() {
      return {
        field: this.field,
        value: this.value
      };
    }
  },
  methods: {
    handleValueUpdate(val) {
      this.$emit('update:value', val);
    }
  }
};
</script>

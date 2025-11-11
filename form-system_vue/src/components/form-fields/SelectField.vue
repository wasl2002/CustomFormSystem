<template>
  <a-select 
    v-model:value="localValue"
    v-bind="fieldProps"
    :placeholder="field.fieldName"
    :allowClear="true"
    :style="{ width: '100%', paddingRight: '30px' }"
  >
    <a-select-option 
      v-for="option in fieldOptions" 
      :key="option.value" 
      :value="option.value"
    >
      {{ option.label }}
    </a-select-option>
  </a-select>
</template>

<script>
export default {
  name: 'SelectField',
  props: {
    field: {
      type: Object,
      required: true
    },
    value: {
      type: [String, Number],
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
    },
    fieldOptions() {
      if (!this.field.fieldOptions) return [];
      
      try {
        const options = [];
        const lines = this.field.fieldOptions.split('\n');
        
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
  }
};
</script>
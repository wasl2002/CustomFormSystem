<template>
  <a-rate
    v-model:value="localValue"
    v-bind="rateProps"
  />
</template>

<script>
export default {
  name: 'RatingField',
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
    rateProps() {
      const props = {};
      
      // 从字段属性中获取评分配置
      if (this.field.fieldProperties) {
        try {
          const fieldProperties = JSON.parse(this.field.fieldProperties);
          if (fieldProperties.max !== undefined) {
            props.count = fieldProperties.max;
          }
          if (fieldProperties.allowHalf !== undefined) {
            props.allowHalf = fieldProperties.allowHalf;
          }
          if (fieldProperties.icon) {
            // 这里可以添加自定义图标支持
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
<template>
  <a-cascader
    v-model:value="localValue"
    v-bind="cascaderProps"
    :placeholder="field.fieldName"
  />
</template>

<script>
export default {
  name: 'RegionField',
  props: {
    field: {
      type: Object,
      required: true
    },
    value: {
      type: Array,
      default: () => []
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
    cascaderProps() {
      const props = {
        options: this.regionOptions,
        changeOnSelect: true
      };
      
      // 从字段属性中获取配置
      if (this.field.fieldProperties) {
        try {
          const fieldProperties = JSON.parse(this.field.fieldProperties);
          if (fieldProperties.allowEmpty !== undefined) {
            props.allowClear = fieldProperties.allowEmpty;
          }
        } catch (e) {
          console.error('解析字段属性失败:', e);
        }
      }
      
      return props;
    },
    regionOptions() {
      // 这里应该从后端获取真实的省市区数据
      // 为了演示，返回一些示例数据
      return [
        {
          value: 'beijing',
          label: '北京市',
          children: [
            {
              value: 'beijing-shi',
              label: '北京市',
              children: [
                { value: 'dongcheng', label: '东城区' },
                { value: 'xicheng', label: '西城区' },
                { value: 'chaoyang', label: '朝阳区' }
              ]
            }
          ]
        },
        {
          value: 'shanghai',
          label: '上海市',
          children: [
            {
              value: 'shanghai-shi',
              label: '上海市',
              children: [
                { value: 'huangpu', label: '黄浦区' },
                { value: 'jingan', label: '静安区' },
                { value: 'xuhui', label: '徐汇区' }
              ]
            }
          ]
        }
      ];
    }
  }
};
</script>
<template>
  <div>
    <a-form-item label="日期类型">
      <a-select 
        v-model:value="dateType" 
        :disabled="isViewMode"
      >
        <a-select-option value="date">日期</a-select-option>
        <a-select-option value="datetime">日期时间</a-select-option>
        <a-select-option value="week">周</a-select-option>
        <a-select-option value="month">月</a-select-option>
        <a-select-option value="quarter">季度</a-select-option>
        <a-select-option value="year">年</a-select-option>
      </a-select>
    </a-form-item>
    
    <a-form-item label="显示格式">
      <a-input 
        v-model:value="dateFormat" 
        placeholder="YYYY-MM-DD"
        :disabled="isViewMode"
      />
    </a-form-item>
    
    <a-form-item label="值格式">
      <a-input 
        v-model:value="valueFormat" 
        placeholder="YYYY-MM-DD"
        :disabled="isViewMode"
      />
    </a-form-item>
  </div>
</template>

<script>
export default {
  name: 'DateFieldConfig',
  props: {
    field: {
      type: Object,
      required: true
    },
    isViewMode: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:field'],
  computed: {
    dateType: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.dateType || 'date';
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('dateType', value);
        }
      }
    },
    
    dateFormat: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.dateFormat || 'YYYY-MM-DD';
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('dateFormat', value);
        }
      }
    },
    
    valueFormat: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.valueFormat || 'YYYY-MM-DD';
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('valueFormat', value);
        }
      }
    }
  },
  methods: {
    parseFieldProperties() {
      if (!this.field.fieldProperties) {
        return {};
      }
      
      try {
        return JSON.parse(this.field.fieldProperties);
      } catch (error) {
        console.error('解析字段属性失败:', error);
        return {};
      }
    },
    
    updateFieldProperty(key, value) {
      if (this.isViewMode) return;
      
      try {
        let properties = {};
        if (this.field.fieldProperties) {
          properties = JSON.parse(this.field.fieldProperties);
        }
        
        properties[key] = value;
        const updatedField = {
          ...this.field,
          fieldProperties: JSON.stringify(properties)
        };
        
        this.$emit('update:field', updatedField);
      } catch (error) {
        console.error('更新字段属性失败:', error);
      }
    }
  }
};
</script>
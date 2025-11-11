<template>
  <div>
    <a-form-item label="明细标题">
      <a-input 
        v-model:value="detailTitle" 
        placeholder="明细"
        :disabled="isViewMode"
      />
    </a-form-item>
    
    <a-form-item label="最少行数">
      <a-input-number 
        v-model:value="minRows" 
        :min="0" 
        :disabled="isViewMode"
      />
    </a-form-item>
    
    <a-form-item label="最多行数">
      <a-input-number 
        v-model:value="maxRows" 
        :min="1" 
        :disabled="isViewMode"
      />
    </a-form-item>
    
    <a-alert 
      message="明细组件内可添加其他组件，形成列表结构" 
      type="info" 
      show-icon 
      style="margin-top: 16px"
    />
  </div>
</template>

<script>
export default {
  name: 'DetailFieldConfig',
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
    detailTitle: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.title || '明细';
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('title', value);
        }
      }
    },
    
    minRows: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.minRows || 0;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('minRows', value);
        }
      }
    },
    
    maxRows: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.maxRows || 10;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('maxRows', value);
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
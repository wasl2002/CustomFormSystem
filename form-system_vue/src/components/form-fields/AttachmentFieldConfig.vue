<template>
  <div>
    <a-form-item label="允许预览">
      <a-switch 
        v-model:checked="allowPreview" 
        :disabled="isViewMode"
      />
    </a-form-item>
    
    <a-form-item label="最大文件大小(MB)">
      <a-input-number 
        v-model:value="maxFileSize" 
        :min="1" 
        :max="100"
        :disabled="isViewMode"
      />
    </a-form-item>
    
    <a-form-item label="允许的文件类型">
      <a-input 
        v-model:value="allowedTypes" 
        placeholder="image/*,.pdf,.doc,.docx"
        :disabled="isViewMode"
      />
    </a-form-item>
  </div>
</template>

<script>
export default {
  name: 'AttachmentFieldConfig',
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
    allowPreview: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.allowPreview !== false; // 默认为true
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('allowPreview', value);
        }
      }
    },
    
    maxFileSize: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.maxFileSize || 10;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('maxFileSize', value);
        }
      }
    },
    
    allowedTypes: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.allowedTypes || 'image/*,.pdf,.doc,.docx';
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('allowedTypes', value);
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
<template>
  <div>
    <a-checkbox 
      v-model:checked="textareaReadOnly" 
      :disabled="isViewMode"
    >
      只读
    </a-checkbox>
    <a-checkbox 
      v-model:checked="textareaDisabled" 
      :disabled="isViewMode"
      style="margin-left: 16px;"
    >
      禁用
    </a-checkbox>
    <a-form-item label="行数" style="margin-top: 8px;">
      <a-input-number 
        v-model:value="textareaRows" 
        :min="1" 
        :max="20"
        :disabled="isViewMode"
      />
    </a-form-item>
  </div>
</template>

<script>
export default {
  name: 'TextareaAttributeConfig',
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
    textareaReadOnly: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.readonly === true;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('readonly', value);
        }
      }
    },
    
    textareaDisabled: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.disabled === true;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('disabled', value);
        }
      }
    },
    
    textareaRows: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.rows || 3;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('rows', value);
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
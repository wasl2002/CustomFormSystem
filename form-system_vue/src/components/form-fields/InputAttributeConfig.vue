<template>
  <div>
    <a-checkbox 
      v-model:checked="inputReadOnly" 
      :disabled="isViewMode"
    >
      只读
    </a-checkbox>
    <a-checkbox 
      v-model:checked="inputDisabled" 
      :disabled="isViewMode"
      style="margin-left: 16px;"
    >
      禁用
    </a-checkbox>
  </div>
</template>

<script>
export default {
  name: 'InputAttributeConfig',
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
    inputReadOnly: {
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
    
    inputDisabled: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.disabled === true;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('disabled', value);
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
<template>
  <div>
    <a-form-item label="选择级别">
      <a-select 
        v-model:value="regionLevel" 
        :disabled="isViewMode"
      >
        <a-select-option value="province">省</a-select-option>
        <a-select-option value="province-city">省市</a-select-option>
        <a-select-option value="province-city-district">省市区</a-select-option>
      </a-select>
    </a-form-item>
    
    <a-form-item label="允许空值">
      <a-switch 
        v-model:checked="allowEmptyRegion" 
        :disabled="isViewMode"
      />
    </a-form-item>
  </div>
</template>

<script>
export default {
  name: 'RegionFieldConfig',
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
    regionLevel: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.level || 'province-city-district';
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('level', value);
        }
      }
    },
    
    allowEmptyRegion: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.allowEmpty === true;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('allowEmpty', value);
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
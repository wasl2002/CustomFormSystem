<template>
  <div>
    <a-form-item label="计算表达式">
      <a-textarea 
        v-model:value="formulaExpression" 
        placeholder="例如: field1 + field2 * 0.1"
        :disabled="isViewMode"
        :auto-size="{ minRows: 2, maxRows: 4 }"
      />
      <div style="margin-top: 8px; font-size: 12px; color: #666;">
        可用字段: 
        <a-tag 
          v-for="field in availableFieldsForFormula" 
          :key="field.value" 
          style="margin-right: 4px; margin-bottom: 4px;"
        >
          {{ field.label }}({{ field.value }})
        </a-tag>
      </div>
    </a-form-item>
    
    <a-form-item label="小数位数">
      <a-input-number 
        v-model:value="formulaPrecision" 
        :min="0" 
        :max="10"
        :disabled="isViewMode"
      />
    </a-form-item>
    
    <a-form-item label="自动计算">
      <a-switch 
        v-model:checked="autoCalculate" 
        :disabled="isViewMode"
      />
    </a-form-item>
    
    <a-form-item label="显示为大写汉字">
      <a-switch 
        v-model:checked="showInChinese" 
        :disabled="isViewMode"
      />
    </a-form-item>
    
    <a-alert 
      message="支持四则运算（+-*/）、数字（0-9）和括号，结果将根据配置自动计算并显示" 
      type="info" 
      show-icon 
      style="margin-top: 16px"
    />
  </div>
</template>

<script>
export default {
  name: 'FormulaFieldConfig',
  props: {
    field: {
      type: Object,
      required: true
    },
    isViewMode: {
      type: Boolean,
      default: false
    },
    allFields: {
      type: Array,
      default: () => []
    }
  },
  emits: ['update:field'],
  computed: {
    formulaExpression: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.expression || '';
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('expression', value);
        }
      }
    },
    
    formulaPrecision: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.precision !== undefined ? properties.precision : 2;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('precision', value);
        }
      }
    },
    
    autoCalculate: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.autoCalculate !== false; // 默认为true
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('autoCalculate', value);
        }
      }
    },
    
    showInChinese: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.showInChinese === true;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('showInChinese', value);
        }
      }
    },
    
    // 可用作计算的字段列表
    availableFieldsForFormula() {
      return this.allFields
        .filter(field => 
          field.fieldType === 'input' || 
          field.fieldType === 'textarea' || 
          field.fieldType === 'select' || 
          field.fieldType === 'radio' ||
          field.fieldType === 'formula'
        )
        .map(field => ({
          value: field.fieldKey,
          label: field.fieldName
        }));
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
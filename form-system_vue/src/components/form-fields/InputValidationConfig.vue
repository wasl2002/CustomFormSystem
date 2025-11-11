<template>
  <div>
    <a-checkbox 
      v-model:checked="requiredValidation" 
      :disabled="isViewMode"
    >
      必填
    </a-checkbox>
    <a-checkbox 
      v-model:checked="emailValidation" 
      :disabled="isViewMode"
      style="margin-left: 16px;"
    >
      邮箱格式
    </a-checkbox>
    <a-checkbox 
      v-model:checked="numberValidation" 
      :disabled="isViewMode"
      style="margin-left: 16px;"
    >
      数字格式
    </a-checkbox>
    <a-form-item label="最小长度" style="margin-top: 8px;">
      <a-input-number 
        v-model:value="minLength" 
        :min="0" 
        :disabled="isViewMode"
      />
    </a-form-item>
    <a-form-item label="最大长度">
      <a-input-number 
        v-model:value="maxLength" 
        :min="0" 
        :disabled="isViewMode"
      />
    </a-form-item>
  </div>
</template>

<script>
export default {
  name: 'InputValidationConfig',
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
    requiredValidation: {
      get() {
        const validations = this.parseFieldValidations();
        return validations.required === true;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldValidation('required', value);
        }
      }
    },
    
    emailValidation: {
      get() {
        const validations = this.parseFieldValidations();
        return validations.email === true;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldValidation('email', value);
        }
      }
    },
    
    numberValidation: {
      get() {
        const validations = this.parseFieldValidations();
        return validations.number === true;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldValidation('number', value);
        }
      }
    },
    
    minLength: {
      get() {
        const validations = this.parseFieldValidations();
        return validations.minLength || 0;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldValidation('minLength', value);
        }
      }
    },
    
    maxLength: {
      get() {
        const validations = this.parseFieldValidations();
        return validations.maxLength || 0;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldValidation('maxLength', value);
        }
      }
    }
  },
  methods: {
    parseFieldValidations() {
      if (!this.field.fieldValidations) {
        return {};
      }
      
      try {
        return JSON.parse(this.field.fieldValidations);
      } catch (error) {
        console.error('解析字段校验规则失败:', error);
        return {};
      }
    },
    
    updateFieldValidation(key, value) {
      if (this.isViewMode) return;
      
      try {
        let validations = {};
        if (this.field.fieldValidations) {
          validations = JSON.parse(this.field.fieldValidations);
        }
        
        if (value === false || value === 0 || value === '') {
          delete validations[key];
        } else {
          validations[key] = value;
        }
        
        const updatedField = {
          ...this.field,
          fieldValidations: Object.keys(validations).length > 0 ? JSON.stringify(validations) : ''
        };
        
        this.$emit('update:field', updatedField);
      } catch (error) {
        console.error('更新字段校验规则失败:', error);
      }
    }
  }
};
</script>
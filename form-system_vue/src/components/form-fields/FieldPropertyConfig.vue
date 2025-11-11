<template>
  <div>
    <!-- 字段名称 -->
    <a-form-item label="字段名称">
      <a-input 
        v-model:value="localField.fieldName" 
        :disabled="isViewMode"
        @change="updateField"
      />
    </a-form-item>
    
    <!-- 字段类型 -->
    <a-form-item label="字段类型">
      <a-tag>{{ getFieldTypeLabel(localField.fieldType) }}</a-tag>
    </a-form-item>
    
    <!-- 默认值 -->
    <a-form-item label="默认值">
      <div style="position: relative;">
        <component 
          :is="defaultValueComponent" 
          v-model:value="defaultValue"
          :field="localField"
          :disabled="isViewMode"
          style="width: 100%; padding-right: 30px;"
        />
        <a-button 
          v-if="defaultValue && localField.fieldType !== 'select'"
          type="text" 
          size="small" 
          @click="clearDefaultValue"
          :disabled="isViewMode"
          style="position: absolute; right: 8px; top: 50%; transform: translateY(-50%);"
        >
          <template #icon>
            <svg viewBox="64 64 896 896" focusable="false" data-icon="close-circle" width="1em" height="1em" fill="currentColor" aria-hidden="true"><path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm165.4 618.2l-66-.3L512 563.4l-99.3 118.4-66.1.3c-4.4 0-8-3.5-8-8 0-1.9.7-3.7 1.9-5.2l130.1-155L340.5 359a8.32 8.32 0 01-1.9-5.2c0-4.4 3.6-8 8-8l66.1.3L512 464.6l99.3-118.4 66-.3c4.4 0 8 3.5 8 8 0 1.9-.7 3.7-1.9 5.2L553.5 514l130 155c1.2 1.5 1.9 3.3 1.9 5.2 0 4.4-3.6 8-8 8z"></path></svg>
          </template>
        </a-button>
      </div>
    </a-form-item>
    
    <!-- 是否必填 -->
    <a-form-item label="是否必填">
      <a-switch 
        v-model:checked="isRequired" 
        :disabled="isViewMode"
      />
    </a-form-item>
    
    <!-- 字段选项配置 -->
    <a-form-item 
      v-if="localField.fieldType === 'select' || 
            localField.fieldType === 'radio' || 
            localField.fieldType === 'checkbox'" 
      label="选项配置"
    >
      <a-textarea 
        v-model:value="fieldOptionsText" 
        :disabled="isViewMode"
        :auto-size="{ minRows: 4, maxRows: 10 }"
        placeholder="每行一个选项，格式：值=标签"
        @change="onFieldOptionsChange"
      />
    </a-form-item>
    
    <!-- 字段特定配置组件 -->
    <component 
      :is="fieldConfigComponent" 
      :field="localField"
      :is-view-mode="isViewMode"
      @update:field="updatedField => { localField = updatedField; updateField(); }"
    />
    
    <!-- 字段属性配置组件 -->
    <component 
      :is="fieldAttributeConfigComponent" 
      :field="localField"
      :is-view-mode="isViewMode"
      @update:field="updatedField => { localField = updatedField; updateField(); }"
    />
    
    <!-- 字段校验规则配置组件 -->
    <component 
      :is="fieldValidationConfigComponent" 
      :field="localField"
      :is-view-mode="isViewMode"
      @update:field="updatedField => { localField = updatedField; updateField(); }"
    />
    
    <!-- 高级配置 -->
    <a-collapse v-model:activeKey="activeKeys" ghost>
      <a-collapse-panel key="advanced" header="高级配置">
        <!-- 字段属性（文本格式） -->
        <a-form-item label="字段属性">
          <a-textarea 
            v-model:value="fieldPropertiesText" 
            :disabled="isViewMode"
            :auto-size="{ minRows: 4, maxRows: 10 }"
            placeholder="每行一个属性，格式：键=值"
            @change="onFieldPropertiesChange"
          />
        </a-form-item>
        
        <!-- 字段校验规则（JSON格式） -->
        <a-form-item label="校验规则">
          <a-textarea 
            v-model:value="fieldValidationsText" 
            :disabled="isViewMode"
            :auto-size="{ minRows: 4, maxRows: 10 }"
            placeholder="JSON格式的校验规则"
            @change="onFieldValidationsChange"
          />
        </a-form-item>
      </a-collapse-panel>
    </a-collapse>
  </div>
</template>

<script>
import DateFieldConfig from './DateFieldConfig.vue';
import AttachmentFieldConfig from './AttachmentFieldConfig.vue';
import RegionFieldConfig from './RegionFieldConfig.vue';
import RatingFieldConfig from './RatingFieldConfig.vue';
import FormulaFieldConfig from './FormulaFieldConfig.vue';
import DetailFieldConfig from './DetailFieldConfig.vue';
import InputAttributeConfig from './InputAttributeConfig.vue';
import TextareaAttributeConfig from './TextareaAttributeConfig.vue';
import InputValidationConfig from './InputValidationConfig.vue';
// 导入默认值组件
import InputField from './InputField.vue';
import TextareaField from './TextareaField.vue';
import SelectField from './SelectField.vue';
import RadioField from './RadioField.vue';
import CheckboxField from './CheckboxField.vue';
import DateField from './DateField.vue';

export default {
  name: 'FieldPropertyConfig',
  components: {
    DateFieldConfig,
    AttachmentFieldConfig,
    RegionFieldConfig,
    RatingFieldConfig,
    FormulaFieldConfig,
    DetailFieldConfig,
    InputAttributeConfig,
    TextareaAttributeConfig,
    InputValidationConfig,
    // 注册默认值组件
    InputField,
    TextareaField,
    SelectField,
    RadioField,
    CheckboxField,
    DateField
  },
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
  data() {
    return {
      localField: JSON.parse(JSON.stringify(this.field)),
      fieldOptionsText: '',
      fieldPropertiesText: '',
      fieldValidationsText: '',
      activeKeys: ['advanced']
    };
  },
  watch: {
    field: {
      handler(newField) {
        this.localField = JSON.parse(JSON.stringify(newField));
        this.updateFieldOptionsText();
        this.updateFieldPropertiesText();
        this.updateFieldValidationsText();
      },
      deep: true,
      immediate: true
    }
  },
  computed: {
    // 是否必填计算属性
    isRequired: {
      get() {
        return this.localField.isRequired === 1;
      },
      set(value) {
        if (!this.isViewMode) {
          this.localField.isRequired = value ? 1 : 0;
          this.updateField();
        }
      }
    },
    
    // 默认值组件
    defaultValueComponent() {
      const componentMap = {
        'input': 'InputField',
        'textarea': 'TextareaField',
        'select': 'SelectField',
        'radio': 'RadioField',
        'checkbox': 'CheckboxField',
        'date': 'DateField'
      };
      
      // 如果没有匹配的组件，使用输入框作为默认组件
      const component = componentMap[this.localField.fieldType];
      return component || 'InputField'; // 确保不会返回null或undefined
    },
    
    // 默认值计算属性
    defaultValue: {
      get() {
        // 从字段属性中解析默认值
        if (this.localField.fieldProperties) {
          try {
            const properties = JSON.parse(this.localField.fieldProperties);
            const defaultValue = properties.defaultValue;
            
            // 处理复选框的默认值（数组类型）
            if (this.localField.fieldType === 'checkbox' && Array.isArray(defaultValue)) {
              return defaultValue;
            }
            
            // 处理日期类型的默认值
            if (this.localField.fieldType === 'date' && defaultValue) {
              // 如果是字符串格式的日期，转换为 dayjs 对象
              if (typeof defaultValue === 'string') {
                return defaultValue;
              }
            }
            
            return defaultValue !== undefined ? defaultValue : '';
          } catch (e) {
            console.error('解析字段属性失败:', e);
          }
        }
        return '';
      },
      set(value) {
        if (this.isViewMode) return;
        
        // 更新字段属性中的默认值
        let properties = {};
        if (this.localField.fieldProperties) {
          try {
            properties = JSON.parse(this.localField.fieldProperties);
          } catch (e) {
            console.error('解析字段属性失败:', e);
          }
        }
        
        // 设置默认值
        properties.defaultValue = value;
        
        // 更新字段属性
        this.localField.fieldProperties = JSON.stringify(properties);
        this.updateField();
      }
    },
    
    // 字段选项
    fieldOptions() {
      if (!this.localField.fieldOptions) return [];
      
      try {
        const options = [];
        const lines = this.localField.fieldOptions.split('\n');
        
        lines.forEach(line => {
          if (line.trim()) {
            const [value, label] = line.split('=');
            if (value && label) {
              options.push({
                value: value.trim(),
                label: label.trim()
              });
            }
          }
        });
        
        return options;
      } catch (error) {
        console.error('解析字段选项失败:', error);
        return [];
      }
    },
    
    // 字段配置组件
    fieldConfigComponent() {
      const componentMap = {
        'date': 'DateFieldConfig',
        'attachment': 'AttachmentFieldConfig',
        'region': 'RegionFieldConfig',
        'rating': 'RatingFieldConfig',
        'formula': 'FormulaFieldConfig',
        'detail': 'DetailFieldConfig'
      };
      const component = componentMap[this.localField.fieldType];
      return component || 'div'; // 确保不会返回null或undefined
    },
    
    // 字段属性配置组件
    fieldAttributeConfigComponent() {
      const componentMap = {
        'input': 'InputAttributeConfig',
        'textarea': 'TextareaAttributeConfig'
      };
      const component = componentMap[this.localField.fieldType];
      return component || 'div'; // 确保不会返回null或undefined
    },
    
    // 字段校验规则配置组件
    fieldValidationConfigComponent() {
      const componentMap = {
        'input': 'InputValidationConfig',
        'textarea': 'InputValidationConfig'
      };
      const component = componentMap[this.localField.fieldType];
      return component || 'div'; // 确保不会返回null或undefined
    }
  },
  methods: {
    updateField() {
      this.$emit('update:field', this.localField);
    },
    
    // 获取字段类型标签
    getFieldTypeLabel(fieldType) {
      const fieldTypeLabels = {
        'input': '输入框',
        'textarea': '文本域',
        'select': '下拉框',
        'radio': '单选框',
        'checkbox': '复选框',
        'date': '日期选择',
        'attachment': '附件上传',
        'region': '省市区选择',
        'rating': '评分组件',
        'formula': '计算公式',
        'detail': '明细组件'
      };
      
      return fieldTypeLabels[fieldType] || '未知类型';
    },
    
    // 更新字段选项文本
    updateFieldOptionsText() {
      this.fieldOptionsText = this.localField.fieldOptions || '';
    },
    
    // 更新字段属性文本
    updateFieldPropertiesText() {
      if (!this.localField.fieldProperties) {
        this.fieldPropertiesText = '';
        return;
      }
      
      try {
        const properties = JSON.parse(this.localField.fieldProperties);
        this.fieldPropertiesText = Object.keys(properties)
          .map(key => `${key}=${properties[key]}`)
          .join('\n');
      } catch (error) {
        console.error('解析字段属性失败:', error);
        this.fieldPropertiesText = this.localField.fieldProperties;
      }
    },
    
    // 更新字段校验规则文本
    updateFieldValidationsText() {
      if (!this.localField.fieldValidations) {
        this.fieldValidationsText = '';
        return;
      }
      
      try {
        this.fieldValidationsText = JSON.stringify(JSON.parse(this.localField.fieldValidations), null, 2);
      } catch (error) {
        console.error('解析字段校验规则失败:', error);
        this.fieldValidationsText = this.localField.fieldValidations;
      }
    },
    
    // 字段选项文本改变时的处理
    onFieldOptionsChange(e) {
      if (this.isViewMode) return;
      this.fieldOptionsText = e.target.value;
      this.localField.fieldOptions = this.fieldOptionsText;
      this.updateField();
    },
    
    // 字段属性文本改变时的处理
    onFieldPropertiesChange(e) {
      if (this.isViewMode) return;
      this.fieldPropertiesText = e.target.value;
      this.updateFieldProperties(this.localField, this.fieldPropertiesText);
      this.updateField();
    },
    
    // 字段校验规则文本改变时的处理
    onFieldValidationsChange(e) {
      if (this.isViewMode) return;
      this.fieldValidationsText = e.target.value;
      this.updateFieldValidations(this.localField, this.fieldValidationsText);
      this.updateField();
    },
    
    // 更新字段属性
    updateFieldProperties(field, propertiesText) {
      if (this.isViewMode) return;
      if (!propertiesText) {
        field.fieldProperties = null;
        return;
      }
      
      try {
        const properties = {};
        const lines = propertiesText.split('\n');
        
        lines.forEach(line => {
          if (line.trim()) {
            const [key, value] = line.split('=');
            if (key && value) {
              // 尝试转换为适当的类型
              const trimmedValue = value.trim();
              if (trimmedValue === 'true') {
                properties[key.trim()] = true;
              } else if (trimmedValue === 'false') {
                properties[key.trim()] = false;
              } else if (!isNaN(trimmedValue) && !isNaN(parseFloat(trimmedValue))) {
                properties[key.trim()] = parseFloat(trimmedValue);
              } else {
                properties[key.trim()] = trimmedValue;
              }
            }
          }
        });
        
        field.fieldProperties = JSON.stringify(properties);
      } catch (error) {
        console.error('更新字段属性失败:', error);
        field.fieldProperties = propertiesText;
      }
    },
    
    // 更新字段校验规则
    updateFieldValidations(field, validationsText) {
      if (this.isViewMode) return;
      if (!validationsText) {
        field.fieldValidations = null;
        return;
      }
      
      try {
        // 验证是否为有效的JSON
        const validations = JSON.parse(validationsText);
        field.fieldValidations = JSON.stringify(validations);
      } catch (error) {
        console.error('更新字段校验规则失败:', error);
        // 保持原始文本，但不保存为JSON
        field.fieldValidations = validationsText;
      }
    },
    
    // 清空默认值
    clearDefaultValue() {
      if (this.isViewMode) return;
      
      // 从字段属性中完全移除 defaultValue
      let properties = {};
      if (this.localField.fieldProperties) {
        try {
          properties = JSON.parse(this.localField.fieldProperties);
        } catch (e) {
          console.error('解析字段属性失败:', e);
        }
      }
      
      // 删除 defaultValue 属性
      delete properties.defaultValue;
      
      // 如果还有其他属性，更新字段属性；否则设为 null
      if (Object.keys(properties).length > 0) {
        this.localField.fieldProperties = JSON.stringify(properties);
      } else {
        this.localField.fieldProperties = null;
      }
      
      this.updateField();
    }
  }
};
</script>
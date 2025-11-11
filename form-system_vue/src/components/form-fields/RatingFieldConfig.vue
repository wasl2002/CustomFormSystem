<template>
  <div>
    <a-form-item label="最大分数">
      <a-input-number 
        v-model:value="maxRating" 
        :min="1" 
        :max="10"
        :disabled="isViewMode"
      />
    </a-form-item>
    
    <a-form-item label="允许半星">
      <a-switch 
        v-model:checked="allowHalfRating" 
        :disabled="isViewMode"
      />
    </a-form-item>
    
    <a-form-item label="图标类型">
      <a-select 
        v-model:value="ratingIcon" 
        :disabled="isViewMode"
      >
        <a-select-option value="star">星星</a-select-option>
        <a-select-option value="heart">爱心</a-select-option>
      </a-select>
    </a-form-item>
  </div>
</template>

<script>
export default {
  name: 'RatingFieldConfig',
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
    maxRating: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.max || 5;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('max', value);
        }
      }
    },
    
    allowHalfRating: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.allowHalf === true;
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('allowHalf', value);
        }
      }
    },
    
    ratingIcon: {
      get() {
        const properties = this.parseFieldProperties();
        return properties.icon || 'star';
      },
      set(value) {
        if (!this.isViewMode) {
          this.updateFieldProperty('icon', value);
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
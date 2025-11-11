<template>
  <a-layout>
    <a-layout-content>
      <a-card title="表单设计器">
        <a-row :gutter="16">
          <!-- 左侧组件库 -->
          <a-col :span="4">
            <a-card title="组件库" size="small">
              <div
                v-for="(label, type) in fieldTypes"
                :key="type"
                class="component-item"
                draggable="true"
                @dragstart="handleDragStart($event, type)"
              >
                {{ label }}
              </div>
            </a-card>
          </a-col>

          <!-- 中间设计区域 -->
          <a-col :span="14">
            <a-card title="设计区域" size="small">
              <div
                class="design-area"
                @dragover="handleDragOver"
                @drop="handleDrop"
              >
                <draggable
                  v-model="formFields"
                  tag="div"
                  :animation="200"
                  item-key="fieldKey"
                  @end="onDragEnd"
                >
                  <template #item="{ element: field, index }">
                    <div
                      class="form-field-item"
                      :class="{ 'selected': selectedField === index }"
                      @click="editField(index)"
                    >
                      <div class="field-header">
                        <span>{{ field.fieldName }}</span>
                        <div>
                          <a-button
                            type="text"
                            size="small"
                            @click.stop="copyField(index)"
                          >
                            复制
                          </a-button>
                          <a-button
                            type="text"
                            size="small"
                            @click.stop="moveFieldUp(index)"
                            :disabled="index === 0"
                          >
                            ↑
                          </a-button>
                          <a-button
                            type="text"
                            size="small"
                            @click.stop="moveFieldDown(index)"
                            :disabled="index === formFields.length - 1"
                          >
                            ↓
                          </a-button>
                          <a-button
                            type="text"
                            danger
                            size="small"
                            @click.stop="removeField(index)"
                          >
                            删除
                          </a-button>
                        </div>
                      </div>
                      <div class="field-preview">
                        <FormField
                          :field="field"
                          :value="getFieldPreviewValue(field)"
                        />
                      </div>
                    </div>
                  </template>
                </draggable>
                <div v-if="formFields.length === 0" class="empty-design-area">
                  请从左侧拖拽组件到此处
                </div>
              </div>
            </a-card>
          </a-col>

          <!-- 右侧属性面板 -->
          <a-col :span="6">
            <a-card title="字段属性" size="small">
              <div v-if="selectedField !== null">
                <a-form layout="vertical">
                  <FieldPropertyConfig
                    :field="formFields[selectedField]"
                    :is-view-mode="isViewMode"
                    :all-fields="formFields"
                    @update:field="updateField"
                  />
                </a-form>
              </div>
              <div v-else>
                <a-alert message="请选择一个字段进行配置" type="info" show-icon />
              </div>
            </a-card>
          </a-col>
        </a-row>

        <!-- 底部操作按钮 -->
        <div style="text-align: center; margin-top: 20px;">
          <a-button @click="cancel">取消</a-button>
          <a-button
            type="primary"
            @click="saveForm"
            style="margin-left: 10px"
            :disabled="isViewMode"
          >
            保存
          </a-button>
        </div>
      </a-card>
    </a-layout-content>
  </a-layout>
</template>

<script>
import { getFormConfigById, getFormConfigByVersionId, createFormConfig, updateFormConfig } from '../api/formConfig';
import { getFieldsByFormId } from '../api/formField';
import { batchCreateFields } from '../api/formField';
import { message } from 'ant-design-vue';
import draggable from 'vuedraggable';
import { formFieldComponents } from '../components/form-fields';
import FieldPropertyConfig from '../components/form-fields/FieldPropertyConfig.vue';
import FormField from '../components/FormField.vue';

export default {
  name: 'FormDesigner',
  components: {
    draggable,
    FormField,
    ...formFieldComponents,
    FieldPropertyConfig
  },
  props: {
    id: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      formConfig: {
        formKey: '',
        formName: '',
        formDescription: '',
        status: 1
      },
      formFields: [],
      selectedField: null,
      fieldTypes: {
        'input-field': '输入框',
        'textarea-field': '文本域',
        'select-field': '下拉框',
        'radio-field': '单选框',
        'checkbox-field': '复选框',
        'date-field': '日期选择',
        'attachment-field': '附件上传',
        'detail': '明细组件',
        'region-field': '省市区选择',
        'rating-field': '评分组件',
        'formula-field': '计算公式'
      },
      isViewMode: false // 是否为查看模式（查看历史版本）
    };
  },

  mounted() {
    if (this.id) {
      this.loadFormConfig(this.id);
    } else {
      // 新建表单时生成唯一标识
      this.formConfig.formKey = 'form_' + Date.now();
    }
  },

  methods: {
    async loadFormConfig(id) {
      try {
        // 判断是查看历史版本还是编辑最新版本
        let formResponse;
        if (this.$route.query.view === 'history') {
          // 查看历史版本
          formResponse = await getFormConfigByVersionId(id);
          this.isViewMode = true;
        } else {
          // 编辑最新版本
          formResponse = await getFormConfigById(id);
        }

        this.formConfig = formResponse.data.data;

        // 如果表单状态是停用的，也进入查看模式
        if (this.formConfig && this.formConfig.status === 3) {
          this.isViewMode = true;
        }

        // 加载字段配置
        await this.loadFormFields(this.formConfig.id);
      } catch (error) {
        console.error('加载表单配置失败:', error);
        message.error('加载表单配置失败');
      }
    },

    async loadFormFields(formId) {
      try {
        const response = await getFieldsByFormId(formId);
        this.formFields = response.data.data || [];
      } catch (error) {
        console.error('加载字段配置失败:', error);
        message.error('加载字段配置失败');
      }
    },

    // 拖拽开始事件
    handleDragStart(event, fieldType) {
      event.dataTransfer.setData('fieldType', fieldType);
      // 标记这是从组件库拖拽的新组件
      event.dataTransfer.setData('isNewField', 'true');
    },

    // 拖拽过程事件
    handleDragOver(event) {
      event.preventDefault();
    },

    // 拖拽放置事件
    handleDrop(event) {
      event.preventDefault();
      const fieldType = event.dataTransfer.getData('fieldType');
      const isNewField = event.dataTransfer.getData('isNewField');
      
      if (isNewField === 'true') {
        // 添加新字段
        this.addField(fieldType);
      }
      // 如果是移动现有字段，draggable组件会自动处理，不需要额外操作
    },

    // 添加字段
    addField(fieldType) {
      const fieldKey = `field_${Date.now()}`;
      const newField = {
        fieldKey: fieldKey,
        fieldName: this.getDefaultFieldName(fieldType),
        fieldType: fieldType,
        fieldOrder: this.formFields.length + 1,
        sortOrder: this.formFields.length + 1,
        isRequired: 0,
        fieldOptions: '',
        fieldProperties: null,
        fieldValidations: null
      };

      this.formFields.push(newField);
      this.selectedField = this.formFields.length - 1;
    },

    // 获取默认字段名称
    getDefaultFieldName(fieldType) {
      const fieldNames = {
        'input-field': '输入框',
        'textarea-field': '文本域',
        'select-field': '下拉框',
        'radio-field': '单选框',
        'checkbox-field': '复选框',
        'date-field': '日期选择',
        'attachment-field': '附件上传',
        'detail': '明细组件',
        'region-field': '省市区选择',
        'rating-field': '评分组件',
        'formula-field': '计算公式'
      };

      return fieldNames[fieldType] || '未命名字段';
    },

    // 编辑字段
    editField(index) {
      this.selectedField = index;
    },

    // 更新字段
    updateField(updatedField) {
      if (this.selectedField !== null && !this.isViewMode) {
        // 在Vue 3中，直接赋值即可，无需使用this.$set
        this.formFields[this.selectedField] = updatedField;
        // 触发视图更新
        this.$forceUpdate();
      }
    },

    // 移除字段
    removeField(index) {
      if (this.isViewMode) return;

      this.formFields.splice(index, 1);

      // 更新选中字段索引
      if (this.selectedField === index) {
        this.selectedField = null;
      } else if (this.selectedField > index) {
        this.selectedField--;
      }
    },

    // 字段上移
    moveFieldUp(index) {
      if (this.isViewMode || index === 0) return;

      const temp = this.formFields[index];
      // 在Vue 3中，直接赋值即可，无需使用this.$set
      this.formFields[index] = this.formFields[index - 1];
      this.formFields[index - 1] = temp;

      // 更新选中字段索引
      if (this.selectedField === index) {
        this.selectedField = index - 1;
      } else if (this.selectedField === index - 1) {
        this.selectedField = index;
      }
    },

    // 字段下移
    moveFieldDown(index) {
      if (this.isViewMode || index === this.formFields.length - 1) return;

      const temp = this.formFields[index];
      // 在Vue 3中，直接赋值即可，无需使用this.$set
      this.formFields[index] = this.formFields[index + 1];
      this.formFields[index + 1] = temp;

      // 更新选中字段索引
      if (this.selectedField === index) {
        this.selectedField = index + 1;
      } else if (this.selectedField === index + 1) {
        this.selectedField = index;
      }
    },

    // 复制字段
    copyField(index) {
      if (this.isViewMode) return;

      // 深拷贝字段数据
      const originalField = this.formFields[index];
      const newField = JSON.parse(JSON.stringify(originalField));

      // 生成新的字段key和名称
      const timestamp = Date.now();
      const randomNum = Math.floor(Math.random() * 1000);
      newField.fieldKey = `${originalField.fieldType}_${timestamp}_${randomNum}`;
      newField.fieldName = `${originalField.fieldName}_副本`;
      newField.id = null; // 清除ID，保存时会生成新的ID

      // 在当前字段下方插入新字段
      this.formFields.splice(index + 1, 0, newField);

      // 选中新复制的字段
      this.selectedField = index + 1;
    },

    // 拖拽结束事件
    onDragEnd() {
      // 重新计算字段顺序
      this.formFields.forEach((field, index) => {
        field.fieldOrder = index + 1;
        // 同时设置sortOrder字段以确保与后端一致
        field.sortOrder = index + 1;
      });
    },

    // 获取字段预览值
    getFieldPreviewValue(field) {
      // 根据字段类型返回预览值
      switch (field.fieldType) {
        case 'input-field':
        case 'input':
          return '请输入文本';
        case 'textarea-field':
        case 'textarea':
          return '请输入多行文本';
        case 'select-field':
        case 'select':
          return '';
        case 'radio-field':
        case 'radio':
          return '';
        case 'checkbox-field':
        case 'checkbox':
          return [];
        case 'date-field':
        case 'date':
          return '';
        case 'attachment-field':
        case 'attachment':
          return [];
        case 'region-field':
        case 'region':
          return '';
        case 'rating-field':
        case 'rating':
          return 0;
        case 'formula-field':
        case 'formula':
          return '';
        default:
          return '';
      }
    },

    // 保存表单
    async saveForm() {
      if (this.isViewMode) {
        message.warning('查看模式下无法保存');
        return;
      }

      try {
        // 验证表单配置
        if (!this.formConfig.formName.trim()) {
          message.warning('请输入表单名称');
          return;
        }

        if (this.formFields.length === 0) {
          message.warning('请至少添加一个字段');
          return;
        }

        // 验证字段配置
        for (let i = 0; i < this.formFields.length; i++) {
          const field = this.formFields[i];
          if (!field.fieldName.trim()) {
            message.warning(`第${i + 1}个字段的字段名称不能为空`);
            return;
          }
        }

        // 确保字段顺序正确
        this.formFields.forEach((field, index) => {
          field.fieldOrder = index + 1;
          field.sortOrder = index + 1;
        });

        let formId;

        // 保存表单配置
        if (this.formConfig.id) {
          // 更新表单
          const response = await updateFormConfig(this.formConfig.formKey,this.formConfig);
          formId = response.data.data.id;
        } else {
          // 创建表单
          const response = await createFormConfig(this.formConfig);
          formId = response.data.data.id;
        }

        // 保存字段配置
        if (formId) {
          // 批量保存字段
          const fieldData = this.formFields.map(field => ({
            ...field,
            formId: formId
          }));

          await batchCreateFields(fieldData);
          message.success('保存成功');
          this.$router.push('/form/list');
        }
      } catch (error) {
        console.error('保存表单失败:', error);
        message.error('保存表单失败');
      }
    },

    // 取消操作
    cancel() {
      this.$router.push('/form/list');
    }
  }
};
</script>

<style scoped>
.component-item {
  padding: 8px;
  margin-bottom: 8px;
  border: 1px dashed #d9d9d9;
  border-radius: 4px;
  cursor: move;
}

.component-item:hover {
  border-color: #1890ff;
}

.design-area {
  min-height: 400px;
  border: 1px dashed #d9d9d9;
  border-radius: 4px;
  padding: 16px;
}

.form-field-item {
  padding: 12px;
  margin-bottom: 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  cursor: pointer;
}

.form-field-item:hover {
  border-color: #1890ff;
}

.form-field-item.selected {
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.field-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-weight: bold;
}

.field-preview {
  padding: 8px;
  background: #fafafa;
  border-radius: 4px;
}

.empty-design-area {
  text-align: center;
  color: #999;
  padding: 40px 20px;
}
</style>

<template>
  <a-layout>
    <a-layout-content>
      <a-card size="small" :title="`数据管理: ${formConfig?.formDescription}`">
        <a-row :gutter="16">
          <!-- 左侧搜索区域 -->
          <a-col :span="6">
            <a-card size="small">
              <!-- 版本选择 -->
              <a-form-item label="版本选择">
                <a-select
                    v-model:value="selectedVersion"
                    style="width: 100%"
                    @change="onVersionChange"
                >
                  <a-select-option value="">全部版本</a-select-option>
                  <a-select-option
                      v-for="version in formVersions"
                      :key="version.formVersion"
                      :value="version.formVersion"
                  >
                    v{{ version.formVersion }}
                    <a-tag v-if="version.isLatest === 1" color="green">最新</a-tag>
                  </a-select-option>
                </a-select>
              </a-form-item>

              <!-- 搜索模式切换 -->
              <a-form-item label="搜索模式">
                <a-radio-group v-model:value="searchMode" @change="onSearchModeChange">
                  <a-radio value="single">单条件搜索</a-radio>
                  <a-radio value="multiple">多条件搜索</a-radio>
                </a-radio-group>
              </a-form-item>

              <!-- 单条件搜索 -->
              <div v-if="searchMode === 'single'">
                <!-- 搜索字段选择 -->
                <a-form-item label="搜索字段">
                  <a-select
                      v-model:value="searchForm.field"
                      allowClear
                      style="width: 100%"
                      @change="onSearchFieldChange"
                  >
                    <a-select-option value="">请选择字段</a-select-option>
                    <a-select-option
                        v-for="field in formFields"
                        :key="field.fieldKey"
                        :value="field.fieldKey"
                    >
                      {{ field.fieldName }}
                    </a-select-option>
                  </a-select>
                </a-form-item>

                <!-- 搜索值输入 -->
                <a-form-item label="搜索值">
                  <FormField
                      :field="getSearchField()"
                      :placeholder="getSearchPlaceholder()"
                      :value="searchForm.value"
                      @update:value="val => searchForm.value = val"
                  />
                </a-form-item>
              </div>

              <!-- 多条件搜索 -->
              <div v-else>
                <a-form-item label="搜索条件">
                  <a-button size="small" style="margin-bottom: 8px;" type="primary" @click="addSearchCondition">
                    添加条件
                  </a-button>
                  <div v-for="(condition, index) in multipleSearchConditions" :key="index" style="margin-bottom: 8px;">
                    <a-row :gutter="8">
                      <a-col :span="10">
                        <a-select
                            v-model:value="condition.fieldKey"
                            placeholder="选择字段"
                            size="small"
                            style="width: 100%"
                        >
                          <a-select-option
                              v-for="field in formFields"
                              :key="field.fieldKey"
                              :value="field.fieldKey"
                          >
                            {{ field.fieldName }}
                          </a-select-option>
                        </a-select>
                      </a-col>
                      <a-col :span="10">
                        <FormField
                            v-if="condition.fieldKey"
                            :field="getFieldByDataIndex(condition.fieldKey)"
                            :placeholder="getFieldByDataIndex(condition.fieldKey).fieldName"
                            :value="condition.searchValue"
                            size="small"
                            @update:value="val => condition.searchValue = val"
                        />
                        <a-input
                            v-else
                            v-model:value="condition.searchValue"
                            placeholder="搜索值"
                            size="small"
                        />
                      </a-col>
                      <a-col :span="4">
                        <a-button danger size="small" @click="removeSearchCondition(index)">
                          删除
                        </a-button>
                      </a-col>
                    </a-row>
                  </div>
                </a-form-item>
              </div>

              <a-form-item>
                <a-button style="margin-right: 8px;" type="primary" @click="searchData">搜索</a-button>
                <a-button @click="resetSearch">重置</a-button>
              </a-form-item>

              <a-form-item>
                <a-button style="margin-right: 8px;" @click="refreshData">刷新</a-button>
                <a-button type="primary" @click="showDataForm">新增数据</a-button>
                <a-button style="margin-left: 8px;" @click="exportToExcel">导出Excel</a-button>
              </a-form-item>
            </a-card>
          </a-col>

          <!-- 右侧数据列表区域 -->
          <a-col :span="18">
            <a-card size="small" :title="`${formConfig?.formDescription}数据列表 `">
              <a-table
                  :columns="columns"
                  :data-source="dataList"
                  :loading="loading"
                  :pagination="pagination"
                  rowKey="id"
                  size="small"
                  @change="handleTableChange"
              >
                <!-- 自定义列渲染 -->
                <template #bodyCell="{ column, record }">
                  <template v-if="column.dataIndex === 'action'">
                    <a-button type="link" @click="editData(record)">编辑</a-button>
                    <a-button danger type="link" @click="deleteData(record)">删除</a-button>
                  </template>
                  <template v-else>
                    <!-- 根据字段类型渲染不同的显示格式 -->
                    <template v-if="getFieldByDataIndex(column.dataIndex).fieldType === 'date'">
                      {{ formatDate(record[column.dataIndex], getFieldByDataIndex(column.dataIndex)) }}
                    </template>
                    <template v-else-if="getFieldByDataIndex(column.dataIndex).fieldType === 'checkbox'">
                      {{ formatCheckboxValue(record[column.dataIndex], getFieldByDataIndex(column.dataIndex)) }}
                    </template>
                    <template
                        v-else-if="getFieldByDataIndex(column.dataIndex).fieldType === 'select' || getFieldByDataIndex(column.dataIndex).fieldType === 'radio-field'">
                      {{ formatSelectValue(record[column.dataIndex], getFieldByDataIndex(column.dataIndex)) }}
                    </template>
                    <template v-else>
                      {{ formatGenericValue(record[column.dataIndex]) }}
                    </template>
                  </template>
                </template>
              </a-table>
            </a-card>
          </a-col>
        </a-row>
      </a-card>

      <!-- 数据编辑模态框 -->
      <a-modal
          v-model:open="modalVisible"
          :title="editMode ? '编辑数据' : '新增数据'"
          width="600px"
          @cancel="cancelModal"
          @ok="saveData"
      >
        <a-form layout="vertical">
          <a-form-item
              v-for="field in formFields"
              :key="field.fieldKey"
              :label="field.fieldName"
              :required="field.isRequired === 1"
          >
            <FormField
                :field="field"
                :placeholder="field.fieldName"
                :value="currentData[field.fieldKey]"
                @update:value="val => updateCurrentData(field.fieldKey, val)"
            />
          </a-form-item>
        </a-form>
      </a-modal>
    </a-layout-content>
  </a-layout>
</template>

<script>
import {getFormConfigByFormKey, getFormConfigVersions} from '../api/formConfig';
import {getFieldsByFormKey, getFieldsByFormKeyAndVersion} from '../api/formField';
import {
  deleteFormData,
  exportFormDataWithMultipleConditions,
  saveFormData,
  pageFormDataWithMultipleConditions,
  updateFormData
} from '../api/formData';
import {message, Modal} from 'ant-design-vue';
import DetailComponent from '../components/DetailComponent.vue';
import {formFieldComponents} from '../components/form-fields';
import FormField from '../components/FormField.vue';
import dayjs from 'dayjs';
import localizedFormat from 'dayjs/plugin/localizedFormat';

dayjs.extend(localizedFormat);

export default {
  name: 'FormDataManage',
  components: {
    DetailComponent,
    FormField,
    ...formFieldComponents
  },
  props: {
    formKey: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      formConfig: null,
      formFields: [],
      formVersions: [],
      selectedVersion: null, // 默认不选择特定版本
      dataList: [],
      loading: false,
      pagination: {
        current: 1,
        pageSize: 10,
        total: 110,
        pageSizeOptions: ['10', '20', '30', '50', '100'], // 可选择的每页显示记录数
        showTotal: (total, range) => `${range[0]}-${range[1]} 共${total}条`, // 总记录数显示
        showQuickJumper: true, // 是否显示快速跳转
        showSizeChanger: true, // 是否可以改变 pageSize
      },
      modalVisible: false,
      editMode: false,
      currentData: {},
      currentDataId: null,
      searchForm: {
        field: '',
        value: undefined // 改为 undefined 而不是空字符串
      },
      // 搜索模式：single-单条件搜索，multiple-多条件搜索
      searchMode: 'single',
      // 多条件搜索条件列表
      multipleSearchConditions: [
        {fieldKey: '', searchValue: ''}
      ]
    };
  },
  computed: {
    columns() {
      const columns = this.formFields.map(field => {
        return {
          title: field.fieldName,
          dataIndex: field.fieldKey,
          key: field.fieldKey
        };
      });

      // 添加操作列
      columns.push({
        title: '操作',
        dataIndex: 'action',
        key: 'action',
        width: 150
      });

      return columns;
    }
  },
  mounted() {
    this.loadFormConfig();
    this.loadFormVersions();
    this.loadDataPage();
  },
  methods: {
    async loadFormConfig() {
      try {
        // 获取表单配置
        const formResponse = await getFormConfigByFormKey(this.formKey);
        this.formConfig = formResponse.data.data;

        // 获取字段配置（默认获取最新版本）
        await this.loadFormFields();
      } catch (error) {
        console.error('加载表单配置失败:', error);
        message.error('加载表单配置失败');
      }
    },
    async loadFormFields() {
      try {
        let fieldResponse;
        if (this.selectedVersion) {
          // 获取指定版本的字段配置
          fieldResponse = await getFieldsByFormKeyAndVersion(this.formKey, this.selectedVersion);
        } else {
          // 获取最新版本的字段配置
          fieldResponse = await getFieldsByFormKey(this.formKey);
        }
        this.formFields = fieldResponse.data.data || [];
      } catch (error) {
        console.error('加载字段配置失败:', error);
        message.error('加载字段配置失败');
      }
    },
    async loadFormVersions() {
      try {
        // 获取表单所有版本
        const response = await getFormConfigVersions(this.formKey);
        if (response.data.data) {
          this.formVersions = response.data.data;

          // 默认不选择特定版本（显示全部版本数据）
          this.selectedVersion = null;
        } else {
          message.error('获取表单版本失败');
        }
      } catch (error) {
        console.error('加载表单版本失败:', error);
      }
    },
    async loadDataPage() {
      this.loading = true;
      try {
        const validConditions = []
        if (this.searchMode === 'single') {
          validConditions.push({
            fieldKey: this.searchForm.field,
            searchValue: this.searchForm.value
          });
        } else {
          validConditions.push(...this.multipleSearchConditions);
        }

        let response = await pageFormDataWithMultipleConditions(this.formKey, this.selectedVersion, validConditions.filter(condition =>
            condition.fieldKey && condition.searchValue !== undefined && condition.searchValue !== ''
        ), this.pagination.current, this.pagination.pageSize);
        console.log('response', response.data?.data);
        this.dataList = response.data?.data?.records || [];
        this.pagination.total = response.data?.data?.total || 0;
        console.log('this.pagination', this.pagination);

      } catch (error) {
        console.error('加载数据列表失败:', error);
        message.error('加载数据列表失败');
      } finally {
        this.loading = false;
      }
    },
    showDataForm() {
      this.editMode = false;
      // 初始化currentData并设置字段默认值
      this.currentData = {};
      for (const field of this.formFields) {
        // 获取字段的默认值
        const fieldProperties = this.parseFieldProperties(field);
        if (fieldProperties.defaultValue !== undefined) {
          // 特殊处理复选框默认值
          if (field.fieldType === 'checkbox-field' || field.fieldType === 'checkbox') {
            // 复选框默认值应该是数组
            if (Array.isArray(fieldProperties.defaultValue)) {
              this.currentData[field.fieldKey] = fieldProperties.defaultValue;
            } else if (typeof fieldProperties.defaultValue === 'string') {
              // 如果是逗号分隔的字符串，转换为数组
              this.currentData[field.fieldKey] = fieldProperties.defaultValue.split(',').map(v => v.trim()).filter(v => v);
            } else {
              this.currentData[field.fieldKey] = [fieldProperties.defaultValue];
            }
          } else {
            // 其他字段类型直接使用默认值
            this.currentData[field.fieldKey] = fieldProperties.defaultValue;
          }
        }
      }
      this.currentDataId = null;
      this.modalVisible = true;
    },
    editData(record) {
      this.editMode = true;
      // 深拷贝record以避免直接修改原始数据
      this.currentData = JSON.parse(JSON.stringify(record));
      this.currentDataId = record.id;
      this.modalVisible = true;
    },
    async saveData() {
      try {
        // 创建一个新对象，确保只发送表单字段数据
        const formData = {};
        for (const field of this.formFields) {
          if (this.currentData.hasOwnProperty(field.fieldKey)) {
            formData[field.fieldKey] = this.currentData[field.fieldKey];
          }
        }

        let response;

        if (this.editMode) {
          // 更新数据
          response = await updateFormData(this.formKey, this.currentDataId, formData);
        } else {
          // 新增数据
          response = await saveFormData(this.formKey, formData);
        }

        if (response.data.data) {
          message.success(this.editMode ? '更新成功' : '保存成功');
          this.modalVisible = false;
          this.loadDataPage();
        } else {
          message.error(this.editMode ? '更新失败' : '保存失败');
        }
      } catch (error) {
        console.error('保存数据失败:', error);
        message.error('保存数据失败');
      }
    },
    cancelModal() {
      this.modalVisible = false;
    },
    deleteData(record) {
      Modal.confirm({
        title: '确认删除',
        content: '确定要删除这条数据吗？此操作不可恢复！',
        onOk: async () => {
          try {
            const response = await deleteFormData(this.formKey, record.id);
            if (response.data.data) {
              message.success('删除成功');
              this.loadDataPage();
            } else {
              message.error('删除失败');
            }
          } catch (error) {
            console.error('删除数据失败:', error);
            message.error('删除数据失败');
          }
        }
      });
    },
    handleTableChange(newPagination, filters, sorter) {
      Object.assign(this.pagination, newPagination);
      console.log('pagination', this.pagination);
      this.loadDataPage();
    },
    searchData() {
      this.pagination.current = 1;
      this.loadDataPage();
    },

    resetSearch() {
      if (this.searchMode === 'single') {
        this.searchForm.field = '';
        this.searchForm.value = undefined;
      } else {
        this.multipleSearchConditions = [
          {fieldKey: '', searchValue: ''}
        ];
      }
      this.pagination.current = 1;
      this.loadDataPage();
    },

    // 添加搜索条件
    addSearchCondition() {
      this.multipleSearchConditions.push({
        fieldKey: '',
        searchValue: ''
      });
    },

    // 删除搜索条件
    removeSearchCondition(index) {
      if (this.multipleSearchConditions.length > 1) {
        this.multipleSearchConditions.splice(index, 1);
      } else {
        // 如果只剩一个条件，重置它
        this.multipleSearchConditions[0] = {
          fieldKey: '',
          searchValue: ''
        };
      }
    },

    // 搜索模式改变时的处理
    onSearchModeChange() {
      // 切换搜索模式时重置搜索条件
      if (this.searchMode === 'single') {
        this.searchForm.field = '';
        this.searchForm.value = undefined;
      } else {
        this.multipleSearchConditions = [
          {fieldKey: '', searchValue: ''}
        ];
      }
    },
    refreshData() {
      this.loadDataPage();
    },

    // 导出当前版本数据为Excel
    exportToExcel() {
      try {

        const validConditions = []
        if (this.searchMode === 'single') {
          validConditions.push({
            fieldKey: this.searchForm.field,
            searchValue: this.searchForm.value
          });
        } else {
          validConditions.push(...this.multipleSearchConditions);
        }
        // 多条件搜索模式
        exportFormDataWithMultipleConditions(
            this.formKey,
            this.selectedVersion,
            this.multipleSearchConditions
        );
      } catch (error) {
        console.error('导出Excel失败:', error);
        message.error('导出Excel失败');
      }
    },

    getFieldByDataIndex(dataIndex) {
      return this.formFields.find(field => field.fieldKey === dataIndex) || {};
    },
    // 获取字段值
    getFieldValue(fieldKey) {
      return this.currentData[fieldKey];
    },
    // 更新当前数据
    updateCurrentData(fieldKey, value) {
      // 在Vue 3中，直接赋值即可，无需使用this.$set
      this.currentData[fieldKey] = value;
    },
    // 设置字段值
    setFieldValue(fieldKey, value) {
      // 特殊处理复选框，确保值是数组
      if (this.getFieldByDataIndex(fieldKey).fieldType === 'checkbox-field') {
        if (!Array.isArray(value)) {
          // 如果不是数组，可能是单个值或者是逗号分隔的字符串
          if (typeof value === 'string' && value.includes(',')) {
            value = value.split(',').map(v => v.trim()).filter(v => v);
          } else if (value !== undefined && value !== null) {
            value = [value];
          } else {
            value = [];
          }
        }
      }

      // 在Vue 3中，直接赋值即可，无需使用this.$set
      this.currentData[fieldKey] = value;
    },
    // 转换值类型以匹配选项值
    convertValueType(value) {
      // 尝试转换为数字，如果失败则保持为字符串
      if (typeof value === 'string' && !isNaN(value) && !isNaN(parseFloat(value))) {
        return parseFloat(value);
      }
      return value;
    },
    // 获取选中的字段
    getSelectedField() {
      if (!this.searchForm.field) return null;
      return this.formFields.find(field => field.fieldKey === this.searchForm.field);
    },
    // 获取搜索字段
    getSearchField() {
      if (!this.searchForm.field) {
        return {
          fieldType: 'input-field',
          fieldName: '搜索值'
        };
      }

      const field = this.formFields.find(f => f.fieldKey === this.searchForm.field);
      return field || {
        fieldType: 'input-field',
        fieldName: '搜索值'
      };
    },
    // 获取搜索框占位符
    getSearchPlaceholder() {
      const selectedField = this.getSelectedField();
      if (!selectedField) {
        return '请输入搜索值';
      }

      // 根据字段类型设置占位符
      const placeholderMap = {
        'input-field': '请输入文本',
        'textarea-field': '请输入文本',
        'select-field': '请选择选项',
        'radio-field': '请选择选项',
        'checkbox-field': '请选择选项',
        'date-field': '请选择日期'
      };

      return placeholderMap[selectedField.fieldType] || '请输入搜索值';
    },
    // 搜索字段改变时的处理
    onSearchFieldChange() {
      // 清空搜索值
      this.searchForm.value = undefined;
    },
    // 解析字段属性
    parseFieldProperties(field) {
      if (!field.fieldProperties) {
        return {};
      }

      try {
        return JSON.parse(field.fieldProperties);
      } catch (error) {
        console.error('解析字段属性失败:', error);
        return {};
      }
    },
    // 版本改变时的处理
    async onVersionChange() {
      await this.loadFormFields();
      this.loadDataPage();
    },
    parseFieldOptions(optionsStr) {
      if (!optionsStr) return [];

      try {
        const options = [];
        const lines = optionsStr.split('\n');

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
    // 格式化日期显示
    formatDate(dateString, field) {
      if (!dateString) return '';
      try {
        // 获取字段属性中的日期格式配置
        const fieldProperties = this.parseFieldProperties(field);
        const displayFormat = fieldProperties.dateFormat || 'YYYY-MM-DD';
        const dateType = fieldProperties.dateType || 'date';

        // 处理不同格式的日期字符串
        let date;

        // 如果是 dayjs 对象
        if (dateString && dateString.$d) {
          date = dayjs(dateString.$d);
        }
        // 如果已经是Date对象
        else if (dateString instanceof Date) {
          date = dayjs(dateString);
        }
        // 如果是ISO格式的字符串 (2023-01-01T00:00:00.000Z)
        else if (typeof dateString === 'string' && dateString.includes('T')) {
          date = dayjs(dateString);
        }
        // 如果是日期字符串 (2023-01-01)
        else if (typeof dateString === 'string' && /^\d{4}-\d{2}-\d{2}/.test(dateString)) {
          date = dayjs(dateString);
        }
        // 如果是时间戳
        else if (typeof dateString === 'number') {
          date = dayjs(dateString);
        }
        // 其他情况尝试直接创建dayjs对象
        else {
          date = dayjs(dateString);
        }

        // 检查日期是否有效
        if (!date.isValid()) {
          return String(dateString);
        }

        // 根据日期类型和格式进行格式化
        switch (dateType) {
          case 'datetime':
            // 日期时间格式
            return date.format('YYYY-MM-DD HH:mm:ss');

          case 'week':
            // 周格式
            return date.format('YYYY-WW');

          case 'month':
            // 月格式
            return date.format('YYYY-MM');

          case 'quarter':
            // 季度格式
            return date.format('YYYY-Q');

          case 'year':
            // 年格式
            return date.format('YYYY');

          default:
            // 使用字段配置的格式或默认格式
            return date.format(displayFormat);
        }
      } catch (error) {
        console.error('日期格式化失败:', error);
        return String(dateString);
      }
    },
    // 获取周数
    getWeekNumber(date) {
      const firstDayOfYear = new Date(date.getFullYear(), 0, 1);
      const pastDaysOfYear = (date - firstDayOfYear) / 86400000;
      return Math.ceil((pastDaysOfYear + firstDayOfYear.getDay() + 1) / 7);
    },

    formatCheckboxValue(value, field) {
      if (!value || !field) return '';
      const options = this.parseFieldOptions(field.fieldOptions);

      // 处理不同类型的值
      let selectedOptions = [];
      if (Array.isArray(value)) {
        selectedOptions = value;
      } else if (typeof value === 'string') {
        // 如果是逗号分隔的字符串，分割成数组
        selectedOptions = value.split(',').map(v => v.trim()).filter(v => v);
      } else {
        selectedOptions = [value];
      }

      const labels = selectedOptions.map(val => {
        const option = options.find(opt => opt.value == val); // 使用 == 而不是 === 以支持类型转换
        return option ? option.label : val;
      });
      return labels.join(', ');
    },
    formatSelectValue(value, field) {
      if (!value || !field) return value;
      const options = this.parseFieldOptions(field.fieldOptions);
      const option = options.find(opt => opt.value == value); // 使用 == 而不是 === 以支持类型转换
      return option ? option.label : value;
    },
    formatGenericValue(value) {
      // 处理通用值的显示
      if (value === null || value === undefined) {
        return '';
      }
      if (Array.isArray(value)) {
        return value.join(', ');
      }
      return String(value);
    }
  }
};
</script>

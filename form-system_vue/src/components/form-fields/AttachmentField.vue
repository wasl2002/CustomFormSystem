<template>
  <a-upload
    v-model:file-list="fileList"
    v-bind="uploadProps"
    @change="handleChange"
  >
    <a-button>
      <upload-outlined></upload-outlined>
      点击上传
    </a-button>
  </a-upload>
</template>

<script>
import { UploadOutlined } from '@ant-design/icons-vue';

export default {
  name: 'AttachmentField',
  components: {
    UploadOutlined
  },
  props: {
    field: {
      type: Object,
      required: true
    },
    value: {
      type: Array,
      default: () => []
    }
  },
  emits: ['update:value'],
  data() {
    return {
      fileList: this.value || []
    };
  },
  computed: {
    uploadProps() {
      const props = {
        action: '/api/form/data/upload', // 上传地址
        listType: 'picture',
        multiple: true
      };
      
      // 从字段属性中获取上传配置
      if (this.field.fieldProperties) {
        try {
          const fieldProperties = JSON.parse(this.field.fieldProperties);
          if (fieldProperties.maxFileSize) {
            props.maxFileSize = fieldProperties.maxFileSize * 1024 * 1024; // 转换为字节
          }
          if (fieldProperties.allowedTypes) {
            props.accept = fieldProperties.allowedTypes;
          }
        } catch (e) {
          console.error('解析字段属性失败:', e);
        }
      }
      
      return props;
    }
  },
  watch: {
    value: {
      handler(newVal) {
        this.fileList = newVal || [];
      },
      immediate: true
    }
  },
  methods: {
    handleChange({ file, fileList }) {
      this.fileList = fileList;
      this.$emit('update:value', fileList);
    }
  }
};
</script>
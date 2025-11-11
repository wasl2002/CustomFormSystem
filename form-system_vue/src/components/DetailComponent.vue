<template>
  <div class="detail-component">
    <div class="detail-header">
      <span class="detail-title">{{ title }}</span>
      <a-button 
        type="primary" 
        size="small" 
        @click="addRow"
        :disabled="isMaxRowsReached"
      >
        添加
      </a-button>
    </div>
    
    <div class="detail-rows">
      <div 
        v-for="(row, index) in rows" 
        :key="index" 
        class="detail-row"
      >
        <div class="row-content">
          <!-- 这里应该渲染子组件 -->
          <slot :row="row" :index="index"></slot>
        </div>
        <div class="row-actions">
          <a-button 
            type="text" 
            danger 
            size="small" 
            @click="removeRow(index)"
            :disabled="isMinRowsReached"
          >
            删除
          </a-button>
        </div>
      </div>
    </div>
    
    <div v-if="rows.length === 0" class="empty-detail">
      暂无数据，点击"添加"按钮添加新行
    </div>
  </div>
</template>

<script>
export default {
  name: 'DetailComponent',
  props: {
    title: {
      type: String,
      default: '明细'
    },
    minRows: {
      type: Number,
      default: 0
    },
    maxRows: {
      type: Number,
      default: 10
    },
    value: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      rows: this.value || []
    };
  },
  computed: {
    isMinRowsReached() {
      return this.rows.length <= this.minRows;
    },
    isMaxRowsReached() {
      return this.rows.length >= this.maxRows;
    }
  },
  watch: {
    value: {
      handler(newVal) {
        this.rows = newVal || [];
      },
      immediate: true
    }
  },
  methods: {
    addRow() {
      if (this.rows.length < this.maxRows) {
        const newRow = {};
        this.rows.push(newRow);
        this.$emit('input', this.rows);
      }
    },
    removeRow(index) {
      if (this.rows.length > this.minRows) {
        this.rows.splice(index, 1);
        this.$emit('input', this.rows);
      }
    }
  }
};
</script>

<style scoped>
.detail-component {
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  padding: 16px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.detail-title {
  font-weight: bold;
  font-size: 16px;
}

.detail-rows {
  margin-bottom: 16px;
}

.detail-row {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
  padding: 12px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
}

.row-content {
  flex: 1;
}

.row-actions {
  margin-left: 12px;
  padding-top: 4px;
}

.empty-detail {
  text-align: center;
  color: #999;
  padding: 24px;
  border: 1px dashed #d9d9d9;
  border-radius: 4px;
}
</style>
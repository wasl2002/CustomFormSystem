<template>
  <a-card title="表单列表">
    <template #extra>
      <a-button type="primary" @click="goToDesigner">新建表单</a-button>
    </template>
    <a-table
      :dataSource="formList"
      :columns="columns"
      :loading="loading"
      :pagination="pagination"
      @change="handleTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'status'">
          <a-tag :color="getStatusColor(record.status)">{{ getStatusText(record.status) }}</a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'version'">
          <span>v{{ record.formVersion }}</span>
          <a-tag v-if="record.isLatest === 1" color="green">最新</a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <a-space>
            <a-button type="link" @click="editForm(record)" :disabled="record.status === 3">编辑</a-button>
            <a-button type="link" @click="publishForm(record)" :disabled="record.status !== 1">发布</a-button>
            <a-button type="link" @click="disableForm(record)" :disabled="record.status !== 2">停用</a-button>
            <a-button type="link" @click="enableForm(record)" :disabled="record.status !== 3">启用</a-button>
            <a-button type="link" @click="viewVersions(record)">历史版本</a-button>
            <a-button type="link" danger @click="deleteForm(record)">删除</a-button>
            <a-button type="link" @click="manageData(record)">数据管理</a-button>
            <a-button type="link" @click="viewStatistics(record)">数据统计</a-button>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- 版本管理模态框 -->
    <a-modal
      v-model:open="versionModalVisible"
      title="表单历史版本管理"
      width="900px"
      @ok="closeVersionModal"
      @cancel="closeVersionModal"
    >
      <a-alert message="历史版本管理说明" type="info" show-icon style="margin-bottom: 16px;">
        <template #description>
          <p>• 最新版本：当前正在使用的表单版本</p>
          <p>• 历史版本：之前保存的表单版本，可用于回滚或查看</p>
          <p>• 回滚操作：将指定历史版本设为最新版本</p>
        </template>
      </a-alert>

      <a-table
        :dataSource="formVersions"
        :columns="versionColumns"
        :pagination="false"
        :rowKey="record => record.id"
        :rowClassName="record => record.isLatest === 1 ? 'latest-version-row' : ''"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'status'">
            <a-tag :color="getStatusColor(record.status)">{{ getStatusText(record.status) }}</a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'version'">
            <span>v{{ record.formVersion }}</span>
            <a-tag v-if="record.isLatest === 1" color="green">最新</a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'isLatest'">
            <a-tag v-if="record.isLatest === 1" color="green">是</a-tag>
            <span v-else>否</span>
          </template>
          <template v-else-if="column.dataIndex === 'createdTime'">
            {{ formatDateTime(record.createdTime) }}
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <a-space>
              <a-button
                type="link"
                @click="viewVersionConfig(record)"
              >
                查看配置
              </a-button>
              <a-button
                type="link"
                @click="rollbackToVersion(record)"
                :disabled="record.isLatest === 1"
                v-if="record.isLatest !== 1"
              >
                回滚到此版本
              </a-button>
              <a-button
                type="link"
                @click="viewVersionData(record)"
              >
                查看数据
              </a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-modal>
  </a-card>
</template>

<script>
import { getAllFormConfigs, publishForm, disableForm, deleteFormConfig, getFormConfigVersions, rollbackToVersion, enableForm } from '../api/formConfig';
import { message, Modal } from 'ant-design-vue';

export default {
  name: 'FormList',
  data() {
    return {
      formList: [],
      formVersions: [],
      loading: false,
      versionModalVisible: false,
      currentFormKey: null,
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      columns: [
        {
          title: '表单名称',
          dataIndex: 'formName',
          key: 'formName'
        },
        {
          title: '表单描述',
          dataIndex: 'formDescription',
          key: 'formDescription'
        },
        {
          title: '版本',
          dataIndex: 'version',
          key: 'version'
        },
        {
          title: '状态',
          dataIndex: 'status',
          key: 'status'
        },
        {
          title: '创建时间',
          dataIndex: 'createdTime',
          key: 'createdTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          key: 'action'
        }
      ],
      versionColumns: [
        {
          title: '版本',
          dataIndex: 'version',
          key: 'version'
        },
        {
          title: '状态',
          dataIndex: 'status',
          key: 'status'
        },
        {
          title: '是否最新',
          dataIndex: 'isLatest',
          key: 'isLatest'
        },
        {
          title: '创建时间',
          dataIndex: 'createdTime',
          key: 'createdTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          key: 'action'
        }
      ]
    };
  },
  mounted() {
    this.loadFormList();
  },
  methods: {
    async loadFormList() {
      this.loading = true;
      try {
        const response = await getAllFormConfigs();
        // 只显示最新版本的表单
        this.formList = (response.data.data || []).filter(form => form.isLatest === 1);
        this.pagination.total = this.formList.length;
      } catch (error) {
        console.error('获取表单列表失败:', error);
        message.error('获取表单列表失败');
      } finally {
        this.loading = false;
      }
    },
    getStatusText(status) {
      const statusMap = {
        1: '草稿',
        2: '已发布',
        3: '已停用'
      };
      return statusMap[status] || '未知';
    },
    getStatusColor(status) {
      const colorMap = {
        1: 'orange',
        2: 'green',
        3: 'red'
      };
      return colorMap[status] || 'default';
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '';
      return new Date(dateTime).toLocaleString('zh-CN');
    },
    goToDesigner() {
      this.$router.push('/form/designer');
    },
    editForm(record) {
      this.$router.push(`/form/designer/${record.id}`);
    },
    publishForm(record) {
      Modal.confirm({
        title: '确认发布',
        content: `确定要发布表单"${record.formName}"吗？`,
        onOk: async () => {
          try {
            const response = await publishForm(record.id);
            if (response.data.data) {
              message.success('发布成功');
              this.loadFormList();
            } else {
              message.error('发布失败');
            }
          } catch (error) {
            console.error('发布表单失败:', error);
            message.error('发布表单失败');
          }
        }
      });
    },
    disableForm(record) {
      Modal.confirm({
        title: '确认停用',
        content: `确定要停用表单"${record.formName}"吗？`,
        onOk: async () => {
          try {
            const response = await disableForm(record.id);
            if (response.data.data) {
              message.success('停用成功');
              this.loadFormList();
            } else {
              message.error('停用失败');
            }
          } catch (error) {
            console.error('停用表单失败:', error);
            message.error('停用表单失败');
          }
        }
      });
    },
    enableForm(record) {
      Modal.confirm({
        title: '确认启用',
        content: `确定要启用表单"${record.formName}"吗？`,
        onOk: async () => {
          try {
            const response = await enableForm(record.id);
            if (response.data.data) {
              message.success('启用成功');
              this.loadFormList();
            } else {
              message.error('启用失败');
            }
          } catch (error) {
            console.error('启用表单失败:', error);
            message.error('启用表单失败');
          }
        }
      });
    },
    deleteForm(record) {
      Modal.confirm({
        title: '确认删除',
        content: `确定要删除表单"${record.formName}"吗？此操作不可恢复！`,
        onOk: async () => {
          try {
            const response = await deleteFormConfig(record.id);
            if (response.data.data) {
              message.success('删除成功');
              this.loadFormList();
            } else {
              message.error('删除失败');
            }
          } catch (error) {
            console.error('删除表单失败:', error);
            message.error('删除表单失败');
          }
        }
      });
    },
    manageData(record) {
      this.$router.push(`/form/data/${record.formKey}`);
    },
    viewStatistics(record) {
      this.$router.push(`/form/statistics/${record.formKey}`);
    },
    handleTableChange(pagination) {
      this.pagination.current = pagination.current;
      this.loadFormList();
    },
    // 查看版本
    async viewVersions(record) {
      try {
        const response = await getFormConfigVersions(record.formKey);
        if (response.data.data) {
          this.currentFormKey = record.formKey;
          this.formVersions = response.data.data;
          this.versionModalVisible = true;
        } else {
          message.error('获取版本列表失败');
        }
      } catch (error) {
        console.error('获取版本列表失败:', error);
        message.error('获取版本列表失败');
      }
    },
    // 关闭版本模态框
    closeVersionModal() {
      this.versionModalVisible = false;
      this.formVersions = [];
      this.currentFormKey = null;
    },
    // 回滚到指定版本
    async rollbackToVersion(record) {
      Modal.confirm({
        title: '确认回滚',
        content: `确定要回滚到版本${record.formVersion}吗？回滚后将创建一个新的版本。`,
        onOk: async () => {
          try {
            const response = await rollbackToVersion(record.formKey, record.formVersion);
            if (response.data.data) {
              message.success('回滚成功');
              this.closeVersionModal();
              this.loadFormList();
            } else {
              message.error('回滚失败');
            }
          } catch (error) {
            console.error('回滚版本失败:', error);
            message.error('回滚版本失败');
          }
        }
      });
    },
    // 查看指定版本的数据
    viewVersionData(record) {
      this.$router.push(`/form/data/page/${record.formKey}?version=${record.formVersion}`);
    },
    // 查看指定版本的配置
    viewVersionConfig(record) {
      this.$router.push(`/form/designer/${record.id}?view=history`);
    }
  }
};
</script>

<style scoped>
.latest-version-row {
  background-color: #f6ffed;
}
</style>

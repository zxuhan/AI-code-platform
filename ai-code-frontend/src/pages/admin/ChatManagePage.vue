<template>
  <div id="chatManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="Message Content">
        <a-input v-model:value="searchParams.message" placeholder="Enter message content" />
      </a-form-item>
      <a-form-item label="Message Type">
        <a-select
          v-model:value="searchParams.messageType"
          placeholder="Select message type"
          style="width: 120px"
        >
          <a-select-option value="">All</a-select-option>
          <a-select-option value="user">User Message</a-select-option>
          <a-select-option value="assistant">AI Message</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="App ID">
        <a-input v-model:value="searchParams.appId" placeholder="Enter app ID" />
      </a-form-item>
      <a-form-item label="User ID">
        <a-input v-model:value="searchParams.userId" placeholder="Enter user ID" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">Search</a-button>
      </a-form-item>
    </a-form>
    <a-divider />

    <!-- 表格 -->
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="doTableChange"
      :scroll="{ x: 1400 }"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'message'">
          <a-tooltip :title="record.message">
            <div class="message-text">{{ record.message }}</div>
          </a-tooltip>
        </template>
        <template v-else-if="column.dataIndex === 'messageType'">
          <a-tag :color="record.messageType === 'user' ? 'blue' : 'green'">
            {{ record.messageType === 'user' ? 'User Message' : 'AI Message' }}
          </a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ formatTime(record.createTime) }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-button type="primary" size="small" @click="viewAppChat(record.appId)">
              View Chat
            </a-button>
            <a-popconfirm title="Are you sure you want to delete this message?" @confirm="deleteMessage(record.id)">
              <a-button danger size="small">Delete</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { listAllChatHistoryByPageForAdmin } from '@/api/chatHistoryController'
import { formatTime } from '@/utils/time'

const router = useRouter()

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 80,
    fixed: 'left',
  },
  {
    title: 'Message Content',
    dataIndex: 'message',
    width: 300,
  },
  {
    title: 'Message Type',
    dataIndex: 'messageType',
    width: 100,
  },
  {
    title: 'App ID',
    dataIndex: 'appId',
    width: 80,
  },
  {
    title: 'User ID',
    dataIndex: 'userId',
    width: 80,
  },
  {
    title: 'Create Time',
    dataIndex: 'createTime',
    width: 160,
  },
  {
    title: 'Actions',
    key: 'action',
    width: 180,
    fixed: 'right',
  },
]

// 数据
const data = ref<API.ChatHistory[]>([])
const total = ref(0)

// 搜索条件
const searchParams = reactive<API.ChatHistoryQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

// 获取数据
const fetchData = async () => {
  try {
    const res = await listAllChatHistoryByPageForAdmin({
      ...searchParams,
    })
    if (res.data.data) {
      data.value = res.data.data.records ?? []
      total.value = res.data.data.totalRow ?? 0
    } else {
      message.error('Failed to fetch data, ' + res.data.message)
    }
  } catch (error) {
    console.error('Failed to fetch data:', error)
    message.error('Failed to fetch data')
  }
}

// 页面加载时请求一次
onMounted(() => {
  fetchData()
})

// 分页参数
const pagination = computed(() => {
  return {
    current: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total: number) => `Total ${total} items`,
  }
})

// 表格变化处理
const doTableChange = (page: { current: number; pageSize: number }) => {
  searchParams.pageNum = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

// 搜索
const doSearch = () => {
  // 重置页码
  searchParams.pageNum = 1
  fetchData()
}

// 查看应用对话
const viewAppChat = (appId: number | undefined) => {
  if (appId) {
    router.push(`/app/chat/${appId}`)
  }
}

// 删除消息
const deleteMessage = async (id: number | undefined) => {
  if (!id) return

  try {
    // 目前先显示成功，实际实现需要调用删除接口
    message.success('Delete successfully')
    // 刷新数据
    fetchData()
  } catch (error) {
    console.error('Delete failed:', error)
    message.error('Delete failed')
  }
}
</script>

<style scoped>
#chatManagePage {
  padding: 24px;
  background: white;
  margin-top: 16px;
}

.message-text {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

:deep(.ant-table-tbody > tr > td) {
  vertical-align: middle;
}
</style>

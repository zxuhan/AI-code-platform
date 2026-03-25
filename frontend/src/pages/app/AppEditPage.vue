<template>
  <div id="appEditPage">
    <div class="page-header">
      <h1>Edit application</h1>
    </div>

    <div class="edit-container">
      <a-card title="Basic info" :loading="loading">
        <a-form
          :model="formData"
          :rules="rules"
          layout="vertical"
          @finish="handleSubmit"
          ref="formRef"
        >
          <a-form-item label="Application name" name="appName">
            <a-input
              v-model:value="formData.appName"
              placeholder="Application name"
              :maxlength="50"
              show-count
            />
          </a-form-item>

          <a-form-item
            v-if="isAdmin"
            label="Application cover"
            name="cover"
            extra="Use a public image URL; recommended size 400x300"
          >
            <a-input v-model:value="formData.cover" placeholder="Cover image URL" />
            <div v-if="formData.cover" class="cover-preview">
              <a-image
                :src="formData.cover"
                :width="200"
                :height="150"
                fallback="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="
              />
            </div>
          </a-form-item>

          <a-form-item v-if="isAdmin" label="Priority" name="priority" extra="99 means featured">
            <a-input-number
              v-model:value="formData.priority"
              :min="0"
              :max="99"
              style="width: 200px"
            />
          </a-form-item>

          <a-form-item label="Initial prompt" name="initPrompt">
            <a-textarea
              v-model:value="formData.initPrompt"
              placeholder="Initial prompt"
              :rows="4"
              :maxlength="1000"
              show-count
              disabled
            />
            <div class="form-tip">Initial prompt is read-only</div>
          </a-form-item>

          <a-form-item label="Generation type" name="codeGenType">
            <a-input
              :value="formatCodeGenType(formData.codeGenType)"
              placeholder="Generation type"
              disabled
            />
            <div class="form-tip">Generation type is read-only</div>
          </a-form-item>

          <a-form-item v-if="formData.deployKey" label="Deploy key" name="deployKey">
            <a-input v-model:value="formData.deployKey" placeholder="Deploy key" disabled />
            <div class="form-tip">Deploy key is read-only</div>
          </a-form-item>

          <a-form-item>
            <a-space>
              <a-button type="primary" html-type="submit" :loading="submitting">
                Save changes
              </a-button>
              <a-button @click="resetForm">Reset</a-button>
              <a-button type="link" @click="goToChat">Open chat</a-button>
            </a-space>
          </a-form-item>
        </a-form>
      </a-card>

      <!-- Application metadata -->
      <a-card title="Application info" style="margin-top: 24px">
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="App ID">
            {{ appInfo?.id }}
          </a-descriptions-item>
          <a-descriptions-item label="Creator">
            <UserInfo :user="appInfo?.user" size="small" />
          </a-descriptions-item>
          <a-descriptions-item label="Created at">
            {{ formatTime(appInfo?.createTime) }}
          </a-descriptions-item>
          <a-descriptions-item label="Updated at">
            {{ formatTime(appInfo?.updateTime) }}
          </a-descriptions-item>
          <a-descriptions-item label="Deployed at">
            {{ appInfo?.deployedTime ? formatTime(appInfo.deployedTime) : 'Not deployed' }}
          </a-descriptions-item>
          <a-descriptions-item label="Access URL">
            <a-button v-if="appInfo?.deployKey" type="link" @click="openPreview" size="small">
              Open preview
            </a-button>
            <span v-else>Not deployed</span>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser'
import { getAppVoById, updateApp, updateAppByAdmin } from '@/api/appController'
import { formatCodeGenType } from '@/utils/codeGenTypes'
import { formatTime } from '@/utils/time'
import UserInfo from '@/components/UserInfo.vue'
import { getStaticPreviewUrl } from '@/config/env'
import type { FormInstance } from 'ant-design-vue'

const route = useRoute()
const router = useRouter()
const loginUserStore = useLoginUserStore()

// Application info
const appInfo = ref<API.AppVO>()
const loading = ref(false)
const submitting = ref(false)
const formRef = ref<FormInstance>()

// Form data
const formData = reactive({
  appName: '',
  cover: '',
  priority: 0,
  initPrompt: '',
  codeGenType: '',
  deployKey: '',
})

// Whether the current user is an admin
const isAdmin = computed(() => {
  return loginUserStore.loginUser.userRole === 'admin'
})

// Form validation rules
const rules = {
  appName: [
    { required: true, message: 'Please enter the application name', trigger: 'blur' },
    { min: 1, max: 50, message: 'Application name must be 1-50 characters', trigger: 'blur' },
  ],
  cover: [{ type: 'url', message: 'Please enter a valid URL', trigger: 'blur' }],
  priority: [{ type: 'number', min: 0, max: 99, message: 'Priority must be 0-99', trigger: 'blur' }],
}

// Fetch application info
const fetchAppInfo = async () => {
  const id = route.params.id as string
  if (!id) {
    message.error('Application ID is missing')
    router.push('/')
    return
  }

  loading.value = true
  try {
    const res = await getAppVoById({ id: id as unknown as number })
    if (res.data.code === 0 && res.data.data) {
      appInfo.value = res.data.data

      // Authorization check
      if (!isAdmin.value && appInfo.value.userId !== loginUserStore.loginUser.id) {
        message.error('You are not authorized to edit this application')
        router.push('/')
        return
      }

      // Populate the form
      formData.appName = appInfo.value.appName || ''
      formData.cover = appInfo.value.cover || ''
      formData.priority = appInfo.value.priority || 0
      formData.initPrompt = appInfo.value.initPrompt || ''
      formData.codeGenType = appInfo.value.codeGenType || ''
      formData.deployKey = appInfo.value.deployKey || ''
    } else {
      message.error('Failed to fetch application info')
      router.push('/')
    }
  } catch (error) {
    console.error('Failed to fetch application info:', error)
    message.error('Failed to fetch application info')
    router.push('/')
  } finally {
    loading.value = false
  }
}

// Submit the form
const handleSubmit = async () => {
  if (!appInfo.value?.id) return

  submitting.value = true
  try {
    let res
    if (isAdmin.value) {
      // Admins can edit additional fields
      res = await updateAppByAdmin({
        id: appInfo.value.id,
        appName: formData.appName,
        cover: formData.cover,
        priority: formData.priority,
      })
    } else {
      // Regular users may only edit the application name
      res = await updateApp({
        id: appInfo.value.id,
        appName: formData.appName,
      })
    }

    if (res.data.code === 0) {
      message.success('Saved successfully')
      // Refetch the application info
      await fetchAppInfo()
    } else {
      message.error('Save failed: ' + res.data.message)
    }
  } catch (error) {
    console.error('Save failed:', error)
    message.error('Save failed')
  } finally {
    submitting.value = false
  }
}

// Reset the form
const resetForm = () => {
  if (appInfo.value) {
    formData.appName = appInfo.value.appName || ''
    formData.cover = appInfo.value.cover || ''
    formData.priority = appInfo.value.priority || 0
  }
  formRef.value?.clearValidate()
}

// Open the chat page
const goToChat = () => {
  if (appInfo.value?.id) {
    router.push(`/app/chat/${appInfo.value.id}`)
  }
}

// Open the preview
const openPreview = () => {
  if (appInfo.value?.codeGenType && appInfo.value?.id) {
    const url = getStaticPreviewUrl(appInfo.value.codeGenType, String(appInfo.value.id))
    window.open(url, '_blank')
  }
}

// Initial load
onMounted(() => {
  fetchAppInfo()
})
</script>

<style scoped>
#appEditPage {
  padding: 24px;
  max-width: 1000px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.edit-container {
  border-radius: 8px;
}

.cover-preview {
  margin-top: 12px;
  padding: 12px;
  border: 1px solid #e8e8e8;
  border-radius: 6px;
  background: #fafafa;
}

.form-tip {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

:deep(.ant-card-head) {
  background: #fafafa;
}

:deep(.ant-descriptions-item-label) {
  background: #fafafa;
  font-weight: 500;
}
</style>

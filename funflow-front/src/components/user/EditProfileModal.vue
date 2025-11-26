<template>
  <el-dialog
    v-model="dialogVisible"
    title="修改个人信息"
    width="500px"
    :before-close="handleClose"
    class="edit-profile-dialog"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
      class="edit-form"
    >
      <el-form-item label="头像" prop="avatar">
        <div class="avatar-upload">
          <el-avatar
            :size="80"
            :src="form.avatar"
            :icon="UserFilled"
            class="avatar-preview"
          />
          <el-button
            type="primary"
            size="small"
            @click="handleAvatarClick"
            class="upload-btn"
          >
            更换头像
          </el-button>
          <input
            ref="avatarInput"
            type="file"
            accept="image/*"
            @change="handleAvatarChange"
            style="display: none"
          />
        </div>
      </el-form-item>

      <el-form-item label="账号" prop="username">
        <el-input
          v-model="form.username"
          placeholder="请输入账号"
          maxlength="20"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="昵称" prop="nickname">
        <el-input
          v-model="form.nickname"
          placeholder="请输入昵称"
          maxlength="20"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="个人简介" prop="bio">
        <el-input
          v-model="form.bio"
          type="textarea"
          :rows="4"
          placeholder="介绍一下自己吧～"
          maxlength="200"
          show-word-limit
          resize="none"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">
          保存
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'

interface Props {
  modelValue: boolean
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const authStore = useAuthStore()
const formRef = ref<FormInstance>()
const avatarInput = ref<HTMLInputElement>()
const loading = ref(false)

const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const form = reactive({
  avatar: '',
  username: '',
  nickname: '',
  bio: ''
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度在 3 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '账号只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  bio: [
    { max: 200, message: '个人简介不能超过 200 个字符', trigger: 'blur' }
  ]
}

// 监听弹窗打开，初始化表单数据
watch(dialogVisible, (visible) => {
  if (visible && authStore.user) {
    form.avatar = authStore.user.avatar || ''
    form.username = authStore.user.username || ''
    form.nickname = authStore.user.nickname || ''
    form.bio = authStore.user.bio || ''
  }
})

const handleAvatarClick = () => {
  avatarInput.value?.click()
}

const handleAvatarChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]

  if (file) {
    // 检查文件大小（限制为5MB）
    if (file.size > 5 * 1024 * 1024) {
      ElMessage.error('头像大小不能超过5MB')
      return
    }

    // 检查文件类型
    if (!file.type.startsWith('image/')) {
      ElMessage.error('请选择图片文件')
      return
    }

    // 创建预览URL
    const reader = new FileReader()
    reader.onload = (e) => {
      form.avatar = e.target?.result as string
    }
    reader.readAsDataURL(file)
  }
}

const handleClose = () => {
  dialogVisible.value = false
  // 重置表单
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    // TODO: 调用更新用户信息API
    // await updateUserProfile(form)

    // 更新store中的用户信息
    if (authStore.user) {
      authStore.setUser({
        ...authStore.user,
        avatar: form.avatar,
        username: form.username,
        nickname: form.nickname,
        bio: form.bio
      })
    }

    ElMessage.success('个人信息修改成功')
    handleClose()
  } catch (error) {
    console.error('修改个人信息失败:', error)
    ElMessage.error('修改失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.edit-profile-dialog {
  --el-dialog-bg-color: #1a1a1a;
  --el-dialog-border-color: #2b2d38;
  --el-text-color-primary: #fff;
  --el-text-color-regular: #ddd;
  --el-border-color: #2b2d38;
  --el-fill-color-blank: #1a1a1a;
  --el-fill-color-light: #2a2a2a;
}

:deep(.el-dialog) {
  background: #1a1a1a;
  border: 1px solid #2b2d38;
}

:deep(.el-dialog__header) {
  border-bottom: 1px solid #2b2d38;
  padding: 20px 24px;
}

:deep(.el-dialog__title) {
  color: #fff;
  font-weight: 600;
}

:deep(.el-dialog__body) {
  padding: 24px;
}

:deep(.el-dialog__footer) {
  border-top: 1px solid #2b2d38;
  padding: 16px 24px;
}

.edit-form {
  margin-top: 0;
}

:deep(.el-form-item__label) {
  color: #ddd;
  font-weight: 500;
}

:deep(.el-input__wrapper) {
  background-color: #2a2a2a;
  border: 1px solid #2b2d38;
  box-shadow: none;
}

:deep(.el-input__wrapper:hover) {
  border-color: #667eea;
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 1px rgba(102, 126, 234, 0.2);
}

:deep(.el-input__inner) {
  color: #fff;
}

:deep(.el-textarea__inner) {
  background-color: #2a2a2a;
  border: 1px solid #2b2d38;
  color: #fff;
  resize: none;
}

:deep(.el-textarea__inner:hover) {
  border-color: #667eea;
}

:deep(.el-textarea__inner:focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 1px rgba(102, 126, 234, 0.2);
}

.avatar-upload {
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar-preview {
  border: 2px solid #2b2d38;
  flex-shrink: 0;
}

.upload-btn {
  flex-shrink: 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-button) {
  border: 1px solid #2b2d38;
}

:deep(.el-button--default) {
  background-color: #2a2a2a;
  color: #ddd;
  border-color: #2b2d38;
}

:deep(.el-button--default:hover) {
  background-color: #333;
  border-color: #667eea;
  color: #fff;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #fe2c55 0%, #ff6b6b 100%);
  border: none;
}

:deep(.el-button--primary:hover) {
  opacity: 0.9;
  transform: translateY(-1px);
}
</style>
<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import { getCaptcha, login, type LoginData } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'
import { ElMessage, ElDialog, ElForm, ElFormItem, ElInput, ElButton } from 'element-plus'

const props = defineProps<{
  show: boolean
  forced?: boolean // 是否为强制登录模式
}>()

const emit = defineEmits(['close', 'switch-to-register'])

const authStore = useAuthStore()
const loading = ref(false)
const formRef = ref()
const formData = reactive<LoginData>({
  email: '',
  password: '',
  captchaId: '',
  captchaText: ''
})

const captchaImage = ref('')
const captchaLoading = ref(false)

const fetchCaptcha = async () => {
  try {
    captchaLoading.value = true
    captchaImage.value = '' // 清空旧图片
    formData.captchaId = ''
    formData.captchaText = '' // 清空用户输入的验证码
    
    const res = await getCaptcha()
    if (res.code === 200) {
      formData.captchaId = res.data.captchaId
      captchaImage.value = res.data.imageData
    } else {
      ElMessage.error(res.message || '获取验证码失败，请重试')
    }
  } catch (error: any) {
    console.error('Failed to fetch captcha:', error)
    ElMessage.error(error.response?.data?.message || '获取验证码失败，请稍后重试')
  } finally {
    captchaLoading.value = false
  }
}

const handleClose = () => {
  // 强制登录模式下不允许关闭
  if (props.forced) {
    return
  }
  emit('close')
}

const switchToRegister = () => {
  emit('switch-to-register', props.forced) // 传递强制模式状态
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      if (loading.value) return
      loading.value = true
      try {
        const res = await login(formData)
        if (res.code === 200) {
          authStore.setToken(res.data.accessToken)
          // 登录成功后获取用户信息
          await authStore.fetchUserProfile()
          ElMessage.success('登录成功')
          // 登录成功后刷新验证码，为下次登录做准备
          fetchCaptcha()
          handleClose()
        } else {
          ElMessage.error(res.message || '登录失败，请检查用户名或密码')
          // 登录失败也刷新验证码
          fetchCaptcha()
        }
      } catch (error) {
        ElMessage.error('登录失败，请检查用户名或密码')
        fetchCaptcha()
      } finally {
        loading.value = false
      }
    }
  })
}

watch(() => props.show, (newVal) => {
  if (newVal) {
    fetchCaptcha()
    formData.password = ''
    formData.captchaText = ''
  }
})

const rules = {
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email' as const, message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  captchaText: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
}
</script>

<template>
  <el-dialog
    :model-value="show"
    @close="handleClose"
    width="380px"
    class="glass-dialog non-modal-dialog"
    :show-close="!forced"
    :append-to-body="true"
    destroy-on-close
    :modal="false"
    :close-on-click-modal="false"
    :close-on-press-escape="!forced"
    :z-index="700"
  >
    <template #header>
      <div class="modal-header">
        <h2 class="modal-title">欢迎回来</h2>
        <p class="modal-subtitle">登录 FunFlow 探索更多精彩</p>
      </div>
    </template>

    <div class="modal-body">
      <el-form 
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-position="top"
        @submit.prevent="handleLogin"
        size="large"
      >
        <el-form-item label="邮箱" prop="email">
          <el-input 
            v-model="formData.email" 
            placeholder="请输入邮箱地址" 
            clearable
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="formData.password" 
            type="password" 
            placeholder="请输入密码" 
            show-password
          />
        </el-form-item>

        <el-form-item label="图形验证码" prop="captchaText">
          <div class="captcha-row">
            <el-input 
              v-model="formData.captchaText" 
              placeholder="验证码" 
              class="captcha-input"
            />
            <div class="captcha-image-container" @click="fetchCaptcha" title="点击刷新验证码">
               <img :src="captchaImage" v-if="captchaImage && !captchaLoading" class="captcha-img" alt="captcha"/>
               <span v-else class="loading-text">{{ captchaLoading ? '加载中...' : '点击刷新' }}</span>
            </div>
          </div>
        </el-form-item>

        <el-button 
          type="primary" 
          class="primary-btn" 
          :loading="loading" 
          @click="handleLogin"
        >
          登录
        </el-button>

        <div class="switch-mode">
          还没有账号？<a class="switch-link" @click="switchToRegister">立即注册</a>
        </div>
      </el-form>
    </div>
  </el-dialog>
</template>

<style scoped>
.modal-header {
  text-align: center;
  padding-top: 20px;
}

.modal-title {
  font-size: 28px;
  font-weight: 800;
  margin-bottom: 8px;
  color: #fff;
  letter-spacing: -0.5px;
}

.modal-subtitle {
  font-size: 15px;
  color: #8a8b8e;
  margin: 0;
}

.modal-body {
  padding: 0 20px 20px;
}

.captcha-row {
  display: flex;
  gap: 12px;
  width: 100%;
}

.captcha-input {
  flex: 1;
}

.captcha-image-container {
  width: 120px;
  height: 40px;
  background: #2b2d38;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  flex-shrink: 0;
}

.captcha-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.loading-text {
  font-size: 12px;
  color: #8a8b8e;
}

.primary-btn {
  width: 100%;
  height: 50px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  margin-top: 10px;
  background: linear-gradient(92deg, #fe2c55 0%, #e02050 100%);
  border: none;
  transition: all 0.3s;
}

.primary-btn:hover {
  background: linear-gradient(92deg, #ff476e 0%, #f02a5b 100%);
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(254, 44, 85, 0.3);
}

.switch-mode {
  text-align: center;
  margin-top: 24px;
  font-size: 14px;
  color: #8a8b8e;
}

.switch-link {
  color: #fe2c55;
  text-decoration: none;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  margin-left: 4px;
}

.switch-link:hover {
  text-decoration: underline;
  color: #ff476e;
}

/* Customizing Element Plus Input for Dark Theme */
:deep(.el-input__wrapper) {
  background-color: rgba(255, 255, 255, 0.05);
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.08) inset;
  border-radius: 12px;
  padding: 8px 16px;
  transition: all 0.3s;
}

:deep(.el-input__wrapper.is-focus) {
  background-color: rgba(255, 255, 255, 0.1);
  box-shadow: 0 0 0 1px #fe2c55 inset !important;
}

:deep(.el-input__inner) {
  color: #fff;
  height: 32px;
}

:deep(.el-form-item__label) {
  color: #e4e6eb;
}
</style>

<style>
/* Override dialog wrapper for non-modal behavior */
:deep(.el-overlay-dialog) {
  z-index: 700 !important;
  pointer-events: none !important;
  background: transparent !important;
}

/* Make the dialog itself interactive */
:deep(.el-overlay-dialog .el-dialog) {
  pointer-events: auto !important;
  z-index: 701 !important;
}

/* Global override for this specific dialog class */
.glass-dialog.el-dialog {
  background: rgba(31, 33, 45, 0.95) !important;
  backdrop-filter: blur(20px);
  border-radius: 24px !important;
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4) !important;
}

/* Special style for non-modal dialog */
.non-modal-dialog {
  max-width: 380px !important;
}

.glass-dialog .el-dialog__headerbtn .el-dialog__close {
  color: #8a8b8e;
  font-size: 20px;
}

.glass-dialog .el-dialog__headerbtn:hover .el-dialog__close {
  color: #fff;
}

/* 在强制模式下隐藏关闭按钮 */
.glass-dialog .el-dialog__headerbtn {
  display: none !important;
}

/* 只有在非强制模式下显示关闭按钮 */
.glass-dialog:not(.forced) .el-dialog__headerbtn {
  display: block !important;
}
</style>

<script setup lang="ts">
import { ref, reactive, watch, computed } from 'vue'
import { getCaptcha, sendEmailCode, register } from '@/api/auth'
import { ElMessage, ElDialog, ElForm, ElFormItem, ElInput, ElButton } from 'element-plus'

const props = defineProps<{
  show: boolean
}>()

const emit = defineEmits(['close', 'switch-to-login'])

const loading = ref(false)
const sendingCode = ref(false)
const countdown = ref(0)
const captchaImage = ref('')
const captchaLoading = ref(false)
const formRef = ref()

const formData = reactive({
  email: '',
  captchaText: '',
  captchaId: '',
  emailCode: '',
  password: ''
})

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
      ElMessage.error(res.msg || '获取验证码失败，请重试')
    }
  } catch (error: any) {
    console.error('Failed to fetch captcha:', error)
    ElMessage.error(error.response?.data?.msg || '获取验证码失败，请稍后重试')
  } finally {
    captchaLoading.value = false
  }
}

const handleSendCode = async () => {
  if (!formData.email) {
    ElMessage.warning('请先输入邮箱地址')
    return
  }
  if (!formData.captchaText) {
    ElMessage.warning('请先输入图形验证码')
    return
  }
  
  sendingCode.value = true
  try {
    await sendEmailCode({
      email: formData.email,
      captchaId: formData.captchaId,
      captchaText: formData.captchaText
    })
    ElMessage.success('验证码已发送到您的邮箱')
    startCountdown()
  } catch (error: any) {
    ElMessage.error(error.response?.data?.msg || '发送失败，请重试')
    fetchCaptcha() // Refresh captcha on failure
    formData.captchaText = ''
  } finally {
    sendingCode.value = false
  }
}

const startCountdown = () => {
  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
}

const handleRegister = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      if (loading.value) return
      loading.value = true
      try {
        await register({
          email: formData.email,
          emailCode: formData.emailCode,
          password: formData.password
        })
        ElMessage.success('注册成功')
        emit('switch-to-login')
      } catch (error: any) {
        ElMessage.error(error.response?.data?.msg || '注册失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const handleClose = () => {
  emit('close')
}

// Password strength
const passwordStrength = computed(() => {
  const password = formData.password
  if (!password) return { score: 0, text: '', class: '' }
  
  let strength = 0
  if (password.length >= 8) strength++
  if (/[a-z]/.test(password)) strength++
  if (/[A-Z]/.test(password)) strength++
  if (/[0-9]/.test(password)) strength++
  if (/[^a-zA-Z0-9]/.test(password)) strength++
  
  if (strength <= 2) return { score: 1, text: '弱', class: 'weak' }
  if (strength <= 3) return { score: 2, text: '中等', class: 'medium' }
  return { score: 3, text: '强', class: 'strong' }
})

watch(() => props.show, (newVal) => {
  if (newVal) {
    fetchCaptcha()
    Object.assign(formData, {
      email: '',
      captchaText: '',
      emailCode: '',
      password: ''
    })
    countdown.value = 0
  }
})

const rules = {
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  captchaText: [
    { required: true, message: '请输入图形验证码', trigger: 'blur' }
  ],
  emailCode: [
    { required: true, message: '请输入邮箱验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度应为6位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, message: '密码长度至少8位', trigger: 'blur' }
  ]
}
</script>

<template>
  <el-dialog
    :model-value="show"
    @close="handleClose"
    width="440px"
    class="glass-dialog"
    align-center
    :show-close="true"
    :append-to-body="true"
    destroy-on-close
  >
    <template #header>
      <div class="modal-header">
        <h2 class="modal-title">加入 FunFlow</h2>
        <p class="modal-subtitle">创建账号，开启创作之旅</p>
      </div>
    </template>

    <div class="modal-body">
      <el-form 
        ref="formRef" 
        :model="formData" 
        :rules="rules" 
        label-position="top"
        @submit.prevent="handleRegister"
        size="large"
      >
        <el-form-item label="邮箱" prop="email">
          <el-input 
            v-model="formData.email" 
            placeholder="请输入邮箱地址" 
            clearable
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

        <el-form-item label="邮箱验证码" prop="emailCode">
          <div class="captcha-row">
            <el-input 
              v-model="formData.emailCode" 
              placeholder="6位验证码" 
              maxlength="6" 
              class="captcha-input"
            />
            <el-button 
              type="default" 
              class="send-code-btn"
              :disabled="sendingCode || countdown > 0" 
              @click="handleSendCode"
            >
              {{ countdown > 0 ? `${countdown}s` : '发送验证码' }}
            </el-button>
          </div>
        </el-form-item>

        <el-form-item label="设置密码" prop="password">
          <el-input 
            v-model="formData.password" 
            type="password" 
            placeholder="8-32位，包含字母和数字" 
            show-password
          />
          <div class="password-strength-container" v-if="formData.password">
            <div class="strength-bars">
              <div class="bar" :class="{ active: passwordStrength.score >= 1, [passwordStrength.class]: true }"></div>
              <div class="bar" :class="{ active: passwordStrength.score >= 2, [passwordStrength.class]: true }"></div>
              <div class="bar" :class="{ active: passwordStrength.score >= 3, [passwordStrength.class]: true }"></div>
            </div>
            <span class="strength-text" :class="passwordStrength.class">{{ passwordStrength.text }}</span>
          </div>
        </el-form-item>

        <el-button 
          type="primary" 
          class="primary-btn" 
          :loading="loading" 
          @click="handleRegister"
        >
          注册
        </el-button>

        <div class="switch-mode">
          已有账号？<a class="switch-link" @click="$emit('switch-to-login')">立即登录</a>
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

.send-code-btn {
  width: 120px;
  height: 40px;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: #fff;
  border-radius: 12px;
}
.send-code-btn:hover {
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
}
.send-code-btn:disabled {
  background: rgba(255, 255, 255, 0.05);
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

/* Password Strength */
.password-strength-container {
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.strength-bars {
  display: flex;
  gap: 4px;
  flex: 1;
}

.bar {
  height: 4px;
  flex: 1;
  background: #2b2d38;
  border-radius: 2px;
  transition: all 0.3s;
}

.bar.active.weak { background: #ff4c4c; }
.bar.active.medium { background: #ffb938; }
.bar.active.strong { background: #00d084; }

.strength-text {
  font-size: 12px;
  font-weight: 500;
  width: 30px;
  text-align: right;
}

.strength-text.weak { color: #ff4c4c; }
.strength-text.medium { color: #ffb938; }
.strength-text.strong { color: #00d084; }

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

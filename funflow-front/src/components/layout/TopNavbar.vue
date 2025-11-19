<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { computed, ref } from 'vue'
import logoImg from '@/assets/logo.jpeg'
import { Search, UserFilled } from '@element-plus/icons-vue'
import { ElInput, ElButton, ElAvatar, ElIcon } from 'element-plus'

const authStore = useAuthStore()
const isLoggedIn = computed(() => authStore.isLoggedIn)
const searchQuery = ref('')

const emit = defineEmits(['open-login', 'open-register'])

const handleLoginClick = () => {
  emit('open-login')
}

const handleLogout = () => {
  authStore.logout()
}
</script>

<template>
  <div class="navbar">
    <div class="logo-section">
      <div class="logo-icon">
        <img :src="logoImg" alt="FunFlow Logo" />
      </div>
      <span class="logo-text">FunFlow</span>
    </div>

    <div class="search-section">
      <el-input
        v-model="searchQuery"
        placeholder="输入搜索关键词"
        class="search-input-custom"
        :prefix-icon="Search"
        clearable
      />
    </div>

    <div class="user-section">
      <div v-if="isLoggedIn" class="user-avatar-wrapper" @click="handleLogout" title="点击退出">
        <el-avatar :icon="UserFilled" class="user-avatar" :size="40" />
      </div>
      <el-button 
        v-else 
        type="primary" 
        class="login-btn" 
        @click="handleLoginClick"
      >
        登录
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 64px;
  background: rgba(22, 24, 35, 0.85);
  backdrop-filter: blur(20px);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
  z-index: 1000;
  transition: all 0.3s ease;
}

/* Logo Section */
.logo-section {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.logo-icon {
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-icon img {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  object-fit: cover;
}

.logo-text {
  font-size: 24px;
  font-weight: 800;
  background: #fff;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -1px;
  font-family: 'Futura', 'Helvetica Neue', sans-serif;
}

/* Search Section */
.search-section {
  flex: 1;
  max-width: 480px;
  margin: 0 20px;
  display: flex;
  justify-content: center;
}

:deep(.search-input-custom .el-input__wrapper) {
  background-color: rgba(255, 255, 255, 0.06);
  border-radius: 22px;
  box-shadow: none !important;
  padding: 4px 15px;
  transition: all 0.3s;
}

:deep(.search-input-custom .el-input__wrapper.is-focus) {
  background-color: rgba(255, 255, 255, 0.12);
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.2) inset !important;
}

:deep(.search-input-custom .el-input__inner) {
  color: #fff;
  height: 32px;
}

:deep(.search-input-custom .el-input__prefix) {
  color: rgba(255, 255, 255, 0.5);
}

/* User Section */
.user-section {
  display: flex;
  align-items: center;
  gap: 15px;
  min-width: 80px;
  justify-content: flex-end;
}

.login-btn {
  background: linear-gradient(135deg, #fe2c55 0%, #ff6b6b 100%);
  border: none;
  font-weight: 600;
  padding: 18px 24px;
  border-radius: 4px;
}

.login-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
  background: linear-gradient(135deg, #fe2c55 0%, #ff6b6b 100%);
}

.user-avatar-wrapper {
  cursor: pointer;
  transition: all 0.3s;
}

.user-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: 2px solid transparent;
  transition: all 0.3s;
}

.user-avatar-wrapper:hover .user-avatar {
  border-color: #667eea;
  box-shadow: 0 0 10px rgba(102, 126, 234, 0.5);
}
</style>

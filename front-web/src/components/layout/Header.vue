<template>
  <header class="header">
    <div class="header-left">
      <img src="/images/logo.jpeg" alt="FunFlow" class="logo" />
      <span class="logo-text">FunFlow</span>
    </div>

    <div class="header-center">
      <div class="search-box">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="搜索..."
          class="search-input"
          @keyup.enter="handleSearch"
        />
        <button class="search-button" @click="handleSearch">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="11" cy="11" r="8"></circle>
            <path d="m21 21-4.35-4.35"></path>
          </svg>
        </button>
      </div>
    </div>

    <div class="header-right">
      <!-- 未登录状态 -->
      <button v-if="!userStore.isLoggedIn" class="login-btn" @click="showAuthDialog = true">
        登录
      </button>
      <!-- 已登录状态 -->
      <div v-else class="user-info">
        <span class="username">{{ userEmail }}</span>
        <button class="logout-btn" @click="handleLogout">退出</button>
      </div>
    </div>

    <!-- 登录/注册弹窗 -->
    <AuthDialog v-model="showAuthDialog" />
  </header>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import AuthDialog from '@/components/common/AuthDialog.vue'

const userStore = useUserStore()
const searchQuery = ref('')
const showAuthDialog = ref(false)

// 从localStorage获取用户邮箱（实际项目中应该从token解析或从用户信息接口获取）
const userEmail = computed(() => {
  const email = localStorage.getItem('user_email')
  return email || '用户'
})

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    console.log('搜索:', searchQuery.value)
    // TODO: 实现搜索逻辑
  }
}

const handleLogout = () => {
  userStore.logout()
  localStorage.removeItem('user_email')
  ElMessage.success('已退出登录')
}
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 64px;
  background: #161823;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 200px;
}

.logo {
  height: 36px;
  width: auto;
  object-fit: contain;
  border-radius: 8px;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
  color: #ffffff;
  letter-spacing: 0.5px;
}

.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
  max-width: 600px;
  margin: 0 24px;
}

.search-box {
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 500px;
  background: #242634;
  border-radius: 18px;
  padding: 6px 6px 6px 14px;
  transition: all 0.2s;
}

.search-box:focus-within {
  background: #242634;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.search-input {
  flex: 1;
  border: none;
  background: transparent;
  outline: none;
  font-size: 14px;
  color: #ffffff;
}

.search-input::placeholder {
  color: #888888;
}

.search-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  border: none;
  background: transparent;
  color: #888888;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.2s;
}

.search-button:hover {
  background: rgba(24, 144, 255, 0.1);
  color: #1890ff;
}

.header-right {
  display: flex;
  align-items: center;
  min-width: 200px;
  justify-content: flex-end;
}

.login-btn {
  padding: 8px 24px;
  background: #1890ff;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.login-btn:hover {
  background: #40a9ff;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.username {
  font-size: 14px;
  color: #ffffff;
}

.logout-btn {
  padding: 6px 16px;
  background: transparent;
  color: #888888;
  border: 1px solid #3a3a4a;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.logout-btn:hover {
  background: #3a3a4a;
  color: #ffffff;
}
</style>

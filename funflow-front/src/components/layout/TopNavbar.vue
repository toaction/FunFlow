<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { computed, ref, onMounted, onUnmounted } from 'vue'
import logoImg from '@/assets/logo.jpeg'
import { Search, UserFilled, SwitchButton } from '@element-plus/icons-vue'
import { ElInput, ElButton, ElAvatar, ElIcon, ElMessageBox, ElMessage } from 'element-plus'

const authStore = useAuthStore()
const isLoggedIn = computed(() => authStore.isLoggedIn)
const searchQuery = ref('')
const isDropdownOpen = ref(false)
const displayName = computed(() => authStore.getDisplayName())
const userName = computed(() => authStore.user?.username || '')

const emit = defineEmits(['open-login', 'open-register'])

const handleLoginClick = () => {
  emit('open-login')
}

const handleAvatarClick = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要退出登录吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    authStore.logout()
    isDropdownOpen.value = false

    ElMessage({
      message: '退出登录成功',
      type: 'success',
      duration: 2000,
    })
  } catch {
    // 用户取消退出
  }
}

const closeDropdown = () => {
  isDropdownOpen.value = false
}

// 点击外部区域关闭下拉菜单
const handleClickOutside = (event: Event) => {
  const dropdownWrapper = document.querySelector('.user-dropdown-wrapper')
  if (dropdownWrapper && !dropdownWrapper.contains(event.target as Node)) {
    isDropdownOpen.value = false
  }
}

// 键盘事件处理
const handleKeydown = (event: KeyboardEvent) => {
  if (event.key === 'Escape' && isDropdownOpen.value) {
    closeDropdown()
  }
}

// 生命周期钩子
onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  document.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  document.removeEventListener('keydown', handleKeydown)
})
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
      <div v-if="isLoggedIn" class="user-dropdown-wrapper">
        <div class="user-avatar-wrapper" @click="handleAvatarClick">
          <el-avatar :icon="UserFilled" class="user-avatar" :size="40" />
        </div>

        <!-- 下拉菜单 -->
        <transition name="dropdown">
          <div v-if="isDropdownOpen" class="user-dropdown">
            <!-- 箭头 -->
            <div class="dropdown-arrow"></div>

            <!-- 用户信息区域 -->
            <div class="user-info">
              <div class="info-avatar">
                <el-avatar
                  :icon="UserFilled"
                  :size="48"
                  :src="authStore.user?.avatar"
                />
              </div>
              <div class="user-details">
                <div class="user-name">{{ displayName }}</div>
                <div class="user-status">{{ userName || '已登录' }}</div>
              </div>
            </div>

            <!-- 退出登录按钮 -->
            <div class="logout-section">
              <button class="logout-btn" @click="handleLogout">
                <el-icon><SwitchButton /></el-icon>
                <span>退出登录</span>
              </button>
            </div>
          </div>
        </transition>
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

/* 用户下拉菜单 */
.user-dropdown-wrapper {
  position: relative;
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

/* 下拉菜单样式 */
.user-dropdown {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  min-width: 280px;
  background: rgba(30, 31, 41, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4);
  z-index: 1001;
  overflow: hidden;
}

/* 箭头 */
.dropdown-arrow {
  position: absolute;
  top: -6px;
  right: 20px;
  width: 12px;
  height: 12px;
  background: rgba(30, 31, 41, 0.95);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-bottom: none;
  border-right: none;
  transform: rotate(45deg);
  z-index: -1;
}

/* 用户信息区域 */
.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  background: rgba(102, 126, 234, 0.05);
}

.info-avatar {
  flex-shrink: 0;
}

.user-details {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-status {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
}

/* 退出登录区域 */
.logout-section {
  padding: 8px;
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
  padding: 12px 16px;
  background: transparent;
  border: none;
  border-radius: 8px;
  color: #ff6b6b;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.logout-btn:hover {
  background: rgba(255, 107, 107, 0.1);
  color: #ff6b6b;
}

.logout-btn .el-icon {
  font-size: 16px;
}

/* 动画效果 */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.3s ease;
}

.dropdown-enter-from {
  opacity: 0;
  transform: translateY(-10px) scale(0.95);
}

.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px) scale(0.95);
}
</style>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import TopNavbar from './TopNavbar.vue'
import SideNavbar from './SideNavbar.vue'
import LoginModal from '@/components/auth/LoginModal.vue'
import RegisterModal from '@/components/auth/RegisterModal.vue'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const route = useRoute()
const showLogin = ref(false)
const showRegister = ref(false)
const isForced = ref(false) // 强制登录模式

const openLogin = (forced = false) => {
  showLogin.value = true
  showRegister.value = false
  isForced.value = forced
}

const openRegister = (forced = false) => {
  showRegister.value = true
  showLogin.value = false
  isForced.value = forced
}

// 处理弹窗间切换，保持强制模式状态
const handleSwitchToRegister = (forced: boolean) => {
  showRegister.value = true
  showLogin.value = false
  isForced.value = forced
}

const handleSwitchToLogin = (forced: boolean) => {
  showLogin.value = true
  showRegister.value = false
  isForced.value = forced
}

const closeLogin = (forceClose = false) => {
  // 只有在非强制模式下或强制关闭时才允许关闭
  if (isForced.value && !forceClose) return
  showLogin.value = false
  if (forceClose) {
    isForced.value = false
  }
}

const closeRegister = (forceClose = false) => {
  // 只有在非强制模式下或强制关闭时才允许关闭
  if (isForced.value && !forceClose) return
  showRegister.value = false
  if (forceClose) {
    isForced.value = false
  }
}

// 监听路由变化，检查访问权限
watch(() => route.path, (newPath) => {
  // 如果访问需要认证的页面但未登录，弹出强制登录弹窗
  const protectedRoutes = ['/profile', '/create']
  if (protectedRoutes.includes(newPath) && !authStore.token) {
    // 避免重复弹窗和提示
    if (!showLogin.value) {
      openLogin(true) // 使用强制模式
      ElMessage.warning('请先登录')
    }
    return
  }

  // 只有在访问公开页面时才关闭弹窗
  const publicRoutes = ['/', '/hot', '/follow']
  if (publicRoutes.includes(newPath)) {
    // 在强制模式下，只有导航到公开页面才关闭弹窗
    if (isForced.value) {
      showLogin.value = false
      showRegister.value = false
      isForced.value = false
    }
  }
})

// 应用启动时检查用户登录状态和初始路由
onMounted(async () => {
  if (authStore.token && !authStore.user) {
    await authStore.fetchUserProfile()
  }

  // 检查当前路由是否需要认证
  const protectedRoutes = ['/profile', '/create']
  if (protectedRoutes.includes(route.path) && !authStore.token) {
    openLogin(true) // 使用强制模式
    ElMessage.warning('请先登录')
  }
})
</script>

<template>
  <div class="layout">
    <TopNavbar @open-login="openLogin" @open-register="openRegister" />
    <SideNavbar />
    
    <main class="main-content">
      <RouterView />
    </main>

    <LoginModal
      :show="showLogin"
      :forced="isForced"
      @close="closeLogin"
      @switch-to-register="handleSwitchToRegister"
    />
    <RegisterModal
      :show="showRegister"
      :forced="isForced"
      @close="closeRegister"
      @switch-to-login="handleSwitchToLogin"
    />
  </div>
</template>

<style scoped>
.layout {
  min-height: 100vh;
  background-color: #161823;
  color: white;
}

.main-content {
  padding-top: 64px;
  padding-left: 260px;
  min-height: 100vh;
}
</style>


<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import TopNavbar from './TopNavbar.vue'
import SideNavbar from './SideNavbar.vue'
import LoginModal from '@/components/auth/LoginModal.vue'
import RegisterModal from '@/components/auth/RegisterModal.vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const route = useRoute()
const router = useRouter()
const showLogin = ref(false)
const showRegister = ref(false)

const openLogin = () => {
  showLogin.value = true
  showRegister.value = false
}

const openRegister = () => {
  showRegister.value = true
  showLogin.value = false
}

const closeLogin = () => {
  showLogin.value = false
}

const closeRegister = () => {
  showRegister.value = false
}

// 监听路由变化，检查访问权限
watch(() => route.path, (newPath) => {
  // 如果访问个人中心但未登录，弹出登录弹窗
  if (newPath === '/profile' && !authStore.token) {
    showLogin.value = true
    // 可以选择是否重定向回首页
    // router.push('/')
  }
}, { immediate: true })

// 应用启动时检查用户登录状态
onMounted(async () => {
  if (authStore.token && !authStore.user) {
    await authStore.fetchUserProfile()
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
      @close="closeLogin" 
      @switch-to-register="openRegister" 
    />
    <RegisterModal 
      :show="showRegister" 
      @close="closeRegister" 
      @switch-to-login="openLogin" 
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


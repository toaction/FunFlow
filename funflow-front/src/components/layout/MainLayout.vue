<script setup lang="ts">
import { ref } from 'vue'
import { RouterView } from 'vue-router'
import TopNavbar from './TopNavbar.vue'
import SideNavbar from './SideNavbar.vue'
import LoginModal from '@/components/auth/LoginModal.vue'
import RegisterModal from '@/components/auth/RegisterModal.vue'

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


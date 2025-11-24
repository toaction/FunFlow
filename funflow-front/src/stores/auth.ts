import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { UserInfo } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const user = ref<UserInfo | null>(null)
  const isLoggedIn = ref(!!token.value)

  function setToken(newToken: string) {
    token.value = newToken
    localStorage.setItem('token', newToken)
    isLoggedIn.value = true
  }

  function setUser(userInfo: UserInfo) {
    user.value = userInfo
  }

  function logout() {
    token.value = null
    user.value = null
    isLoggedIn.value = false
    localStorage.removeItem('token')
  }

  function getDisplayName(): string {
    if (user.value?.nickname) {
      return user.value.nickname
    }
    if (user.value?.username) {
      return user.value.username
    }
    return '用户'
  }

  return {
    token,
    user,
    isLoggedIn,
    setToken,
    setUser,
    logout,
    getDisplayName
  }
})


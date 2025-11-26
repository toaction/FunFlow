import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import type { UserInfo } from '@/api/auth'
import { getUserProfile } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const user = ref<UserInfo | null>(null)
  const isLoggedIn = computed(() => !!token.value && !!user.value)

  function setToken(newToken: string) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function setUser(userInfo: UserInfo) {
    user.value = userInfo
  }

  function clearUser() {
    user.value = null
  }

  function logout() {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
  }

  async function fetchUserProfile() {
    if (!token.value) return

    try {
      const response = await getUserProfile()
      if (response.code === 200) {
        setUser(response.data)
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
      // 如果获取用户信息失败，可能token已过期，清除登录状态
      logout()
    }
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

  // 初始化时检查 token，如果有 token 则认为已登录（等待用户信息加载）
  const isTokenPresent = computed(() => !!token.value)

  return {
    token,
    user,
    isLoggedIn,
    isTokenPresent,
    setToken,
    setUser,
    clearUser,
    logout,
    getDisplayName,
    fetchUserProfile
  }
})


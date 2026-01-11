import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(localStorage.getItem('access_token'))
  const isLoggedIn = ref<boolean>(!!token.value)

  /**
   * 设置token
   */
  const setToken = (newToken: string) => {
    token.value = newToken
    isLoggedIn.value = true
    localStorage.setItem('access_token', newToken)
  }

  /**
   * 清除token
   */
  const clearToken = () => {
    token.value = null
    isLoggedIn.value = false
    localStorage.removeItem('access_token')
  }

  /**
   * 登出
   */
  const logout = () => {
    clearToken()
  }

  return {
    token,
    isLoggedIn,
    setToken,
    clearToken,
    logout
  }
})

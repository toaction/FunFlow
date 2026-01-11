import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 从localStorage获取token
    const token = localStorage.getItem('access_token')
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    console.log('Response received:', response.data)

    // 检查响应数据结构
    if (!response.data) {
      console.warn('Empty response data')
      return response.data
    }

    // 如果响应有 code 字段，说明是标准响应格式
    if (typeof response.data === 'object' && 'code' in response.data) {
      const { code, data, msg } = response.data

      // 根据后端返回的code判断请求是否成功
      if (code === 200) {
        return data
      } else {
        console.error('API Error:', msg)
        return Promise.reject(new Error(msg || 'Request failed'))
      }
    }

    // 否则直接返回数据（直接返回的格式，如 { accessToken: 'xxx' }）
    return response.data
  },
  (error) => {
    console.error('Response error:', error)
    console.error('Error response:', error.response?.data)

    // 处理401未授权错误
    if (error.response?.status === 401) {
      localStorage.removeItem('access_token')
      // 可以跳转到登录页或触发登录弹窗
    }

    // 返回后端的错误信息
    const errorMsg = error.response?.data?.msg || error.response?.data?.message || error.message || 'Request failed'
    return Promise.reject(new Error(errorMsg))
  }
)

// 封装请求方法
export const http = {
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.get(url, config)
  },

  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return service.post(url, data, config)
  },

  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return service.put(url, data, config)
  },

  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.delete(url, config)
  }
}

export default service

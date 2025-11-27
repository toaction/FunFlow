// API 通用类型定义

export interface ApiResponse<T = unknown> {
  code: number
  message: string
  data: T
}
import request from '@/utils/request'

export interface ApiResponse<T = unknown> {
  code: number
  message: string
  data: T
}

export interface LoginData {
  email: string
  password: string
  captchaId: string
  captchaText: string
}

export interface UserInfo {
  username: string
  nickname: string
  avatar: string
  followingCount?: number
  followerCount?: number
  cachedTotalLikes?: number
  bio?: string
  email?: string
  createdAt?: string
  updatedAt?: string
}


export interface LoginResponse {
  accessToken: string
}

export interface RegisterData {
  email: string
  emailCode: string
  password: string
}

export interface CaptchaResponse {
  captchaId: string
  imageData: string
}

/**
 * 获取图形验证码
 * @returns 验证码响应数据
 */
export function getCaptcha() {
  return request.get<any, ApiResponse<CaptchaResponse>>('/auth/captcha')
}

export function sendEmailCode(data: { email: string; captchaId: string; captchaText: string }) {
  return request.post<any, ApiResponse<null>>('/auth/send-email-code', data)
}

export function register(data: RegisterData) {
  return request.post<any, ApiResponse<null>>('/auth/register', data)
}

/**
 * 用户登录
 * @param data 登录数据
 * @returns 登录响应数据（包含token和用户信息）
 */
export function login(data: LoginData) {
  return request.post<any, ApiResponse<LoginResponse>>('/auth/login', data)
}

/**
 * 获取用户信息
 * @returns 用户信息响应数据
 */
export function getUserProfile() {
  return request.get<any, ApiResponse<UserInfo>>('/user/profile')
}


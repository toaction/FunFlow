import request from '@/utils/request'

export interface LoginData {
  email: string
  password: string
  captchaId: string
  captchaText: string
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
  return request.get<any, { code: number; msg: string; data: CaptchaResponse }>('/auth/captcha')
}

export function sendEmailCode(data: { email: string; captchaId: string; captchaText: string }) {
  return request.post('/auth/send-email-code', data)
}

export function register(data: RegisterData) {
  return request.post('/auth/register', data)
}

export function login(data: LoginData) {
  return request.post<any, { code: number; data: { accessToken: string } }>('/auth/login', data)
}


import request from '@/utils/request'
import type { ApiResponse } from './types'

export interface UserInfo {
  username: string
  nickname: string
  avatar: string
  followingCount: number
  followerCount: number
  cachedTotalLikes: number
  bio: string
}

export interface UpdateProfileData {
  avatar?: string
  username?: string
  nickname?: string
  bio?: string
}

/**
 * 获取用户信息
 * @returns 用户信息响应数据
 */
export function getUserProfile() {
  return request.get<any, ApiResponse<UserInfo>>('/user/profile')
}

/**
 * 上传用户头像
 * @param avatarFile 头像文件
 * @returns 头像URL响应数据
 */
export function uploadAvatar(avatarFile: File) {
  const formData = new FormData()
  formData.append('avatar', avatarFile)
  return request.post<any, ApiResponse<{ avatar: string }>>('/user/profile/avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 更新用户信息
 * @param data 更新用户信息数据
 * @returns 更新结果响应数据
 */
export function updateUserProfile(data: UpdateProfileData) {
  return request.put<any, ApiResponse<null>>('/user/profile', data)
}
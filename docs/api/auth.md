# 登录注册相关接口

## 注册功能
**基本流程：**
1. 用户提交邮箱和图形验证码，校验通过后将收到邮箱验证码
2. 填写邮箱验证码和自定义密码，完成注册

## 图形验证码
在登录/注册功能中引入图形验证码，主要目的是防御自动化恶意行为，提升系统安全性和稳定性。
- 防止暴力破解登录密码
- 阻止批量注册垃圾账号
- 保护邮件发送服务

### 前后端流程
- 登录/注册页面加载，或用户点击刷新按钮后，前端调用接口请求图形验证码
- 后端生成随机文本（数字）和对应图片，生成唯一 `captchaId`，将 (`captchaId`, 验证码文本) 存入 Redis 中并设置过期时间（3 分钟）
- 后端返回 `captchaId` 和图形验证码（base64 格式），前端将图片显示在页面上

### 接口定义

获取图形验证码：`GET /api/auth/captcha`

响应示例：
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "captchaId": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
    "imageData": "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL..."
  }
}
```

## 邮件发送服务
用户填写邮箱和图形验证码，校验通过后将向指定的邮箱发送一个 6 位数字的邮箱验证码。

### 前后端流程
- 前端提交邮箱、图形验证码、`captchaId`
- 后端校验通过后，生成随机 6 位数字作为邮箱验证码发送到指定邮箱，然后将验证码存放在 Redis 中，设置过期时间

### 接口定义
发送邮箱验证码 `POST /api/auth/send-email-code`

请求示例：
```json
{
  "email": "user@example.com",
  "captchaId": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
  "captchaText": "A3b7"
}
```

## 用户注册
用户接收到邮箱验证码后，提交表单（邮箱、验证码、密码）完成注册。

### 接口定义
提交注册 `POST /api/auth/register`

请求示例：
```json
{
  "email": "user@example.com",
  "emailCode": "654321",
  "password": "UserSetPlainTextPassword123!"
}
```

## 登录功能

**需求分析：**
- 用户输入邮箱、密码，进行登录操作
- 使用图形验证码防止机器人、脚本
- 使用 JWT 保存用户登录状态信息

**基本流程：**
1. 页面展示图形验证码
2. 用户输入邮箱、图形验证码、密码进行登录
3. 后端返回 JWT，前端将其保存，之后每次请求都携带 JWT

### 接口定义

用户登录：`POST /api/auth/login`

请求示例：
```json
{
  "email": "user@example.com",
  "password": "UserSetPlainTextPassword123!",
  "captchaId": "login_captcha_123456",
  "captchaText": "X8y9"
}
```

响应示例：
```json
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```
> 后期引入 refreshToken 进行令牌的刷新

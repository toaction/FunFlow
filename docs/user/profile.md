## 获取用户信息

### 需求分析
- 刷新页面时，获取个人信息，用于页面右上角的头像展示
- 导航栏点击个人中心，跳转到个人中心页面，展示头像、昵称、账号、关注数、粉丝数、总获赞量、简介等信息
- 点击修改个人信息按钮，弹出修改信息框，展示头像、账号、昵称、个人简介信息

### 接口定义
- 获取用户信息：`GET /api/user/profile`

前端请求需携带 JWT 令牌：
```json
{
    "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

后端响应：
```json
{
    "code": 200,
    "msg": "success",
    "data": {
        "avatar": "https://example.com/avatar.png",
        "nickname": "Example User",
        "username": "example@example.com",
        "followingCount": 10,
        "followerCount": 5,
        "cachedTotalLikes": 20,
        "bio": "This is an example bio."
    }
}
```

## 修改个人信息

### 需求分析
- 点击修改个人信息按钮，弹出修改信息框，展示头像、账号、昵称、个人简介信息
- 点击更换头像，调用后端接口，使用阿里云 OSS 保存头像，返回图片 URL
- 点击保存按钮，调用后端接口，修改用户信息

### 接口定义

- 上传头像：`POST /api/user/profile/avatar`
- 修改用户信息：`PUT /api/user/profile`

### 上传头像流程

**前端：**
- 用户点击更改头像按钮，选择本地图片，点击确认
- 前端调用接口：POST `/api/user/profile/avatar`

**后端：**
1. 接收请求：处理 `POST /api/user/profile/avatar` 请求
2. 上传图片：使用阿里云 OSS 上传图片，返回图片 URL

**响应体：**
```json
{
    "code": 200,
    "msg": "success",
    "data": {
        "avatar": "https://example.com/avatar.png"
    }
}
```

### 修改用户信息流程

**前端：**
- 用户修改头像、账号、昵称、个人简介，点击保存
- 前端调用接口：`PUT /api/user/profile`

**请求体内容：**
```json
{
    "avatar": "https://example.com/avatar.png",
    "username": "example@example.com",
    "nickname": "Example User",
    "bio": "This is an example bio."
}
```

**后端：**
- 接收请求：处理 `PUT /api/user/profile` 请求
- 修改用户信息：修改数据库中该用户的信息

**响应体：**
```json
{
    "code": 200,
    "msg": "success",
    "data": null
}

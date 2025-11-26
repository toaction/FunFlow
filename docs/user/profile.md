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

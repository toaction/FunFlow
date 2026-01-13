## 注册功能

### 需求分析
- 使用邮箱作为注册入口
- 防止滥用邮箱发送邮件服务

### 基本流程

1. 用户提交邮箱和图形验证码，校验通过后将收到邮箱验证码
2. 填写邮箱验证码和自定义密码，完成账号注册

### 涉及接口

- `GET /api/auth/captcha` - 获取图形验证码
- `POST /api/auth/send-email-code` - 发送邮箱验证码
- `POST /api/auth/register` - 用户注册

## 登录功能

### 需求分析

- 用户输入邮箱、密码，进行登录操作
- 使用图形验证码防止机器人、脚本
- 使用 JWT 保存用户登录状态信息

### 基本流程

1. 页面展示图形验证码
2. 用户输入邮箱、图形验证码、密码进行登录
3. 后端返回 JWT，前端将其保存，之后每次请求都携带 JWT

### 涉及接口

- `GET /api/auth/captcha` - 获取图形验证码
- `POST /api/auth/login` - 用户登录

## 认证拦截器

### 需求分析

- 保护需要登录才能访问的接口
- 校验请求是否携带有效的 JWT 令牌
- 将当前用户信息存储到请求上下文中，方便业务代码获取
- 公开接口（登录、注册等）不需要认证

### 功能实现

#### 1. UserContext 用户上下文

使用 `ThreadLocal` 存储当前请求的用户 ID：

```java
// 设置当前用户ID
UserContext.setUserId(userId);

// 获取当前用户ID
Long userId = UserContext.getUserId();

// 清除当前用户ID
UserContext.clear();
```

#### 2. AuthInterceptor 认证拦截器

拦截器实现逻辑：

1. **提取 Token**: 从请求头 `Authorization` 中提取 Bearer Token
2. **验证 Token**:
   - 检查 Token 格式是否正确（必须以 `Bearer ` 开头）
   - 验证 Token 是否过期
   - 解析 Token 获取用户 ID
   - 当 Token 验证失败时，返回 `401 Unauthorized`
3. **存储上下文**: 验证通过后，将 `userId` 存储到 `ThreadLocal`
4. **清理资源**: 请求完成后自动清理 `ThreadLocal`，防止内存泄漏

#### 3. 拦截器配置

在 `WebConfig` 中注册拦截器：

- **拦截路径**: 所有 `/api/**` 路径
- **排除路径**: `/api/auth/**`（登录、注册、验证码等公开接口）

### 使用示例

#### 前端请求示例

```javascript
// 在请求头中携带 JWT
fetch('/api/user/profile', {
  headers: {
    'Authorization': 'Bearer ' + token
  }
})
```

#### 后端获取当前用户

```java
@RestController
@RequestMapping("/user/profile")
public class UserController {

    @GetMapping
    public Result<UserProfile> getProfile() {
        // 直接从 ThreadLocal 获取当前登录用户的 ID
        Long userId = UserContext.getUserId();

        // 执行业务逻辑...
        return Result.success(userProfile);
    }
}
```

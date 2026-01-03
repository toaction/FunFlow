## 项目介绍

FunFlow 是一个短视频浏览与分享应用，致力于为用户提供流畅、有趣的短视频消费体验。

## 主要功能

1. 视频推送：首页视频推荐、热门视频排行榜、标签视频推送、相似视频推荐、关注用户视频列表
2. 用户信息：登录、注册、个人信息修改、关注/粉丝列表、主页作品、收藏夹、喜欢列表、浏览记录
3. 视频相关：视频创作、点赞、收藏、评论
3. 基础服务：对象存储、内容审核

## 技术栈

- 前端：Vue.js
- 后端：Spring Boot、MyBatis、MySQL、Redis、OSS 等

> 具体业务具体分析，以完成功能为导向，不做没必要的炫技

## 目录结构
后端：
```text
main/
├── resources/
│   └── application.yml          # 配置文件
│
└── java/com/action/
    ├── Application.java         # 启动类
    ├── common/                  # 公共类（封装返回结果类、状态码等）
    ├── config/                  # 配置类（跨域、拦截器注册等）
    ├── controller/              # 控制器（接收 HTTP 请求）
    ├── service/                 # 业务逻辑层
    ├── dao/                     # 数据访问层（Mapper 接口）
    ├── domain/
    │   ├── entity/              # 数据库实体类
    │   ├── dto/                 # 前端请求参数对象
    │   └── vo/                  # 前端响应数据对象
    ├── exception/               # 异常类和全局异常处理
    ├── interceptor/             # 拦截器（登录验证、权限校验）
    └── util/                    # 工具类（JWT、 ThreadLocal 等）
```

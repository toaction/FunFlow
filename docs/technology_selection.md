## 技术选型

### 语言：Java / Go
**Java：**
- 简单、相关教程多
- 框架支持：Spring Boot、Spring Cloud

**Golang：**
- 语法简洁，速度快
- 框架支持：Gin、go-zero

### 项目架构
前后端分离，单体项目，MVC 架构


### 技术栈
- Web 框架：SpringBoot / Gin
- 数据库：MySQL
- 数据库操作：MyBatis / Gorm
- 缓存中间件：Redis

### 目录结构
> Spring Boot 项目
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
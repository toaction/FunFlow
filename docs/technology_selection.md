## 技术选型

### 编程语言的选择

Web 项目开发，主要使用的语言有 Java、Golang、Python 等。这里只比较一下 Java 和 Golang。

**Java：**
- 容易上手，网上相关的视频教程有很多
- 丰富的框架生态：Spring、SpringBoot、MyBatis、SpringCloud

**Golang：**
- 语法简洁，编译和运行速度快
- 框架支持：Gin、Gorm、go-zero

该项目暂时使用 Java。

### 项目架构

最基本：前后端分离，前端和后端通过接口对接，后端返回 JSON 类型的数据。

**单体架构 OR 微服务架构：**
- 在并发访问量不大的情况下，单体架构足以支撑本项目的运行。
- 待项目完善以及访问量增大时，考虑迁移到微服务架构。

单体项目，采用经典的 MVC （Model-View-Controller） 架构：
- Model：代表项目的核心数据和业务逻辑，具体实现为后端的 Service 层和 Mapper 层等。
- View：用户界面的呈现层。具体实现为前端部分。
- Controller：作为模型和视图之间的协调者，处理用户输入。具体实现为后端的 Controller 层。

### 技术栈

根据业务需求决定技术栈的选型：
- Web 框架：Spring Boot
- 数据存储：MySQL
- 对象访问：Mybatis
- 缓存：Redis
- 对象存储：阿里云 OSS

是否要引入 MQ、ES、Zookeeper 等中间件？
- 具体业务，具体分析
- 单体项目，理论上不需要这些中间件

### 目录结构
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

## 需求分析

**包含字段：**
- 主键 user_id
- 用户在进行账号注册时，需提供邮箱和密码，密码以密文形式存储
- 在个人主页展示的用户信息包括：头像、昵称、账号、关注数、粉丝数、作品总获赞量、个人简介
- 需记录账号状态，包括：创建时间、最后登录时间、状态（正常、禁用）、注销时间（账号注销与恢复）

**字段约束：**
- 非空：id、邮箱、密码哈希、昵称、账号、关注数、粉丝数、获赞量、创建时间、最后登录时间、状态
- 唯一：id、邮箱、账号

**字段说明：**
- 用户名/邮箱：应用层统一转小写存储，避免大小写问题
- 关注数/粉丝数：作为冗余字段存储在用户表中，通过事务保证一致性
- 获赞量：使用 `BIGINT` 类型的 `cached_total_likes` 缓存字段，通过定时任务更新，支持更大数值
- 密码：存储密码哈希而非明文，增强安全性
- 状态：`1` 表示正常，`0` 表示禁用
- 软删除：通过 `deleted_at` 字段实现，`NULL` 表示未删除，有值表示删除时间；支持数据恢复

## 索引
UNIQUE 索引：账号和邮箱的 UNIQUE 约束已自动创建索引，无需重复

联合索引：建立 `idx_status_deleted` 联合索引，快速查询正常用户
 ```sql
 -- 查询正常用户（最常见，99%的查询）
WHERE status = 1 AND deleted_at IS NULL

-- 查询禁用用户
WHERE status = 0 AND deleted_at IS NULL
 ```

## 表设计
```sql
CREATE TABLE user (
    user_id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',

    -- 隐藏信息
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱账号（唯一，应用层统一转小写存储）',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    
    -- 展示信息
    avatar VARCHAR(255) COMMENT '头像URL',
    nickname VARCHAR(30) NOT NULL COMMENT '昵称',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '账号（唯一，应用层统一转小写存储）',
    following_count INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '关注数',
    follower_count INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '粉丝数',
    cached_total_likes BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '获赞量（缓存）',
    bio TEXT COMMENT '个人简介',
    
    -- 系统字段
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    last_login_at TIMESTAMP NULL COMMENT '最后登录时间',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    deleted_at TIMESTAMP NULL COMMENT '注销时间（软删除）',
    
    -- 索引（注意：UNIQUE 约束已自动创建索引，无需重复添加）
    INDEX idx_status_deleted (status, deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
```

## 设计决策
### 为什么获赞量是一个 cache，而非实时更新的字段？
直接在用户表中添加一个 `total_likes` 字段，属于“反范式化”设计。虽然它可能在查询时非常快，但会带来一系列严重的问题：
- 点赞/取消属于频繁的写入操作。如果获赞量单独存储，每当视频的点赞数发生变化，都需要同时更新两个表：视频表和用户表。
- 频繁同时更新两张表，会大大增加数据库的写入压力。高并发场景下，存在数据不一致风险。

**解决方案1：实时查询**
- 当需要展示用户的获赞量时，直接通过 SQL 的 SUM 聚合函数从视频表中计算。
- 适用于数据量不大或实时性要求不高的场景。

**解决方案2：异步计算 + 定期更新**
- 不把 `total_likes` 作为数据库中的一个持久化字段，而是作为一个可以延迟更新的“缓存值”。
- 通过定时任务（例如，每10分钟或每小时）运行一次聚合查询，更新这个缓存字段。
- 优点：查询性能高、减轻写入压力、最终一致性。

### 为什么关注数和粉丝数作为冗余字段实时更新？
关注/粉丝数的写操作（关注/取关）相对稀少，但读操作极其频繁。这种“写少读多”的模式是使用冗余计数字段的理想场景。

**数据一致性方案：** 在关注/取关操作时，在同一个数据库事务内更新关系表和计数字段。

### 是否需要在 `deleted_at` 上单独添加索引？

不需要，原因如下：

1. **查询频率分析**：
   - ✅ 高频：`WHERE status = 1 AND deleted_at IS NULL`（查正常用户）→ 联合索引支持
   - ⚠️ 低频：`WHERE deleted_at IS NOT NULL`（查已删除用户）→ 通常只在定时清理任务中使用
   - ⚠️ 低频：`WHERE deleted_at < 某时间`（清理过期账号）→ 一天执行一次的定时任务

2. **成本收益比**：
   - 添加索引的成本：每次 INSERT/UPDATE/DELETE 都要维护额外索引，增加写入开销
   - 收益：仅对低频查询（定时任务）有优化，但即使全表扫描也能在秒级完成

3. **联合索引的妥协方案**：
   - 虽然 `idx_status_deleted` 无法完美覆盖 `WHERE deleted_at IS NOT NULL`
   - 但 MySQL 仍可扫描此索引（比全表扫描快），性能可接受

**何时需要添加单独的 `deleted_at` 索引？**
- 已删除用户数量超过百万级别
- 需要频繁查询或统计已删除用户（而非定时任务）
- 此时可添加：`INDEX idx_deleted_at (deleted_at)` 用于支持清理任务

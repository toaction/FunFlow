# 短视频表设计

## 需求分析
- 用户上传短视频，需提供的信息包括：视频、封面、文案、标签、是否公开属性
- 在浏览其他用户的视频时，可以看到的视频信息包括：视频、封面、标题、标签、作者、点赞数、收藏数、评论数
- 在进行视频管理时，需记录创建时间、更新时间、状态（审核中、已发布、已下架、违规等）

## 表字段设计
**基本信息字段**
- `id`，视频唯一标识
- `user_id`，上传用户 ID
- `title`，视频文案
- `tags`，视频标签列表
- `cover_url`，视频封面图片 URL
- `video_url`，视频文件 URL

**互动数据字段**
- `view_count`，播放量
- `like_count`，点赞量
- `collect_count`，收藏数
- `comment_count`，评论数

**状态控制字段**
- `status`，视频状态（0:审核中, 1:已发布, 2:已下架, 3:违规）
- `is_public`，是否公开

**时间戳**
- `created_at`，创建时间
- `updated_at`，修改时间

## 建表语句
```sql
CREATE TABLE video (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '视频唯一标识',
    user_id BIGINt UNSIGNED NOT NULL COMMENT '上传用户 ID',
    title VARCHAR(255) NOT NULL DEFAULT '' COMMENT '视频文案',
    tags JSON DEFAULT NULL COMMENT '视频标签列表，如 ["搞笑", "美食"]',
    cover_url VARCHAR(512) NOT NULL DEFAULT '' COMMENT '视频封面图片 URL',
    video_url VARCHAR(512) NOT NULL DEFAULT '' COMMENT '视频文件 URL',

    view_count BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '播放量',
    like_count BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞量',
    collect_count BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '收藏数',
    comment_count BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论数',

    status TINYINT NOT NULL DEFAULT 0 COMMENT '视频状态：0-审核中, 1-已发布, 2-已下架, 3-违规',
    is_public TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否公开：0-否, 1-是',

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',

    PRIMARY KEY (id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短视频主表';
```

插入数据示例：
```sql
INSERT INTO video (
    user_id,
    title,
    tags,
    cover_url,
    video_url,
    status,
    is_public
) VALUES (
    1001,
    '今天吃了超好吃的火锅！',
    JSON_ARRAY('美食', '日常', '探店'),
    'https://example.com/covers/1001_hotpot.jpg',
    'https://example.com/videos/1001_hotpot.mp4',
    1,   -- 已发布
    1    -- 公开
);
```

package com.action.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 * 对应数据表：user
 *
 * @author Xiangfu
 * @date 2025-11-20
 */
@Data
public class User {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 账号（唯一，统一小写）
     */
    private String username;

    /**
     * 关注数
     */
    private Integer followingCount;

    /**
     * 粉丝数
     */
    private Integer followerCount;

    /**
     * 获赞量缓存
     */
    private Long cachedTotalLikes;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 邮箱（唯一，统一小写）
     */
    private String email;

    /**
     * 密码哈希
     */
    private String passwordHash;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;

    /**
     * 状态：1-正常，0-禁用
     */
    private Integer status;

    /**
     * 删除时间（NULL 表示未删除）
     */
    private LocalDateTime deletedAt;
}



package com.action.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 * 对应数据表：user
 *
 * @author Xiangfu
 */
@Data
public class User {

    /**
     * 用户ID（主键）
     */
    private Long userId;

    /**
     * 邮箱账号（唯一，应用层统一转小写存储）
     */
    private String email;

    /**
     * 密码哈希
     */
    private String passwordHash;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 账号（唯一，应用层统一转小写存储）
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
     * 获赞量（缓存）
     */
    private Long cachedTotalLikes;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;

    /**
     * 状态：1-正常，0-禁用
     */
    private Integer status;

    /**
     * 注销时间（软删除，NULL 表示未删除）
     */
    private LocalDateTime deletedAt;
}



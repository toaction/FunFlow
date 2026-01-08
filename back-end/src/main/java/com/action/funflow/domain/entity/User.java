package com.action.funflow.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * table: users
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 用户唯一ID
     */
    private Long userId;

    /**
     * 邮箱（统一小写存储）
     */
    private String email;

    /**
     * 密码哈希值
     */
    private String passwordHash;

    /**
     * 账号（统一小写存储）
     */
    private String username;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 关注数
     */
    private Integer followingCount;

    /**
     * 粉丝数
     */
    private Integer followerCount;

    /**
     * 作品总获赞量（缓存值）
     */
    private Long totalLikesReceived;

    /**
     * 账号创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;

    /**
     * 账号状态：0-正常，1-封禁，2-注销
     */
    private Integer status;

    /**
     * 账号状态枚举
     */
    public enum Status {
        NORMAL(0, "正常"),
        BANNED(1, "封禁"),
        DELETED(2, "注销");

        private final Integer code;
        private final String desc;

        Status(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}

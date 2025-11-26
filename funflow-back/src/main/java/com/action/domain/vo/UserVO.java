package com.action.domain.vo;

import lombok.Data;

/**
 * 用户信息返回的 VO
 * 对应文档中的用户信息接口
 *
 * @author Xiangfu
 * @date 2025-11-26
 */
@Data
public class UserVO {

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 账号
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
     * 总获赞量
     */
    private Long cachedTotalLikes;

    /**
     * 个人简介
     */
    private String bio;
}
package com.action.domain.vo;

import lombok.Data;

/**
 * 登录成功返回的 VO
 *
 * @author Xiangfu
 * @date 2025-11-20
 */
@Data
public class LoginVO {

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 用户信息
     */
    private UserInfo user;

    @Data
    public static class UserInfo {
        /**
         * 用户名
         */
        private String username;

        /**
         * 昵称
         */
        private String nickname;

        /**
         * 头像URL
         */
        private String avatar;
    }
}

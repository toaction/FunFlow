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
}

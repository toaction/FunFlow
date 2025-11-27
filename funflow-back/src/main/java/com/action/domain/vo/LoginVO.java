package com.action.domain.vo;

import lombok.Data;

/**
 * 登录成功返回的 VO
 *
 * @author Xiangfu
 */
@Data
public class LoginVO {

    /**
     * 访问令牌
     */
    private String accessToken;
}

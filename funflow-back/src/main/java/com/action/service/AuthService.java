package com.action.service;

import com.action.domain.dto.RegisterRequest;
import com.action.domain.dto.SendEmailCodeRequest;

/**
 * 认证服务接口
 *
 * @author Xiangfu
 * @date 2025-11-20
 */
public interface AuthService {

    /**
     * 发送邮箱验证码
     *
     * @param request 发送邮箱验证码请求
     */
    void sendEmailCode(SendEmailCodeRequest request);

    /**
     * 用户注册
     *
     * @param request 注册请求
     */
    void register(RegisterRequest request);
}


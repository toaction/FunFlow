package com.action.service;

/**
 * 邮件服务接口
 *
 * @author Xiangfu
 * @date 2025-11-20
 */
public interface EmailService {
    
    /**
     * 发送邮箱验证码
     *
     * @param to      收件人邮箱
     * @param code    验证码
     */
    void sendEmailCode(String to, String code);
}


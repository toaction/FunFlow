package com.action.funflow.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录请求 DTO
 */
@Data
public class LoginRequest {

    /**
     * 邮箱地址
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 验证码 ID
     */
    @NotBlank(message = "验证码ID不能为空")
    private String captchaId;

    /**
     * 用户输入的验证码文本
     */
    @NotBlank(message = "验证码不能为空")
    private String captchaText;
}
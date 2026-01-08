package com.action.funflow.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户注册请求 DTO
 */
@Data
public class RegisterRequest {
    /**
     * 邮箱地址
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 邮箱验证码（6位数字）
     */
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须为6位数字")
    private String emailCode;

    /**
     * 密码 6-16位，只能为数字、字母和@符号的组合
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9@]{6,16}$", message = "密码必须为6-16位，只能包含数字、字母和@符号")
    private String password;
}

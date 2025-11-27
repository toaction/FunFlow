package com.action.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户注册请求 DTO
 *
 * @author Xiangfu
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
     * 密码（8-32位，至少包含字母和数字）
     * @Size(min = 8, max = 32, message = "密码长度必须在8-32位之间")
     * @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).*", message = "密码必须包含字母和数字")
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}

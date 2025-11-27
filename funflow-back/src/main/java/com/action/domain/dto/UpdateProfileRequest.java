package com.action.domain.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新用户信息请求 DTO
 *
 * @author Xiangfu
 */
@Data
public class UpdateProfileRequest {

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 账号（4-20位数字和字母组合）
     */
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "账号必须是4-20位数字和字母的组合")
    private String username;

    /**
     * 昵称
     */
    @Size(max = 20, message = "昵称长度不能超过20个字符")
    private String nickname;

    /**
     * 个人简介
     */
    @Size(max = 200, message = "个人简介长度不能超过200个字符")
    private String bio;
}
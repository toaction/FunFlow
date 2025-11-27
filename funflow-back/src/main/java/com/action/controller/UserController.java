package com.action.controller;

import com.action.common.Result;
import com.action.domain.dto.UpdateProfileRequest;
import com.action.domain.vo.UserVO;
import com.action.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 处理用户信息相关接口
 *
 * @author Xiangfu
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/profile")
    public Result<UserVO> getUserProfile() {
        log.info("获取用户信息请求");
        UserVO userVO = userService.getCurrentUser();
        return Result.success(userVO);
    }

    /**
     * 上传用户头像
     *
     * @param avatarFile 头像文件
     * @return 头像URL
     */
    @PostMapping("/profile/avatar")
    public Result<Map<String, String>> uploadAvatar(@RequestParam("avatar") MultipartFile avatarFile) {
        log.info("上传用户头像请求，文件名: {}, 文件大小: {} bytes",
                avatarFile.getOriginalFilename(), avatarFile.getSize());

        String avatarUrl = userService.uploadAvatar(avatarFile);

        Map<String, String> data = new HashMap<>();
        data.put("avatar", avatarUrl);

        log.info("上传用户头像成功，头像URL: {}", avatarUrl);
        return Result.success(data);
    }

    /**
     * 更新用户信息
     *
     * @param updateProfileRequest 更新用户信息请求
     * @return 操作结果
     */
    @PutMapping("/profile")
    public Result<Void> updateUserProfile(@RequestBody @Valid UpdateProfileRequest updateProfileRequest) {
        log.info("更新用户信息请求，头像: {}, 账号: {}, 昵称: {}, 简介: {}",
                updateProfileRequest.getAvatar(),
                updateProfileRequest.getUsername(),
                updateProfileRequest.getNickname(),
                updateProfileRequest.getBio());

        userService.updateUserProfile(updateProfileRequest);

        log.info("更新用户信息成功");
        return Result.success();
    }
}
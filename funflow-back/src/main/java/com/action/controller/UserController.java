package com.action.controller;

import com.action.common.Result;
import com.action.domain.vo.UserVO;
import com.action.service.UserService;
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
}
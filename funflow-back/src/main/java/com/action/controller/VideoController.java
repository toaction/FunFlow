package com.action.controller;

import com.action.common.Result;
import com.action.domain.dto.VideoCreateRequest;
import com.action.service.VideoService;
import com.action.util.UserContext;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 视频控制器
 * 处理视频创建相关接口
 *
 * @author Xiangfu
 */
@RestController
@RequestMapping("/api/videos")
@Slf4j
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 创建视频
     *
     * @param request 视频创建请求
     * @return 创建结果
     */
    @PostMapping
    public Result<String> createVideo(@Valid VideoCreateRequest request) {
        Long userId = UserContext.getUserId();
        log.info("用户 {} 创建视频请求", userId);

        videoService.createVideo(userId, request);
        return Result.success();
    }
}

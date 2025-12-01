package com.action.service;

import com.action.domain.dto.VideoCreateRequest;

/**
 * 视频服务接口
 *
 * @author Xiangfu
 */
public interface VideoService {

    /**
     * 创建视频
     *
     * @param userId 用户ID
     * @param request 视频创建请求
     */
    void createVideo(Long userId, VideoCreateRequest request);
}

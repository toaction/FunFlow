package com.action.service.impl;

import com.action.dao.VideoMapper;
import com.action.domain.dto.VideoCreateRequest;
import com.action.domain.entity.Video;
import com.action.exception.BusinessException;
import com.action.service.OssService;
import com.action.service.VideoService;
import com.action.util.FileUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 视频服务实现类
 *
 * @author Xiangfu
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    private OssService ossService;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void createVideo(Long userId, @Validated VideoCreateRequest request) {
        // TODO 校验标签数量、视频格式、视频大小、封面格式、封面大小
        String videoUrl = null;
        String coverUrl = null;

        // 上传视频文件到OSS
        String videoExt = FileUtil.getFileExtension(request.getVideo().getOriginalFilename());
        if (videoExt == null) {
            throw new BusinessException("视频格式错误");
        }
        String videoObjectName = "videos/" + userId + "/" + UUID.randomUUID().toString() + "." + videoExt;
        videoUrl = ossService.upload(request.getVideo(), videoObjectName);

        // 上传封面文件到 OSS
        if (request.getCover() != null && !request.getCover().isEmpty()) {
            String coverExt = FileUtil.getFileExtension(request.getCover().getOriginalFilename());
            if (coverExt == null) {
                throw new BusinessException("图片格式错误");
            }
            String coverObjectName = "covers/" + userId + "/" + UUID.randomUUID().toString() + "." + coverExt;
            coverUrl = ossService.upload(request.getCover(), coverObjectName);
        }

        // 保存视频信息到数据库
        Video video = new Video();
        video.setUserId(userId);
        video.setCaption(request.getCaption());
        video.setCoverUrl(coverUrl != null ? coverUrl : ""); // 如果没有封面，暂时为空，后续可以生成默认封面
        video.setVideoUrl(videoUrl);
        video.setStatus(0); // 0-审核中
        video.setIsPublic(request.getIsPublic());

        // 处理标签
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            try {
                String tagsJson = objectMapper.writeValueAsString(request.getTags());
                video.setTags(tagsJson);
            } catch (JsonProcessingException e) {
                log.error("标签JSON序列化失败", e);
                throw new BusinessException("标签格式错误");
            }
        }

        // 保存到数据库（时间戳由数据库默认值处理）
        videoMapper.insert(video);
        log.info("视频创建成功");
    }
}


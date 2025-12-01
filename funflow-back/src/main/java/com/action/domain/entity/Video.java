package com.action.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 短视频实体
 * 对应数据表：video
 *
 * @author Xiangfu
 */
@Data
public class Video {

    /**
     * 视频唯一标识（主键）
     */
    private Long id;

    /**
     * 上传用户 ID
     */
    private Long userId;

    /**
     * 视频文案
     */
    private String caption;

    /**
     * 视频标签列表，如 ["搞笑", "美食"]
     */
    private String tags;

    /**
     * 视频封面图片 URL
     */
    private String coverUrl;

    /**
     * 视频文件 URL
     */
    private String videoUrl;

    /**
     * 播放量
     */
    private Long viewCount;

    /**
     * 点赞量
     */
    private Long likeCount;

    /**
     * 收藏数
     */
    private Long collectCount;

    /**
     * 评论数
     */
    private Long commentCount;

    /**
     * 视频状态：0-审核中, 1-已发布, 2-已下架, 3-违规
     */
    private Integer status;

    /**
     * 是否公开：0-否, 1-是
     */
    private Boolean isPublic;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 最后更新时间
     */
    private LocalDateTime updatedAt;
}

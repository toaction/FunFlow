package com.action.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 视频创建请求DTO
 * 对应前端 multipart/form-data 格式
 *
 * @author Xiangfu
 */
@Data
public class VideoCreateRequest {

    /**
     * 视频文件（必填）
     */
    @NotNull(message = "视频文件不能为空")
    private MultipartFile video;

    /**
     * 封面图片（可选）
     */
    private MultipartFile cover;

    /**
     * 视频文案（必填，最多500字）
     */
    @NotBlank(message = "视频文案不能为空")
    @Size(max = 500, message = "视频文案不能超过500字")
    private String caption;

    /**
     * 视频标签列表（最多5个）
     */
    private List<String> tags;

    /**
     * 是否公开（必填）
     */
    @NotNull(message = "是否公开不能为空")
    private Boolean isPublic;
}

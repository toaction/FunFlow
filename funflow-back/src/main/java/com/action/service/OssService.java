package com.action.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * OSS 服务接口
 *
 * @author Xiangfu
 */
public interface OssService {

    /**
     * OSS 上传图片
     *
     * @param file 文件
     * @param objectName 存储路径
     * @return URL
     */
    String upload(MultipartFile file, String objectName);
}

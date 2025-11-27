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
     * @param file 图片文件
     * @param fileName 文件名
     * @return 图片 URL
     * @throws Exception 上传异常
     */
    String uploadImage(MultipartFile file, String fileName) throws Exception;
}

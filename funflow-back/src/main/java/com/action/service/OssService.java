package com.action.service;

import com.action.config.OssConfig;
import com.action.util.UserContext;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Date;

/**
 * 阿里云 OSS 服务
 * 处理文件上传到阿里云对象存储服务
 *
 * @author Xiangfu
 * @date 2025-11-27
 */
@Service
public class OssService {

    @Autowired
    private OssConfig ossConfig;

    /**
     * 使用阿里云 OSS 上传图片
     *
     * @param file 图片文件
     * @return 图片 URL
     * @throws Exception
     */
    public String uploadImage(MultipartFile file, String fileName) throws Exception {
        // 创建 OSSClient 实例
        OSS ossClient = new OSSClientBuilder().build(
            ossConfig.getEndpoint(),
            ossConfig.getAccessKeyId(),
            ossConfig.getAccessKeySecret()
        );

        try {
            // 指定上传路径（可选目录，如 images/）
            String objectName = "images/" + fileName;
            // 上传文件流
            ossClient.putObject(ossConfig.getBucketName(), objectName, file.getInputStream());
            // 返回文件访问 URL（公共读 bucket）
            return "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + objectName;

        } finally {
            // 关闭 OSSClient
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}

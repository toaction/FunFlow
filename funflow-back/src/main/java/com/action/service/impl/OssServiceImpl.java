package com.action.service.impl;

import com.action.config.OssConfig;
import com.action.exception.BusinessException;
import com.action.service.OssService;
import com.aliyun.oss.OSS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 阿里云 OSS 服务实现类
 * 处理文件上传到阿里云对象存储服务
 *
 * @author Xiangfu
 */
@Service
@Slf4j
public class OssServiceImpl implements OssService {

    @Autowired
    private OssConfig ossConfig;

    @Autowired
    private OSS ossClient;

    /**
     * 使用阿里云 OSS 上传图片
     *
     * @param file 文件
     * @param objectName 存储路径
     * @return  URL
     */
    @Override
    public String upload(MultipartFile file, String objectName) {

        try (InputStream inputStream = file.getInputStream()) {
            ossClient.putObject(ossConfig.getBucketName(), objectName, inputStream);

            // 如果 bucket 权限是“公共读”，可直接拼接：
            return "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + objectName;

        } catch (Exception e) {
            log.error("OSS 上传失败，SAVE_FILE_PATH: {}", objectName, e);
            throw new BusinessException("OSS 上传失败");
        }
    }
}

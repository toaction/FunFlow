package com.action.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云 OSS 配置类
 * 用于绑定 application.yml 中的阿里云对象存储配置
 *
 * @author Xiangfu
 * @date 2025-11-27
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class OssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
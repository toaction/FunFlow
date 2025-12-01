package com.action.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 阿里云 OSS 配置类
 * 用于绑定 application.yml 中的阿里云对象存储配置
 *
 * @author Xiangfu
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class OssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    @Bean(destroyMethod = "shutdown") // 👈 关键：自动调用 shutdown()
    @Primary
    public OSS ossClient() {
        return  new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }
}

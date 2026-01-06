package com.action.funflow.service.impl;


import com.action.funflow.common.RedisConstants;
import com.action.funflow.domain.vo.CaptchaVO;
import com.action.funflow.exception.BusinessException;
import com.action.funflow.service.AuthService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public CaptchaVO generateCaptcha() {
        try {
            // 生成验证码文本
            String captchaText = defaultKaptcha.createText();

            // 生成验证码图片
            BufferedImage captchaImage = defaultKaptcha.createImage(captchaText);

            // 将图片转换为 Base64
            String imageData = convertImageToBase64(captchaImage);

            // 生成唯一 captchaId
            String captchaId = UUID.randomUUID().toString();

            // 存储到 Redis，设置5分钟过期
            String redisKey = RedisConstants.getCaptchaKey(captchaId);
            stringRedisTemplate.opsForValue().set(
                    redisKey,
                    captchaText,
                    RedisConstants.CAPTCHA_EXPIRE_SECONDS,
                    TimeUnit.SECONDS
            );

            log.info("生成图形验证码成功，captchaId: {}, captchaText: {}", captchaId, captchaText);

            return new CaptchaVO(captchaId, imageData);

        } catch (Exception e) {
            throw new BusinessException("生成验证码失败，请稍后重试");
        }
    }

    /**
     * 将 BufferedImage 转换为 Base64 编码的字符串
     *
     * @param image 图片对象
     * @return Base64 编码的图片字符串
     */
    private String convertImageToBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/png;base64," + base64;
        } catch (IOException e) {
            throw new BusinessException("验证码图片编码失败");
        }
    }

}

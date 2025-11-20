package com.action.service.impl;

import com.action.common.RedisConstants;
import com.action.dao.UserMapper;
import com.action.domain.dto.SendEmailCodeRequest;
import com.action.exception.BusinessException;
import com.action.service.AuthService;
import com.action.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 *
 * @author Xiangfu
 * @date 2025-11-20
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private EmailService emailService;
    
    @Override
    public void sendEmailCode(SendEmailCodeRequest request) {
        String email = request.getEmail().toLowerCase();
        String captchaId = request.getCaptchaId();
        String captchaText = request.getCaptchaText();
        
        // 1. 校验图形验证码
        validateCaptcha(captchaId, captchaText);
        
        // 2. 校验邮箱格式（DTO 已经校验过，这里再次确认）
        // 3. 校验邮箱是否已被注册
        validateEmailNotRegistered(email);
        
        // 4. 生成并发送邮件验证码
        String emailCode = generateEmailCode();
        saveEmailCodeToRedis(email, emailCode);
        emailService.sendEmailCode(email, emailCode);
        
        log.info("邮箱验证码发送成功，邮箱: {}", email);
    }
    
    /**
     * 校验图形验证码
     *
     * @param captchaId   验证码ID
     * @param captchaText 用户输入的验证码文本
     */
    private void validateCaptcha(String captchaId, String captchaText) {
        String redisKey = RedisConstants.getCaptchaKey(captchaId);
        String correctCaptchaText = stringRedisTemplate.opsForValue().get(redisKey);
        
        // 无论对错，立即删除 Redis 中的该 captchaId（一次性使用）
        stringRedisTemplate.delete(redisKey);
        
        // 验证码不存在或已过期
        if (correctCaptchaText == null) {
            throw new BusinessException("验证码已过期，请重新获取");
        }
        
        // 忽略大小写比较
        if (!correctCaptchaText.equalsIgnoreCase(captchaText)) {
            throw new BusinessException("验证码错误，请重新输入");
        }
    }
    
    /**
     * 校验邮箱是否已被注册
     *
     * @param email 邮箱地址（小写）
     */
    private void validateEmailNotRegistered(String email) {
        int count = userMapper.countByEmail(email);
        if (count > 0) {
            throw new BusinessException("该邮箱已被注册");
        }
    }
    
    /**
     * 生成6位数字验证码
     *
     * @return 验证码
     */
    private String generateEmailCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 生成 100000-999999 之间的6位数字
        return String.valueOf(code);
    }
    
    /**
     * 将邮箱验证码存入 Redis
     *
     * @param email 邮箱地址
     * @param code  验证码
     */
    private void saveEmailCodeToRedis(String email, String code) {
        String redisKey = RedisConstants.getEmailCodeKey(email);
        stringRedisTemplate.opsForValue().set(
                redisKey,
                code,
                RedisConstants.EMAIL_CODE_EXPIRE_SECONDS,
                TimeUnit.SECONDS
        );
    }
}


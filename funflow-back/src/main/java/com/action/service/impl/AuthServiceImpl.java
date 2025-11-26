package com.action.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.action.common.RedisConstants;
import com.action.dao.UserMapper;
import com.action.domain.dto.RegisterRequest;
import com.action.domain.dto.SendEmailCodeRequest;
import com.action.domain.entity.User;
import com.action.exception.BusinessException;
import com.action.service.AuthService;
import com.action.service.EmailService;

import cn.hutool.crypto.digest.BCrypt;
import com.action.domain.dto.LoginRequest;
import com.action.domain.vo.LoginVO;
import com.action.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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

        // 2. 校验邮箱是否已被注册
        validateEmailNotRegistered(email);
        
        // 3. 生成 6 位数字并发送邮件验证码
        String emailCode = RandomUtil.randomNumbers(6);
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

    @Override
    public void register(RegisterRequest request) {
        String email = request.getEmail().toLowerCase();
        String emailCode = request.getEmailCode();
        String password = request.getPassword();

        // 1. 验证邮箱验证码
        validateEmailCode(email, emailCode);

        // 2. 再次校验邮箱是否已被注册（防止数据竞争）
        // 在校验验证码和最终注册之间可能很短的时间内有人注册了
        validateEmailNotRegistered(email);

        // 3. 创建用户
        createUser(email, password);
    }

    /**
     * 验证邮箱验证码
     *
     * @param email     邮箱地址
     * @param emailCode 用户输入的验证码
     */
    private void validateEmailCode(String email, String emailCode) {
        String redisKey = RedisConstants.getEmailCodeKey(email);
        String correctCode = stringRedisTemplate.opsForValue().get(redisKey);

        // 验证码不存在或已过期
        if (correctCode == null) {
            throw new BusinessException("验证码已过期，请重新获取");
        }

        // 比较验证码
        if (!correctCode.equals(emailCode)) {
            throw new BusinessException("验证码错误");
        }

        // 验证码验证成功后，立即删除（一次性使用）
        stringRedisTemplate.delete(redisKey);
    }

    /**
     * 创建用户并保存到数据库
     *
     * @param email    邮箱地址
     * @param password 明文密码
     */
    private void createUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(email); // 用户名默认使用邮箱
        user.setNickname(extractNicknameFromEmail(email)); // 昵称从邮箱中提取
        user.setAvatar(""); // 默认头像为空
        user.setBio(""); // 默认简介为空
        user.setPasswordHash(BCrypt.hashpw(password)); // 密码加密
        user.setStatus(1); // 正常状态
        user.setFollowingCount(0);
        user.setFollowerCount(0);
        user.setCachedTotalLikes(0L);
        user.setCreatedAt(LocalDateTime.now());
        user.setLastLoginAt(null); // 注册时为空，登录时更新
        user.setDeletedAt(null); // 软删除字段，初始为空

        userMapper.insert(user);

        log.info("用户注册成功，邮箱: {}", email);
    }

    /**
     * 从邮箱中提取昵称
     * 例如：user@example.com -> user
     *
     * @param email 邮箱地址
     * @return 昵称
     */
    private String extractNicknameFromEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex > 0) {
            return email.substring(0, atIndex);
        }
        return email;
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

    @Override
    public LoginVO login(LoginRequest request) {
        String email = request.getEmail().toLowerCase();
        String password = request.getPassword();
        String captchaId = request.getCaptchaId();
        String captchaText = request.getCaptchaText();

        // 1. 校验图形验证码
        validateCaptcha(captchaId, captchaText);

        // 2. 验证用户凭证
        User user = validateUserCredentials(email, password);

        // 3. 更新最后登录时间
        updateLastLoginTime(user.getUserId());

        // 4. 生成 JWT 令牌
        String accessToken = generateAccessToken(user);

        log.info("用户登录成功，邮箱: {}, 用户ID: {}", email, user.getUserId());

        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(accessToken);

        return loginVO;
    }

    /**
     * 验证用户凭证
     *
     * @param email    邮箱
     * @param password 明文密码
     * @return 用户对象
     */
    private User validateUserCredentials(String email, String password) {
        // 根据邮箱查询用户
        User user = userMapper.selectByEmail(email);

        // 用户不存在
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 密码不匹配
        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 用户状态异常
        if (user.getStatus() != 1) {
            throw new BusinessException("账号异常，请联系客服");
        }

        return user;
    }

    /**
     * 生成 JWT 访问令牌
     *
     * @param user 用户对象
     * @return accessToken
     */
    private String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());

        return JwtUtil.generateAccessToken(claims);
    }

    /**
     * 更新用户最后登录时间
     *
     * @param userId 用户ID
     */
    private void updateLastLoginTime(Long userId) {
        userMapper.updateLastLoginAt(userId, LocalDateTime.now());
    }
}


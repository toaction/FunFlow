package com.action.controller;

import com.action.common.Result;
import com.action.domain.dto.LoginRequest;
import com.action.domain.dto.RegisterRequest;
import com.action.domain.dto.SendEmailCodeRequest;
import com.action.service.AuthService;
import com.action.service.CaptchaService;
import com.action.domain.vo.CaptchaVO;
import com.action.domain.vo.LoginVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 * 处理登录、注册等认证相关接口
 *
 * @author Xiangfu
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private AuthService authService;

    /**
     * 获取图形验证码
     *
     * @return 验证码信息
     */
    @GetMapping("/captcha")
    public Result<CaptchaVO> getCaptcha() {
        log.info("获取图形验证码请求");
        CaptchaVO captchaVO = captchaService.generateCaptcha();
        return Result.success(captchaVO);
    }

    /**
     * 发送邮箱验证码
     *
     * @param request 发送邮箱验证码请求
     * @return 结果
     */
    @PostMapping("/send-email-code")
    public Result<Void> sendEmailCode(@Valid @RequestBody SendEmailCodeRequest request) {
        log.info("发送邮箱验证码请求，邮箱: {}", request.getEmail());
        authService.sendEmailCode(request);
        return Result.success();
    }

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 结果
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        log.info("用户注册请求，邮箱: {}", request.getEmail());
        authService.register(request);
        return Result.success();
    }

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        log.info("用户登录请求，邮箱: {}", request.getEmail());
        LoginVO loginVO = authService.login(request);
        return Result.success(loginVO);
    }
}

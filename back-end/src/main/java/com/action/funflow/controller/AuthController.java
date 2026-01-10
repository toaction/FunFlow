package com.action.funflow.controller;

import com.action.funflow.common.Result;
import com.action.funflow.domain.dto.LoginRequest;
import com.action.funflow.domain.dto.RegisterRequest;
import com.action.funflow.domain.dto.SendEmailCodeRequest;
import com.action.funflow.domain.vo.CaptchaVO;
import com.action.funflow.domain.vo.LoginVO;
import com.action.funflow.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 处理登录、注册等相关接口
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 获取图形验证码
     *
     * @return 验证码信息
     */
    @GetMapping("/captcha")
    public Result<CaptchaVO> getCaptcha() {
        CaptchaVO captchaVO = authService.generateCaptcha();
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
        LoginVO loginVO = authService.login(request);
        return Result.success(loginVO);
    }
}

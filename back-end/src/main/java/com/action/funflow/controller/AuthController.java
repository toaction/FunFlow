package com.action.funflow.controller;

import com.action.funflow.common.Result;
import com.action.funflow.domain.vo.CaptchaVO;
import com.action.funflow.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

package com.action.controller;

import com.action.common.Result;
import com.action.domain.vo.CaptchaVO;
import com.action.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 * 处理登录、注册等认证相关接口
 *
 * @author Xiangfu
 * @date 2025-11-20
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private CaptchaService captchaService;

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
}

package com.action.service;

import com.action.domain.vo.CaptchaVO;

/**
 * 验证码服务接口
 *
 * @author Xiangfu
 * @date 2025-11-20
 */
public interface CaptchaService {
    
    /**
     * 生成图形验证码
     *
     * @return 验证码信息（包含 captchaId 和 imageData）
     */
    CaptchaVO generateCaptcha();
}


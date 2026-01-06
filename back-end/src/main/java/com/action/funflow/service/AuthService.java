package com.action.funflow.service;

import com.action.funflow.domain.vo.CaptchaVO;

public interface AuthService {

    /**
     * 生成图形验证码
     *
     * @return 验证码信息（包含 captchaId 和 imageData）
     */
    CaptchaVO generateCaptcha();

}

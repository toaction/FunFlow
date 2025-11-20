package com.action.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图形验证码响应 VO
 *
 * @author Xiangfu
 * @date 2025-11-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaVO {
    
    /**
     * 验证码ID
     */
    private String captchaId;
    
    /**
     * 验证码图片（Base64编码）
     */
    private String imageData;
}


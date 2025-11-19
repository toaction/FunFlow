package com.action.common;

/**
 * 统一状态码定义
 *
 * @author Xiangfu
 * @date 2025/11/18
 */
public class Code {

    /** 成功 */
    public static final int SUCCESS = 200;
    /** 失败 */
    public static final int ERROR = 400;
    /** 参数校验失败 */
    public static final int VALIDATION_ERROR = 401;
    /** 业务逻辑异常 */
    public static final int BUSINESS_ERROR = 402;
    /** 默认系统异常 */
    public static final int SYSTEM_ERROR = 500;
    
}


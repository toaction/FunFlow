package com.action.funflow.common;

/**
 * 返回状态码定义类
 */
public class Code {

    /**
     * 默认请求成功状态码
     */
    public static final int SUCCESS = 200;
    /**
     * 默认请求失败状态吗
     */
    public static final int ERROR = 400;

    /**
     * 参数校验失败
     */
    public static final int VALIDATION_ERROR = 401;
    /**
     * 业务逻辑异常
     */
    public static final int BUSINESS_ERROR = 402;
    /**
     * 系统异常
     */
    public static final int SYSTEM_ERROR = 403;

}

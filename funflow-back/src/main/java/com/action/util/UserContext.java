package com.action.util;

/**
 * 用户上下文
 * 使用 ThreadLocal 存储当前登录用户信息，方便在业务代码中获取
 *
 * @author Xiangfu
 */
public class UserContext {

    private static final ThreadLocal<Long> userIdThreadLocal = new ThreadLocal<>();

    /**
     * 设置当前用户ID
     *
     * @param userId 用户ID
     */
    public static void setUserId(Long userId) {
        userIdThreadLocal.set(userId);
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID
     */
    public static Long getUserId() {
        return userIdThreadLocal.get();
    }

    /**
     * 清理当前用户信息
     */
    public static void clear() {
        userIdThreadLocal.remove();
    }
}

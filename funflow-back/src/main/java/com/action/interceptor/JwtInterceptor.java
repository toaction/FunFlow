package com.action.interceptor;

import com.action.util.JwtUtil;
import com.action.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 拦截器
 * 用于验证用户身份，校验 JWT 令牌的有效性
 *
 * @author Xiangfu
 * @date 2025-11-26
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取Authorization
        String authHeader = request.getHeader("Authorization");

        // 检查Authorization是否存在且格式正确
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        }

        // 提取JWT令牌
        String token = authHeader.substring(7); // 去掉"Bearer "前缀

        try {
            // 检查令牌是否过期
            if (JwtUtil.isTokenExpired(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                return false;
            }

            // 解析令牌获取用户信息
            Long userId = JwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                return false;
            }

            // 将用户信息存储到ThreadLocal中，便于后续业务逻辑获取
            UserContext.setUserId(userId);
            return true;

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理ThreadLocal，防止内存泄漏
        UserContext.clear();
    }
}

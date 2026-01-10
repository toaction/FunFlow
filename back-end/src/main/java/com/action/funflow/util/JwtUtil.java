package com.action.funflow.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 * 用于生成和解析 JWT 令牌
 */
public class JwtUtil {

    // JWT 密钥（HS256 算法要求至少 256 位 / 32 字节）
    private static final String SECRET_KEY = "fun-flow-secret-key-2026-at-least-32-bytes";

    // accessToken 过期时间：7天
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    private static final SecretKey KEY;

    static {
        // 使用 HS256 算法
        KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * 生成 accessToken
     *
     * @param claims 载荷数据
     * @return accessToken
     */
    public static String generateAccessToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析令牌
     *
     * @param token JWT 令牌
     * @return Claims
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 验证令牌是否过期
     *
     * @param token JWT 令牌
     * @return 是否过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 验证并获取用户ID
     *
     * @param token JWT 令牌
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }
}

package com.action.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.core.Ordered;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

/**
 * 跨域配置类
 * 允许前端跨域访问后端接口
 *
 * @author Xiangfu
 * @date 2025-11-20
 */
@Configuration
public class CorsConfig {

    /**
     * 配置CORS过滤器并设置为最高优先级
     * 确保在应用其他过滤器之前处理跨域请求
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        CorsConfiguration config = new CorsConfiguration();

        // 允许的域名（开发环境）
        // 前端实际运行在 http://localhost:5173
        config.addAllowedOriginPattern("http://localhost:*");
        config.addAllowedOriginPattern("http://127.0.0.1:*");

        // 明确指定允许的请求头（如果需要携带特定的头信息）
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("Origin");
        config.addAllowedHeader("X-Requested-With");

        // 允许所有请求方法（GET、POST、PUT、DELETE等）
        config.addAllowedMethod("*");

        // 允许携带认证信息（如Cookie、Authorization等）
        config.setAllowCredentials(true);

        // 预检请求的缓存时间（秒）
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        CorsFilter corsFilter = new CorsFilter(source);

        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(corsFilter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 设置为最高优先级
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}


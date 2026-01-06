package com.action.funflow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 设置 Controller 统一前缀
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer c) {
        c.addPathPrefix("/api", HandlerTypePredicate.forAnnotation(RestController.class));
    }

}
package com.study.securityoauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        // 모든 경로 맵핑 허용
        corsRegistry.addMapping("/**")

            // JWT 받을 수 있도록 허용
            .exposedHeaders("Set-Cookie")

            // 프론트엔드 서버 주소 허용
            .allowedOrigins("*");
    }
}
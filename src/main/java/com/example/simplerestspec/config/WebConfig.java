package com.example.simplerestspec.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 静的リソース（ルートパスや static 配下）へのアクセスにインターセプターを適用
        registry.addInterceptor(staticResourceInterceptor)
                .addPathPatterns("/**") 
                .excludePathPatterns("/api/**"); // APIエンドポイントは除外
    }

    private final StaticResourceInterceptor staticResourceInterceptor;
}

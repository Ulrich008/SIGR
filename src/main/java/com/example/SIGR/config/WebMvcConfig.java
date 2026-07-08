package com.example.SIGR.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MinistereInterceptor ministereInterceptor;

    public WebMvcConfig(MinistereInterceptor ministereInterceptor) {
        this.ministereInterceptor = ministereInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ministereInterceptor)
                .excludePathPatterns("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**");
    }
}
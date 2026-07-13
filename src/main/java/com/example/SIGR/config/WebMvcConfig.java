package com.example.SIGR.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
        // order() = LOWEST_PRECEDENCE : garantit que ce filtre s'active
        // APRÈS l'intercepteur "Open Session In View" de Spring Boot
        // (spring.jpa.open-in-view=true), qui ouvre la session Hibernate
        // réellement utilisée par les requêtes du contrôleur. Sans cet
        // ordre explicite, l'ordre relatif des deux intercepteurs n'est
        // pas garanti : si le nôtre passe en premier, il active le filtre
        // sur une session jetée avant même que la vraie session (ouverte
        // ensuite par Spring Boot) n'existe — le filtre ne s'applique
        // alors jamais.
        registry.addInterceptor(ministereInterceptor)
                .excludePathPatterns("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**")
                .order(Ordered.LOWEST_PRECEDENCE);
    }
}
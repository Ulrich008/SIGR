package com.example.SIGR.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 🔗 Serveur
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Serveur local"))

                // 🔐 Sécurité JWT
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )

                // ℹ️ Infos API
                .info(new Info()
                        .title("SIGR  MEF API")
                        .description("API de gestion du Système d'Information de Gestion des Risques")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe SIGR")
                                .email("contact@sigr.com")
                                .url("https://sigr.com"))
                        .license(new License()
                                .name("Licence interne")
                                .url("https://sigr.com/license"))
                );
    }
}
package com.example.SIGR.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()

                /**
                 * ================= SERVEUR =================
                 */
                .addServersItem(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Serveur local")
                )

                /**
                 * ================= SÉCURITÉ JWT =================
                 */
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )

                .components(
                        new Components()

                                .addSecuritySchemes(
                                        securitySchemeName,

                                        new SecurityScheme()

                                                .name(securitySchemeName)

                                                .type(SecurityScheme.Type.HTTP)

                                                .scheme("bearer")

                                                .bearerFormat("JWT")
                                )
                )

                /**
                 * ================= INFOS API =================
                 */
                .info(
                        new Info()

                                .title("SIGR - MEF API")

                                .description(
                                        "API de gestion du Système d'Information de Gestion des Risques"
                                )

                                .version("1.0.0")

                                .contact(
                                        new Contact()

                                                .name("Equipe SIGR")

                                                .email("contact@sigr.com")

                                                .url("https://sigr.com")
                                )

                                .license(
                                        new License()

                                                .name("Licence interne")

                                                .url("https://sigr.com/license")
                                )
                );
    }
}
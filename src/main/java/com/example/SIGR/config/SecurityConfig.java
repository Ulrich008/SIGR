package com.example.SIGR.config;

import com.example.SIGR.security.CustomAccessDeniedHandler;
import com.example.SIGR.security.JwtAuthenticationEntryPoint;
import com.example.SIGR.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    /**
     * Encoder des mots de passe
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Hiérarchie des rôles techniques : SUPER_ADMIN hérite de tous les
     * droits d'ADMIN. Ainsi @PreAuthorize("hasAuthority('ADMIN')") (ou
     * hasAnyAuthority(...'ADMIN'...)) laisse aussi passer SUPER_ADMIN,
     * sans avoir à lister SUPER_ADMIN partout dans les contrôleurs.
     * Les profils métier (CMMR, CCI, PILOTE, RESPONSABLE_RISQUES,
     * RESPONSABLE_ACTION, AUDITEUR) restent hors hiérarchie : ce sont
     * des autorisations métier distinctes, pas des rôles techniques.
     *
     * Déclaré en @Bean static, comme recommandé par Spring Security,
     * pour que la hiérarchie soit disponible avant l'initialisation
     * des proxys de sécurité des méthodes (@EnableMethodSecurity).
     */
    @Bean
    static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("SUPER_ADMIN > ADMIN");
    }

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(
            RoleHierarchy roleHierarchy
    ) {
        DefaultMethodSecurityExpressionHandler handler =
                new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

    /**
     * Authentication Manager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    /**
     * Configuration Spring Security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                /**
                 * Activation du CORS
                 */
                .cors(cors -> {})

                /**
                 * Désactivation CSRF
                 */
                .csrf(csrf -> csrf.disable())

                /**
                 * API Stateless avec JWT
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                /**
                 * Gestion des erreurs 401 / 403
                 */
                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                jwtAuthenticationEntryPoint
                        )

                        .accessDeniedHandler(
                                customAccessDeniedHandler
                        )
                )

                /**
                 * Configuration des accès
                 */
                .authorizeHttpRequests(auth -> auth

                        // ================= AUTH =================

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // ================= SWAGGER =================

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // ================= ENUMS (données de référence) =================

                        .requestMatchers(
                                "/api/plans-audit/enums/**"
                        ).permitAll()

                        // ================= OPTIONS =================

                        .requestMatchers(
                                org.springframework.http.HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // ================= ROUTES PROTÉGÉES =================

                        .anyRequest().authenticated()
                )

                /**
                 * Filtre JWT
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
package com.example.SIGR.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/",
            "/swagger-ui",
            "/v3/api-docs",
            "/swagger-resources",
            "/webjars"
    );

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        return PUBLIC_PATHS.stream()
                .anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            String authHeader = request.getHeader("Authorization");

            String token = null;
            String matricule = null;

            /**
             * Extraction du token
             */
            if (authHeader != null
                    && authHeader.startsWith("Bearer ")) {

                token = authHeader.substring(7);

                matricule = jwtService.extractMatricule(token);
            }

            /**
             * Authentification utilisateur
             */
            if (matricule != null
                    && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(matricule);

                if (jwtService.isTokenValid(token)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType("application/json");

            response.getWriter().write("""
                {
                  "message": "Token expiré"
                }
            """);

        } catch (JwtException | IllegalArgumentException e) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType("application/json");

            response.getWriter().write("""
                {
                  "message": "Token JWT invalide"
                }
            """);

        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            response.setContentType("application/json");

            response.getWriter().write("""
                {
                  "message": "Erreur interne d'authentification"
                }
            """);
        }
    }
}
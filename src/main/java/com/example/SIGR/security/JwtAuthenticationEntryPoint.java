package com.example.SIGR.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
        );

        response.setContentType("application/json");

        Map<String, Object> body = new HashMap<>();

        body.put("status", 401);
        body.put("error", "Non authentifié");
        body.put(
                "message",
                "Vous devez vous connecter pour accéder à cette ressource"
        );
        body.put("path", request.getServletPath());

        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(
                response.getOutputStream(),
                body
        );
    }
}
package com.example.SIGR.config;

import com.example.SIGR.service.MinistereService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MinistereFilterConfig extends OncePerRequestFilter {

    private final MinistereService ministereService;
    private final EntityManager entityManager;

    public MinistereFilterConfig(MinistereService ministereService, EntityManager entityManager) {
        this.ministereService = ministereService;
        this.entityManager = entityManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String codeMinistere = ministereService.getCodeMinistereOfCurrentUser();
        
        if (codeMinistere != null) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("ministereFilter")
                    .setParameter("codeMinistere", codeMinistere);
        }
        
        filterChain.doFilter(request, response);
    }
}

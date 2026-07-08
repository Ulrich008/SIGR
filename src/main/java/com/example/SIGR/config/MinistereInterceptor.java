package com.example.SIGR.config;

import com.example.SIGR.service.MinistereService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MinistereInterceptor implements HandlerInterceptor {

    private final MinistereService ministereService;
    private final EntityManager entityManager;

    public MinistereInterceptor(MinistereService ministereService, EntityManager entityManager) {
        this.ministereService = ministereService;
        this.entityManager = entityManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String codeMinistere = ministereService.getCodeMinistereOfCurrentUser();

        if (codeMinistere != null) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("ministereFilter")
                    .setParameter("codeMinistere", codeMinistere);
        }

        return true;
    }
}
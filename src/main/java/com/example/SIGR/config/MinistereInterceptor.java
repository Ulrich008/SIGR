package com.example.SIGR.config;

import com.example.SIGR.security.SecurityUtils;
import com.example.SIGR.services.MinistereService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MinistereInterceptor implements HandlerInterceptor {

    /**
     * Valeur de secours utilisée quand l'utilisateur courant n'a
     * aucun ministère assigné. Ne correspondra jamais à un vrai
     * code_ministere : le filtre reste actif (fail-closed) au lieu
     * de laisser passer toutes les données faute de paramètre.
     */
    public static final String AUCUN_MINISTERE = "__AUCUN_MINISTERE__";

    /**
     * Même principe qu'AUCUN_MINISTERE : valeur de secours qui ne
     * correspondra jamais à un vrai code_unite, pour garder le filtre actif
     * (fail-closed) si le Correspondant Risque courant n'a pas d'UA assignée.
     */
    public static final String AUCUNE_UNITE = "__AUCUNE_UNITE__";

    private final MinistereService ministereService;
    private final EntityManager entityManager;

    public MinistereInterceptor(MinistereService ministereService, EntityManager entityManager) {
        this.ministereService = ministereService;
        this.entityManager = entityManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // SUPER_ADMIN : accès global, non filtré par ministère
        if (SecurityUtils.hasAuthority("SUPER_ADMIN")) {
            return true;
        }

        Session session = entityManager.unwrap(Session.class);

        String codeMinistere = ministereService.getCodeMinistereOfCurrentUser();

        session.enableFilter("ministereFilter")
                .setParameter("codeMinistere", codeMinistere != null ? codeMinistere : AUCUN_MINISTERE);

        // Correspondant Risque : cantonnement supplémentaire à sa propre
        // unité administrative (voir @Filter("uaFilter") sur les entités
        // Formalisation/Évaluation/Mitigation/Cartographie). Filtre inactif,
        // donc sans effet, pour tous les autres profils.
        if (SecurityUtils.hasAuthority("CORRESPONDANT_RISQUE")) {
            String codeUnite = SecurityUtils.getCurrentCodeUnite();
            session.enableFilter("uaFilter")
                    .setParameter("codeUnite", codeUnite != null ? codeUnite : AUCUNE_UNITE);
        }

        return true;
    }
}
package com.example.SIGR.service;

import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.Ministere;
import com.example.SIGR.entity.Role;
import com.example.SIGR.repository.AgentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MinistereService {

    private final AgentRepository agentRepository;

    public MinistereService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    /**
     * Récupère l'agent actuellement connecté
     */
    private Agent getCurrentAgent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String matricule = authentication.getName();

        return agentRepository.findByMatricule(matricule).orElse(null);
    }

    /**
     * Récupère le ministère de l'utilisateur connecté.
     * Retourne null si l'agent est ADMIN (accès à tous les ministères)
     * ou si l'agent n'a pas de ministère assigné.
     */
    public Ministere getMinistereOfCurrentUser() {
        try {
            Agent agent = getCurrentAgent();

            if (agent == null) {
                return null;
            }

            // ADMIN : pas de restriction par ministère
            if (agent.getRole() == Role.ADMIN) {
                return null;
            }

            return agent.getMinistere();

        } catch (Exception e) {
            // Si la colonne code_ministere n'existe pas encore, retourner null
            return null;
        }
    }

    /**
     * Récupère le code du ministère de l'utilisateur connecté.
     * Retourne null pour un ADMIN (aucun filtre appliqué => voit tous les ministères).
     */
    public String getCodeMinistereOfCurrentUser() {
        Ministere ministere = getMinistereOfCurrentUser();
        return ministere != null ? ministere.getCode() : null;
    }
}
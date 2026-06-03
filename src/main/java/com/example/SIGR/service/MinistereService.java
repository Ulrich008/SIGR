package com.example.SIGR.service;

import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.Ministere;
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
     * Récupère le ministère de l'utilisateur connecté
     */
    public Ministere getMinistereOfCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String matricule = authentication.getName();
        
        try {
            Agent agent = agentRepository.findByMatricule(matricule).orElse(null);
            return agent != null ? agent.getMinistere() : null;
        } catch (Exception e) {
            // Si la colonne code_ministere n'existe pas encore, retourner null
            return null;
        }
    }

    /**
     * Récupère le code du ministère de l'utilisateur connecté
     */
    public String getCodeMinistereOfCurrentUser() {
        Ministere ministere = getMinistereOfCurrentUser();
        return ministere != null ? ministere.getCode() : null;
    }
}

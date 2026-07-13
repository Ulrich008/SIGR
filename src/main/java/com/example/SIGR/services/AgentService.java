package com.example.SIGR.services;

import com.example.SIGR.dto.request.AgentRequest;
import com.example.SIGR.dto.response.AgentResponse;

import java.util.List;

public interface AgentService {

    /**
     * Création d'un agent
     */
    AgentResponse create(AgentRequest request);

    /**
     * Recherche d'un agent par matricule
     */
    AgentResponse getByMatricule(String matricule);

    /**
     * Profil de l'agent actuellement connecté. Contrairement à
     * getByMatricule, ignore le filtre par ministère : un agent doit
     * toujours pouvoir se retrouver lui-même.
     */
    AgentResponse getMe(String matricule);

    /**
     * Liste de tous les agents
     */
    List<AgentResponse> getAll();

    /**
     * Modification d'un agent
     */
    AgentResponse update(String matricule, AgentRequest request);

    /**
     * Activation ou désactivation d'un compte agent
     */
    AgentResponse changeStatus(String matricule, Boolean enabled);

    /**
     * Suppression d'un agent
     */
    void delete(String matricule);

    /**
     * Génère un PDF listant les agents d'un ministère.
     * ADMIN : le ministère est imposé (le sien), codeMinistere est ignoré.
     * SUPER_ADMIN : codeMinistere est obligatoire, n'importe quel ministère.
     */
    byte[] generateAgentsPdf(String codeMinistere);
}
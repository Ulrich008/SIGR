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
}
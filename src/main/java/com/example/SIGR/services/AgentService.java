package com.example.SIGR.services;

import com.example.SIGR.dto.request.AgentRequest;
import com.example.SIGR.dto.response.AgentResponse;
import com.example.SIGR.dto.response.ImportResultResponse;
import org.springframework.web.multipart.MultipartFile;

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
     * Changement du mot de passe par l'agent lui-même (self-service).
     * Vérifie l'ancien mot de passe avant d'appliquer le nouveau.
     */
    void changerMotDePasse(String matricule, String ancienMotDePasse, String nouveauMotDePasse);

    /**
     * Modification de son propre email (self-service), depuis "Mon profil".
     */
    AgentResponse modifierMonEmail(String matricule, String email);

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

    /**
     * Import en masse d'agents depuis un fichier Excel. Chaque ligne est
     * traitée indépendamment (une ligne en échec n'annule pas les autres)
     * en réutilisant {@link #create} pour garantir les mêmes règles
     * métier qu'une création unitaire (génération du matricule, etc.).
     */
    ImportResultResponse importFromExcel(MultipartFile file);

    /**
     * Modèle Excel (fichier .xlsx) attendu par {@link #importFromExcel}.
     */
    byte[] generateImportTemplate();
}
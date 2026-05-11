package com.example.SIGR.repository;

import com.example.SIGR.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, String> {

    /**
     * Vérifie si un agent existe par son matricule métier
     */
    boolean existsByMatricule(String matricule);

    /**
     * Recherche un agent par son matricule métier
     */
    Optional<Agent> findByMatricule(String matricule);

    /**
     * Vérifie si un agent existe par son NPI
     */
    boolean existsByNpi(String npi);

    /**
     * Recherche un agent par son NPI
     */
    Optional<Agent> findByNpi(String npi);
}
package com.example.SIGR.repository;

import com.example.SIGR.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
     * Code du ministère de l'agent, en requête SQL native pour ne JAMAIS
     * être soumis au filtre Hibernate "ministereFilter" (contrairement à
     * findByMatricule, une requête dérivée JPQL qui, elle, respecte tout
     * filtre actif sur la session courante). Sert uniquement à déterminer
     * le ministère de l'utilisateur connecté — une opération qui ne doit
     * jamais dépendre d'un filtre lui-même basé sur ce ministère, sous
     * peine de blocage auto-référentiel permanent.
     */
    @Query(value = "SELECT code_ministere FROM agent WHERE matricule_agent = :matricule", nativeQuery = true)
    String findCodeMinistereByMatricule(@Param("matricule") String matricule);

    /**
     * Code (métier) de l'unité administrative de l'agent, en requête SQL
     * native pour les mêmes raisons que findCodeMinistereByMatricule
     * ci-dessus. Sert à restreindre la liste des agents visibles par un
     * AGENT (profil métier) à sa propre unité administrative.
     */
    @Query(value = "SELECT ua.code_unite FROM agent a JOIN unite_administrative ua ON ua.id_unite = a.id_unite " +
            "WHERE a.matricule_agent = :matricule", nativeQuery = true)
    String findCodeUniteByMatricule(@Param("matricule") String matricule);

    /**
     * Vérifie si un agent existe par son NPI
     */
    boolean existsByNpi(String npi);

    /**
     * Recherche un agent par son NPI
     */
    Optional<Agent> findByNpi(String npi);

    /**
     * Nombre d'agents d'un profil donné dans une unité administrative
     * donnée : sert de base à la séquence du matricule généré pour
     * les profils métier (RR_, Pt_, CCI_, CMMR_, Au_).
     */
    long countByProfil_CodeAndUnite_Code(String codeProfil, String codeUnite);

    /**
     * Résout les destinataires d'une notification ciblée par rôle/étape :
     * tous les agents actifs d'un ministère donné rattachés à un profil
     * métier donné (RESPONSABLE_RISQUES, PILOTE, CCI, CMMR, RESPONSABLE_ACTION...).
     * Ignore les comptes désactivés (enabled=false) — inutile de notifier
     * un agent qui ne peut plus se connecter.
     */
    List<Agent> findByMinistere_CodeAndProfil_CodeAndEnabledTrue(String codeMinistere, String codeProfil);
}
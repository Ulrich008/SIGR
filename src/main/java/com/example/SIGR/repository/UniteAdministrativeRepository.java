package com.example.SIGR.repository;

import com.example.SIGR.entity.UniteAdministrative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniteAdministrativeRepository extends JpaRepository<UniteAdministrative, String> {

    /**
     * Vérifie l'existence d'une unité par son code métier
     */
    boolean existsByCode(String code);

    /**
     * Recherche une unité par son code métier
     */
    Optional<UniteAdministrative> findByCode(String code);
}
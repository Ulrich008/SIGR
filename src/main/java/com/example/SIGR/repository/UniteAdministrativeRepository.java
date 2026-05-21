package com.example.SIGR.repository;

import com.example.SIGR.entity.TypeUnite;
import com.example.SIGR.entity.UniteAdministrative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniteAdministrativeRepository extends JpaRepository<UniteAdministrative, String> {

    /**
     * Vérifie l'existence d'une unité par son code métier
     */
     boolean existsByCode(String code);

    // UniteAdministrativeRepository.java
    boolean existsByLibelleIgnoreCase(String libelle);

    boolean existsByLibelleIgnoreCaseAndCodeNot(String libelle, String code);


    boolean existsByTypeUnite(TypeUnite typeUnite);
    /**
     * Recherche une unité par son code métier
     */
    Optional<UniteAdministrative> findByCode(String code);
}
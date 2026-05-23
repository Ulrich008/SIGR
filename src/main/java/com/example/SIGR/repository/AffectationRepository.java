package com.example.SIGR.repository;

import com.example.SIGR.entity.Affectation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AffectationRepository extends JpaRepository<Affectation, String> {

    /**
     * Vérifie si une affectation existe par son code
     */
    boolean existsByCode(String code);

    /**
     * Recherche une affectation par son code
     */
    Optional<Affectation> findByCode(String code);
}
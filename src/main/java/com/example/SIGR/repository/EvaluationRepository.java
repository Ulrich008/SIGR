package com.example.SIGR.repository;

import com.example.SIGR.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, String> {

    // Recherche par code métier
    Optional<Evaluation> findByCode(String code);

    // Vérifier unicité du code
    boolean existsByCode(String code);


}
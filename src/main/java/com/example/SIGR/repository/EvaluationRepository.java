package com.example.SIGR.repository;

import com.example.SIGR.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, String> {

    boolean existsById(String id);
}
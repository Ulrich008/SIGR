package com.example.SIGR.repository;

import com.example.SIGR.entity.CartographieRisques;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartographieRisquesRepository extends JpaRepository<CartographieRisques, String> {

    boolean existsByTitre(String titre);
}
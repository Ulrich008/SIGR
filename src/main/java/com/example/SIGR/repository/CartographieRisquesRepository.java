package com.example.SIGR.repository;

import com.example.SIGR.entity.CartographieRisques;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartographieRisquesRepository extends JpaRepository<CartographieRisques, String> {

    boolean existsByTitre(String titre);

    boolean existsByCode(String code);

    Optional<CartographieRisques> findByCode(String code);
}
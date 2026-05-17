package com.example.SIGR.repository;

import com.example.SIGR.entity.RisqueResiduel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RisqueResiduelRepository
        extends JpaRepository<RisqueResiduel, String> {

    /**
     * ================= CODE METIER =================
     */
    Optional<RisqueResiduel> findByCode(
            String code
    );

    boolean existsByCode(
            String code
    );

    /**
     * ================= PAR EVALUATION =================
     */
    List<RisqueResiduel> findByEvaluation_Code(
            String codeEvaluation
    );

    /**
     * ================= PAR RISQUE =================
     */
    List<RisqueResiduel> findByRisque_Code(
            String codeRisque
    );
}
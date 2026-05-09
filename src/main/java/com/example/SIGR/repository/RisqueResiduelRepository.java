package com.example.SIGR.repository;

import com.example.SIGR.entity.RisqueResiduel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RisqueResiduelRepository extends JpaRepository<RisqueResiduel, String> {

    Optional<RisqueResiduel> findByCode(String code);

    boolean existsByCode(String code);

    List<RisqueResiduel> findByEvaluation_Id(String idEvaluation);

    List<RisqueResiduel> findByRisque_Id(String idRisque);
}
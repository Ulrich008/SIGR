package com.example.SIGR.repository;

import com.example.SIGR.entity.IndicateurPerformance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicateurPerformanceRepository extends JpaRepository<IndicateurPerformance, String> {

    boolean existsByLibelle(String libelle);

    boolean existsByCode(String code);
}
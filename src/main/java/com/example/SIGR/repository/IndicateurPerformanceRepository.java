package com.example.SIGR.repository;

import com.example.SIGR.entity.IndicateurPerformance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndicateurPerformanceRepository extends JpaRepository<IndicateurPerformance, String> {

    boolean existsByCode(String code);

    boolean existsByLibelleIgnoreCase(String libelle);

    boolean existsByLibelleIgnoreCaseAndCodeNot(String libelle, String code);

    Optional<IndicateurPerformance> findByCode(String code);



    List<IndicateurPerformance> findByProcessus_Code(String codeProcessus);

    List<IndicateurPerformance> findByRisque_Code(String codeRisque);

    long countByProcessus_Unite_Code(String codeUnite);
}
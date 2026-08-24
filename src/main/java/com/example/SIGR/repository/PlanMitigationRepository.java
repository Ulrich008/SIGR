package com.example.SIGR.repository;

import com.example.SIGR.entity.PlanMitigation;
import com.example.SIGR.entity.Risque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanMitigationRepository extends JpaRepository<PlanMitigation, String> {

    boolean existsByCode(String code);

    Optional<PlanMitigation> findTopByOrderByIdDesc();

    Optional<PlanMitigation> findByCode(String code);

    boolean existsByLibelle(String libelle);
    boolean existsByLibelleAndCodeNot(String libelle, String code);

    List<PlanMitigation> findByRisquesContaining(Risque risque);

    @Query("SELECT COUNT(DISTINCT pm) FROM PlanMitigation pm JOIN pm.risques r WHERE r.processus.unite.code = :codeUnite")
    long countByRisque_Processus_Unite_Code(@Param("codeUnite") String codeUnite);
}
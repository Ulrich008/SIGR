package com.example.SIGR.repository;

import com.example.SIGR.entity.PlanAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanAuditRepository extends JpaRepository<PlanAudit, String> {

    boolean existsByCode(String code);

    boolean existsByLibelle(String libelle);

    boolean existsByLibelleAndCodeNot(String libelle, String code);

    Optional<PlanAudit> findByCode(String code);
}

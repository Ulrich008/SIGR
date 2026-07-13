package com.example.SIGR.repository;

import com.example.SIGR.entity.Action;
import com.example.SIGR.entity.PlanMitigation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActionRepository extends JpaRepository<Action, String> {

    // ================= BUSINESS KEY =================
    Optional<Action> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByLibelle(String libelle);
    
    List<Action> findByPlanMitigation(PlanMitigation planMitigation);

    List<Action> findByCodeRisque(String codeRisque);

    long countByPlanMitigation_Risque_Processus_Unite_Code(String codeUnite);
}
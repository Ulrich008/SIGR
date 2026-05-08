package com.example.SIGR.repository;

import com.example.SIGR.entity.PlanMitigation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanMitigationRepository extends JpaRepository<PlanMitigation, String> {

    boolean existsById(String id);
}
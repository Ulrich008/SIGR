package com.example.SIGR.repository;

import com.example.SIGR.entity.PlanMitigation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanMitigationRepository extends JpaRepository<PlanMitigation, String> {

    boolean existsByCode(String code);

    Optional<PlanMitigation> findByCode(String code);
}
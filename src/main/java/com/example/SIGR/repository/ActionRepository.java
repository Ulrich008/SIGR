package com.example.SIGR.repository;

import com.example.SIGR.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActionRepository extends JpaRepository<Action, String> {

    // ================= BUSINESS KEY =================
    Optional<Action> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByLibelle(String libelle);
}
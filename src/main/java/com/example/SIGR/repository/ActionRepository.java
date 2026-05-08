package com.example.SIGR.repository;

import com.example.SIGR.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, String> {

    boolean existsByLibelle(String libelle);

    boolean existsById(String id);
}
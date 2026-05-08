package com.example.SIGR.repository;

import com.example.SIGR.entity.Risque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RisqueRepository extends JpaRepository<Risque, String> {

    boolean existsByLibelle(String libelle);

    boolean existsById(String id);
}
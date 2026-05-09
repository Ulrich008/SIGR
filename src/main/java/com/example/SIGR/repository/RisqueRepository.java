package com.example.SIGR.repository;

import com.example.SIGR.entity.Risque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RisqueRepository extends JpaRepository<Risque, String> {

    boolean existsByCode(String code);

    Optional<Risque> findByCode(String code);

    boolean existsByLibelleIgnoreCase(String libelle);

    Optional<Risque> findByLibelleIgnoreCase(String libelle);
}
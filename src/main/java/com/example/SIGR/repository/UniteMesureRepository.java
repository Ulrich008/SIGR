package com.example.SIGR.repository;

import com.example.SIGR.entity.UniteMesure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniteMesureRepository extends JpaRepository<UniteMesure, String> {

    boolean existsByCode(String code);

    boolean existsByLibelleIgnoreCase(String libelle);

    boolean existsByLibelleIgnoreCaseAndCodeNot(String libelle, String code);

    Optional<UniteMesure> findByCode(String code);
}

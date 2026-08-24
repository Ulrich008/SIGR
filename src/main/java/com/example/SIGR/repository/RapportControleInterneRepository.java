package com.example.SIGR.repository;

import com.example.SIGR.entity.RapportControleInterne;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RapportControleInterneRepository extends JpaRepository<RapportControleInterne, String> {

    boolean existsByCode(String code);

    Optional<RapportControleInterne> findByCode(String code);

    long countByUniteAdministrative_Code(String codeUnite);
}

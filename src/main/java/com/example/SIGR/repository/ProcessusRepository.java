package com.example.SIGR.repository;

import com.example.SIGR.entity.Processus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessusRepository extends JpaRepository<Processus, String> {

    boolean existsByCode(String code);

    boolean existsByLibelle(String libelle);

    boolean existsByLibelleAndCodeNot(String libelle, String code);

    Optional<Processus> findByCode(String code);

    long countByUnite_Code(String codeUnite);
}
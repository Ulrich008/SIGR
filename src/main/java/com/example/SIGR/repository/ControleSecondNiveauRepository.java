package com.example.SIGR.repository;

import com.example.SIGR.entity.ControleSecondNiveau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ControleSecondNiveauRepository extends JpaRepository<ControleSecondNiveau, String> {

    boolean existsByCode(String code);

    Optional<ControleSecondNiveau> findByCode(String code);

    long countByUniteAdministrative_Code(String codeUnite);

    List<ControleSecondNiveau> findByUniteAdministrative_CodeAndProcessus_Code(String codeUnite, String codeProcessus);
}

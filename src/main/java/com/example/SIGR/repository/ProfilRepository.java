package com.example.SIGR.repository;

import com.example.SIGR.entity.Profil;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfilRepository extends JpaRepository<Profil, String> {

    Optional<Profil> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByLibelleIgnoreCase(String libelle);

    boolean existsByLibelleIgnoreCaseAndCodeNot(String libelle, String code);
}
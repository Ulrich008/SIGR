package com.example.SIGR.repository;

import com.example.SIGR.entity.TypeUnite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeUniteRepository extends JpaRepository<TypeUnite, String> {

    boolean existsByCode(String code);

    Optional<TypeUnite> findByCode(String code);
}
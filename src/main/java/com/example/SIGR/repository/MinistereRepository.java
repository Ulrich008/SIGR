package com.example.SIGR.repository;

import com.example.SIGR.entity.Ministere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MinistereRepository extends JpaRepository<Ministere, String> {

    boolean existsByNom(String nom);

    boolean existsByCode(String code);

    Optional<Ministere> findByCode(String code);
}
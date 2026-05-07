package com.example.SIGR.repository;

import com.example.SIGR.entity.Ministere;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MinistereRepository extends JpaRepository<Ministere, String> {

    boolean existsByNom(String nom);

    boolean existsByCode(String code);
}
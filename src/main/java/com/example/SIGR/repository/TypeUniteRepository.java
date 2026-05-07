package com.example.SIGR.repository;

import com.example.SIGR.entity.TypeUnite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeUniteRepository extends JpaRepository<TypeUnite, String> {

    boolean existsById(String id);
}
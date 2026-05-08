package com.example.SIGR.repository;

import com.example.SIGR.entity.UniteAdministrative;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniteAdministrativeRepository extends JpaRepository<UniteAdministrative, String> {
    boolean existsById(String id);
}










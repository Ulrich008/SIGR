package com.example.SIGR.repository;

import com.example.SIGR.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, String> {

    Optional<Mission> findByCode(String code);

    boolean existsByCode(String code);

    List<Mission> findByProcessusId(String processusId);
}

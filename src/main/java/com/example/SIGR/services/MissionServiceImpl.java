package com.example.SIGR.services;

import com.example.SIGR.dto.request.MissionRequest;
import com.example.SIGR.dto.response.MissionResponse;
import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.Mission;
import com.example.SIGR.entity.Processus;
import com.example.SIGR.entity.StatutMission;
import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.MissionRepository;
import com.example.SIGR.repository.ProcessusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final ProcessusRepository processusRepository;
    private final AgentRepository agentRepository;

    public MissionServiceImpl(MissionRepository missionRepository,
                              ProcessusRepository processusRepository,
                              AgentRepository agentRepository) {
        this.missionRepository = missionRepository;
        this.processusRepository = processusRepository;
        this.agentRepository = agentRepository;
    }

    private String generateCode() {
        long count = missionRepository.count() + 1;
        return String.format("MISS-%03d", count);
    }

    @Override
    public MissionResponse create(MissionRequest request) {
        Processus processus = processusRepository.findByCode(request.getCodeProcessus())
                .orElseThrow(() -> new RuntimeException("Processus introuvable : " + request.getCodeProcessus()));

        Agent responsable = null;
        if (request.getCodeResponsable() != null) {
            responsable = agentRepository.findByMatricule(request.getCodeResponsable())
                    .orElseThrow(() -> new RuntimeException("Agent introuvable : " + request.getCodeResponsable()));
        }

        String code = generateCode();

        Mission entity = new Mission()
                .setCode(code)
                .setLibelle(request.getLibelle())
                .setDescription(request.getDescription())
                .setProcessus(processus)
                .setDateDebut(request.getDateDebut())
                .setDateFin(request.getDateFin())
                .setStatut(request.getStatut() != null ? StatutMission.valueOf(request.getStatut()) : StatutMission.ACTIF)
                .setResponsable(responsable);

        return toResponse(missionRepository.save(entity));
    }

    @Override
    public MissionResponse getByCode(String code) {
        Mission entity = missionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Mission introuvable : " + code));
        return toResponse(entity);
    }

    @Override
    public List<MissionResponse> getAll() {
        return missionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MissionResponse update(String code, MissionRequest request) {
        Mission entity = missionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Mission introuvable : " + code));

        // Vérification unicité code
        if (!entity.getCode().equals(request.getCode()) && missionRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Code déjà utilisé : " + request.getCode());
        }

        Processus processus = processusRepository.findByCode(request.getCodeProcessus())
                .orElseThrow(() -> new RuntimeException("Processus introuvable : " + request.getCodeProcessus()));

        Agent responsable = null;
        if (request.getCodeResponsable() != null) {
            responsable = agentRepository.findByMatricule(request.getCodeResponsable())
                    .orElseThrow(() -> new RuntimeException("Agent introuvable : " + request.getCodeResponsable()));
        }

        entity.setCode(request.getCode());
        entity.setLibelle(request.getLibelle());
        entity.setDescription(request.getDescription());
        entity.setProcessus(processus);
        entity.setDateDebut(request.getDateDebut());
        entity.setDateFin(request.getDateFin());
        if (request.getStatut() != null) {
            entity.setStatut(StatutMission.valueOf(request.getStatut()));
        }
        entity.setResponsable(responsable);

        return toResponse(missionRepository.save(entity));
    }

    @Override
    public void delete(String code) {
        Mission entity = missionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Mission introuvable : " + code));
        missionRepository.delete(entity);
    }

    @Override
    public List<MissionResponse> getByProcessusId(String processusId) {
        return missionRepository.findByProcessusId(processusId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private MissionResponse toResponse(Mission entity) {
        return new MissionResponse(
                entity.getId(),
                entity.getCode(),
                entity.getLibelle(),
                entity.getDescription(),
                entity.getProcessus() != null ? entity.getProcessus().getCode() : null,
                entity.getProcessus() != null ? entity.getProcessus().getLibelle() : null,
                entity.getDateDebut(),
                entity.getDateFin(),
                entity.getStatut() != null ? entity.getStatut().name() : null,
                entity.getResponsable() != null ? entity.getResponsable().getMatricule() : null,
                entity.getResponsable() != null ? entity.getResponsable().getNom() + " " + entity.getResponsable().getPrenoms() : null
        );
    }
}

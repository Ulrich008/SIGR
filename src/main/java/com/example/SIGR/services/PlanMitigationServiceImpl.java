package com.example.SIGR.services;

import com.example.SIGR.dto.request.PlanMitigationRequest;
import com.example.SIGR.dto.response.PlanMitigationResponse;

import com.example.SIGR.entity.PlanMitigation;
import com.example.SIGR.entity.Risque;

import com.example.SIGR.repository.PlanMitigationRepository;
import com.example.SIGR.repository.RisqueRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanMitigationServiceImpl implements PlanMitigationService {

    private final PlanMitigationRepository repository;
    private final RisqueRepository risqueRepository;

    public PlanMitigationServiceImpl(
            PlanMitigationRepository repository,
            RisqueRepository risqueRepository
    ) {
        this.repository = repository;
        this.risqueRepository = risqueRepository;
    }

    // ================= CREATE =================
    @Override
    public PlanMitigationResponse create(PlanMitigationRequest request) {

        // ================= RISQUE =================
        Risque risque = risqueRepository.findByCode(request.getCodeRisque())
                .orElseThrow(() ->
                        new RuntimeException("Risque introuvable : " + request.getCodeRisque())
                );

        // ================= GENERATION CODE ROBUSTE =================
        String lastCode = repository.findTopByOrderByIdDesc()
                .map(PlanMitigation::getCode)
                .orElse("PLAN-000");

        int nextNumber = Integer.parseInt(lastCode.replace("PLAN-", "")) + 1;

        String code;
        do {
            code = String.format("PLAN-%03d", nextNumber++);
        } while (repository.existsByCode(code));

        // ================= CREATION =================
        PlanMitigation plan = new PlanMitigation();

        plan.setCode(code);
        plan.setDescription(request.getDescription());
        plan.setDateCreation(request.getDateCreation());
        plan.setStatut(request.getStatut());
        plan.setRisque(risque);

        return toResponse(repository.save(plan));
    }

    // ================= GET BY CODE =================
    @Override
    public PlanMitigationResponse getByCode(String code) {

        PlanMitigation plan = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Plan introuvable : " + code)
                );

        return toResponse(plan);
    }

    // ================= GET ALL =================
    @Override
    public List<PlanMitigationResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @Override
    public PlanMitigationResponse updateByCode(String code, PlanMitigationRequest request) {

        PlanMitigation plan = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Plan introuvable : " + code)
                );

        Risque risque = risqueRepository.findByCode(request.getCodeRisque())
                .orElseThrow(() ->
                        new RuntimeException("Risque introuvable : " + request.getCodeRisque())
                );

        plan.setDescription(request.getDescription());
        plan.setDateCreation(request.getDateCreation());
        plan.setStatut(request.getStatut());
        plan.setRisque(risque);

        return toResponse(repository.save(plan));
    }

    // ================= DELETE =================
    @Override
    public void deleteByCode(String code) {

        PlanMitigation plan = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Plan introuvable : " + code)
                );

        repository.delete(plan);
    }

    // ================= RESPONSE =================
    private PlanMitigationResponse toResponse(PlanMitigation plan) {

        return new PlanMitigationResponse(
                plan.getId(),
                plan.getCode(),
                plan.getDescription(),
                plan.getDateCreation(),
                plan.getStatut(),
                plan.getRisque() != null ? plan.getRisque().getCode() : null,
                plan.getRisque() != null ? plan.getRisque().getLibelle() : null
        );
    }
}
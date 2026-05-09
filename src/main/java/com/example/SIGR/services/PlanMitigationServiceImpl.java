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

    @Override
    public PlanMitigationResponse create(PlanMitigationRequest request) {

        // 🔥 vérification sur CODE (pas id)
        if (request.getCode() != null && repository.existsByCode(request.getCode())) {
            throw new RuntimeException("Code déjà utilisé : " + request.getCode());
        }

        Risque risque = risqueRepository.findById(request.getIdRisque())
                .orElseThrow(() ->
                        new RuntimeException("Risque introuvable : " + request.getIdRisque())
                );

        PlanMitigation plan = new PlanMitigation()
                .setCode(request.getCode())
                .setDescription(request.getDescription())
                .setDateCreation(request.getDateCreation())
                .setStatut(request.getStatut())
                .setRisque(risque);

        return toResponse(repository.save(plan));
    }

    @Override
    public PlanMitigationResponse getById(String id) {

        PlanMitigation plan = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Plan introuvable : " + id)
                );

        return toResponse(plan);
    }
    @Override
    public  PlanMitigationResponse getByCode(String code){

        PlanMitigation plan = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Plan Introuvable " +  code)
                );
        return  toResponse(plan);
    }
    @Override
    public List<PlanMitigationResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PlanMitigationResponse updateById(String id, PlanMitigationRequest request) {

        PlanMitigation plan = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Plan introuvable : " + id)
                );

        Risque risque = risqueRepository.findById(request.getIdRisque())
                .orElseThrow(() ->
                        new RuntimeException("Risque introuvable : " + request.getIdRisque())
                );

        plan.setCode(request.getCode());
        plan.setDescription(request.getDescription());
        plan.setDateCreation(request.getDateCreation());
        plan.setStatut(request.getStatut());
        plan.setRisque(risque);

        return toResponse(repository.save(plan));
    }

    @Override
    public void deleteById(String id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Plan introuvable : " + id);
        }

        repository.deleteById(id);
    }

    private PlanMitigationResponse toResponse(PlanMitigation plan) {

        return new PlanMitigationResponse(
                plan.getId(),
                plan.getCode(),
                plan.getDescription(),
                plan.getDateCreation(),
                plan.getStatut(),
                plan.getRisque().getId(),
                plan.getRisque().getLibelle()
        );
    }
}
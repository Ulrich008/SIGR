package com.example.SIGR.services;

import com.example.SIGR.dto.request.AffectationRequest;
import com.example.SIGR.dto.response.AffectationResponse;
import com.example.SIGR.entity.Affectation;
import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.UniteAdministrative;
import com.example.SIGR.repository.AffectationRepository;
import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.UniteAdministrativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AffectationServiceImpl implements AffectationService {

    private final AffectationRepository affectationRepository;
    private final AgentRepository agentRepository;
    private final UniteAdministrativeRepository uniteRepository;

    public AffectationServiceImpl(AffectationRepository affectationRepository,
                                  AgentRepository agentRepository,
                                  UniteAdministrativeRepository uniteRepository) {
        this.affectationRepository = affectationRepository;
        this.agentRepository = agentRepository;
        this.uniteRepository = uniteRepository;
    }

    @Override
    public AffectationResponse create(AffectationRequest request) {

        if (affectationRepository.existsByCode(request.getCode())) {
            throw new RuntimeException(
                    "Une affectation avec ce code existe déjà : "
                            + request.getCode()
            );
        }

        Agent agent = agentRepository
                .findByMatricule(request.getMatriculeAgent())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent introuvable : "
                                        + request.getMatriculeAgent()
                        )
                );

        UniteAdministrative unite = uniteRepository
                .findByCode(request.getCodeUnite())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Unité introuvable : "
                                        + request.getCodeUnite()
                        )
                );

        Affectation affectation = new Affectation();

        affectation.setCode(request.getCode());
        affectation.setAgent(agent);
        affectation.setUnite(unite);
        affectation.setPoste(request.getPoste());
        affectation.setDateAffectation(request.getDateAffectation());
        affectation.setDateFinAffectation(request.getDateFinAffectation());

        Affectation saved = affectationRepository.save(affectation);

        return toResponse(saved);
    }

    @Override
    public AffectationResponse getByCode(String code) {

        Affectation affectation = affectationRepository
                .findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Affectation introuvable : " + code
                        )
                );

        return toResponse(affectation);
    }

    @Override
    public List<AffectationResponse> getAll() {

        return affectationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AffectationResponse update(String code,
                                      AffectationRequest request) {

        Affectation affectation = affectationRepository
                .findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Affectation introuvable : " + code
                        )
                );

        if (request.getMatriculeAgent() != null) {

            Agent agent = agentRepository
                    .findByMatricule(request.getMatriculeAgent())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Agent introuvable"
                            )
                    );

            affectation.setAgent(agent);
        }

        if (request.getCodeUnite() != null) {

            UniteAdministrative unite = uniteRepository
                    .findByCode(request.getCodeUnite())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Unité introuvable"
                            )
                    );

            affectation.setUnite(unite);
        }

        if (request.getPoste() != null) {
            affectation.setPoste(request.getPoste());
        }

        if (request.getDateAffectation() != null) {
            affectation.setDateAffectation(request.getDateAffectation());
        }

        if (request.getDateFinAffectation() != null) {
            affectation.setDateFinAffectation(
                    request.getDateFinAffectation()
            );
        }

        Affectation updated = affectationRepository.save(affectation);

        return toResponse(updated);
    }

    @Override
    public void delete(String code) {

        Affectation affectation = affectationRepository
                .findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Affectation introuvable : " + code
                        )
                );

        affectationRepository.delete(affectation);
    }

    // ================= MAPPING RESPONSE =================

    private AffectationResponse toResponse(Affectation affectation) {

        return new AffectationResponse(
                affectation.getId(),
                affectation.getCode(),

                affectation.getAgent() != null
                        ? affectation.getAgent().getMatricule()
                        : null,

                affectation.getAgent() != null
                        ? affectation.getAgent().getNom()
                          + " "
                          + affectation.getAgent().getPrenoms()
                        : null,

                affectation.getUnite() != null
                        ? affectation.getUnite().getCode()
                        : null,

                affectation.getUnite() != null
                        ? affectation.getUnite().getLibelle()
                        : null,

                affectation.getPoste(),
                affectation.getDateAffectation(),
                affectation.getDateFinAffectation()
        );
    }
}
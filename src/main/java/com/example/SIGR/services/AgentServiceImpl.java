package com.example.SIGR.services;

import com.example.SIGR.dto.request.AgentRequest;
import com.example.SIGR.dto.response.AgentResponse;
import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.UniteAdministrative;
import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.UniteAdministrativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final UniteAdministrativeRepository uniteRepository;

    public AgentServiceImpl(AgentRepository agentRepository,
                            UniteAdministrativeRepository uniteRepository) {
        this.agentRepository = agentRepository;
        this.uniteRepository = uniteRepository;
    }

    @Override
    public AgentResponse create(AgentRequest request) {

        if (agentRepository.existsByMatricule(request.getMatricule())) {
            throw new RuntimeException(
                    "Un agent avec ce matricule existe déjà : "
                            + request.getMatricule()
            );
        }

        UniteAdministrative unite = uniteRepository
                .findByCode(request.getCodeUnite())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Unité introuvable : "
                                        + request.getCodeUnite()
                        )
                );

        Agent agent = new Agent();

        agent.setMatricule(request.getMatricule());
        agent.setNpi(request.getNpi());
        agent.setNom(request.getNom());
        agent.setPrenoms(request.getPrenoms());
        agent.setSexe(request.getSexe());
        agent.setRole(request.getRole());
        agent.setDateNaissance(request.getDateNaissance());
        agent.setDatePriseService(request.getDatePriseService());
        agent.setUnite(unite);

        Agent saved = agentRepository.save(agent);

        return toResponse(saved);
    }

    @Override
    public AgentResponse getByMatricule(String matricule) {

        Agent agent = agentRepository
                .findByMatricule(matricule)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent introuvable : " + matricule
                        )
                );

        return toResponse(agent);
    }

    @Override
    public List<AgentResponse> getAll() {

        return agentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AgentResponse update(String matricule, AgentRequest request) {

        Agent agent = agentRepository
                .findByMatricule(matricule)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent introuvable : " + matricule
                        )
                );

        if (request.getNpi() != null) {
            agent.setNpi(request.getNpi());
        }

        if (request.getNom() != null) {
            agent.setNom(request.getNom());
        }

        if (request.getPrenoms() != null) {
            agent.setPrenoms(request.getPrenoms());
        }

        if (request.getSexe() != null) {
            agent.setSexe(request.getSexe());
        }

        if (request.getRole() != null) {
            agent.setRole(request.getRole());
        }

        if (request.getDateNaissance() != null) {
            agent.setDateNaissance(request.getDateNaissance());
        }

        if (request.getDatePriseService() != null) {
            agent.setDatePriseService(request.getDatePriseService());
        }

        if (request.getCodeUnite() != null) {

            UniteAdministrative unite = uniteRepository
                    .findByCode(request.getCodeUnite())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Unité introuvable : "
                                            + request.getCodeUnite()
                            )
                    );

            agent.setUnite(unite);
        }

        Agent updated = agentRepository.save(agent);

        return toResponse(updated);
    }

    @Override
    public void delete(String matricule) {

        Agent agent = agentRepository
                .findByMatricule(matricule)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent introuvable : " + matricule
                        )
                );

        agentRepository.delete(agent);
    }

    // ================= MAPPING RESPONSE =================

    private AgentResponse toResponse(Agent agent) {

        return new AgentResponse(
                agent.getId(),
                agent.getMatricule(),
                agent.getNpi(),
                agent.getNom(),
                agent.getPrenoms(),
                agent.getSexe(),
                agent.getRole(),
                agent.getDateNaissance(),
                agent.getDatePriseService(),
                agent.getUnite() != null
                        ? agent.getUnite().getCode()
                        : null,
                agent.getUnite() != null
                        ? agent.getUnite().getLibelle()
                        : null
        );
    }
}
package com.example.SIGR.services;

import com.example.SIGR.dto.request.AgentRequest;
import com.example.SIGR.dto.response.AgentResponse;
import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.UniteAdministrative;
import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.UniteAdministrativeRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final UniteAdministrativeRepository uniteRepository;
    private final PasswordEncoder passwordEncoder;

    public AgentServiceImpl(
            AgentRepository agentRepository,
            UniteAdministrativeRepository uniteRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.agentRepository = agentRepository;
        this.uniteRepository = uniteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AgentResponse create(AgentRequest request) {

        // ================= VALIDATIONS =================

        if (request.getMatricule() == null
                || request.getMatricule().isBlank()) {

            throw new RuntimeException(
                    "Le matricule est obligatoire"
            );
        }

        if (request.getPassword() == null
                || request.getPassword().isBlank()) {

            throw new RuntimeException(
                    "Le mot de passe est obligatoire"
            );
        }

        if (agentRepository.existsByMatricule(request.getMatricule())) {

            throw new RuntimeException(
                    "Un agent avec ce matricule existe déjà : "
                            + request.getMatricule()
            );
        }

        if (request.getNpi() != null
                && !request.getNpi().isBlank()
                && agentRepository.existsByNpi(request.getNpi())) {

            throw new RuntimeException(
                    "Un agent avec ce NPI existe déjà : "
                            + request.getNpi()
            );
        }

        // ================= UNITE =================

        UniteAdministrative unite = uniteRepository
                .findByCode(request.getCodeUnite())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Unité introuvable : "
                                        + request.getCodeUnite()
                        )
                );

        // ================= CREATION =================

        Agent agent = new Agent();

        agent.setMatricule(request.getMatricule());

        agent.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        agent.setEnabled(true);

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
    public AgentResponse getById(String id) {

        Agent agent = agentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent introuvable : " + id
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

        // ================= NPI =================

        if (request.getNpi() != null
                && !request.getNpi().isBlank()) {

            boolean npiExiste = agentRepository
                    .existsByNpi(request.getNpi());

            if (npiExiste
                    && !request.getNpi().equals(agent.getNpi())) {

                throw new RuntimeException(
                        "Ce NPI est déjà utilisé : "
                                + request.getNpi()
                );
            }

            agent.setNpi(request.getNpi());
        }

        // ================= PASSWORD =================

        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            agent.setPassword(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }

        // ================= AUTRES CHAMPS =================

        if (request.getNom() != null
                && !request.getNom().isBlank()) {

            agent.setNom(request.getNom());
        }

        if (request.getPrenoms() != null
                && !request.getPrenoms().isBlank()) {

            agent.setPrenoms(request.getPrenoms());
        }

        if (request.getSexe() != null) {
            agent.setSexe(request.getSexe());
        }

        if (request.getRole() != null) {
            agent.setRole(request.getRole());
        }

        if (request.getDateNaissance() != null) {
            agent.setDateNaissance(
                    request.getDateNaissance()
            );
        }

        if (request.getDatePriseService() != null) {
            agent.setDatePriseService(
                    request.getDatePriseService()
            );
        }

        // ================= UNITE =================

        if (request.getCodeUnite() != null
                && !request.getCodeUnite().isBlank()) {

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
    public AgentResponse changeStatus(
            String matricule,
            Boolean enabled
    ) {

        Agent agent = agentRepository
                .findByMatricule(matricule)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent introuvable : "
                                        + matricule
                        )
                );

        agent.setEnabled(enabled);

        Agent updated = agentRepository.save(agent);

        return toResponse(updated);
    }

    @Override
    public void delete(String matricule) {

        Agent agent = agentRepository
                .findByMatricule(matricule)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent introuvable : "
                                        + matricule
                        )
                );

        agentRepository.delete(agent);
    }

    // ================= RESPONSE =================

    private AgentResponse toResponse(Agent agent) {

        return new AgentResponse(
                agent.getId(),
                agent.getMatricule(),
                agent.getNpi(),
                agent.getNom(),
                agent.getPrenoms(),
                agent.getSexe(),
                agent.getRole(),
                agent.getEnabled(),
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
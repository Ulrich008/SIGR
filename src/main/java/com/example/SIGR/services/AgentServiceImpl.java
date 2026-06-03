package com.example.SIGR.services;

import com.example.SIGR.dto.request.AgentRequest;
import com.example.SIGR.dto.response.AgentResponse;
import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.Ministere;
import com.example.SIGR.entity.Profil;
import com.example.SIGR.entity.UniteAdministrative;
import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.MinistereRepository;
import com.example.SIGR.repository.ProfilRepository;
import com.example.SIGR.repository.UniteAdministrativeRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final UniteAdministrativeRepository uniteRepository;
    private final ProfilRepository profilRepository;
    private final MinistereRepository ministereRepository;
    private final PasswordEncoder passwordEncoder;

    public AgentServiceImpl(
            AgentRepository agentRepository,
            UniteAdministrativeRepository uniteRepository,
            ProfilRepository profilRepository,
            MinistereRepository ministereRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.agentRepository = agentRepository;
        this.uniteRepository = uniteRepository;
        this.profilRepository = profilRepository;
        this.ministereRepository = ministereRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AgentResponse create(AgentRequest request) {

        // ================= PASSWORD =================

        if (request.getPassword() == null
                || request.getPassword().isBlank()) {

            throw new RuntimeException(
                    "Le mot de passe est obligatoire"
            );
        }

        // ================= NPI =================

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

        // ================= PROFIL =================

        Profil profil = profilRepository
                .findByCode(request.getCodeProfil())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Profil introuvable : "
                                        + request.getCodeProfil()
                        )
                );

        // ================= MINISTERE =================

        Ministere ministere = null;
        if (request.getCodeMinistere() != null
                && !request.getCodeMinistere().isBlank()) {
            ministere = ministereRepository
                    .findByCode(request.getCodeMinistere())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Ministère introuvable : "
                                            + request.getCodeMinistere()
                            )
                    );
        }

        // ================= GENERATION MATRICULE =================

        long total = agentRepository.count() + 1;

        String matricule = String.format(
                "AGT-%03d",
                total
        );

        while (agentRepository.existsByMatricule(matricule)) {

            total++;

            matricule = String.format(
                    "AGT-%03d",
                    total
            );
        }

        // ================= CREATION =================

        Agent agent = new Agent();

        agent.setMatricule(matricule);

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

        // ================= PROFIL =================

        agent.setProfil(profil);

        // ================= MINISTERE =================

        agent.setMinistere(ministere);

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

        // ================= PROFIL =================

        if (request.getCodeProfil() != null
                && !request.getCodeProfil().isBlank()) {

            Profil profil = profilRepository
                    .findByCode(request.getCodeProfil())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Profil introuvable : "
                                            + request.getCodeProfil()
                            )
                    );

            agent.setProfil(profil);
        }

        // ================= MINISTERE =================

        if (request.getCodeMinistere() != null
                && !request.getCodeMinistere().isBlank()) {

            Ministere ministere = ministereRepository
                    .findByCode(request.getCodeMinistere())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Ministère introuvable : "
                                            + request.getCodeMinistere()
                            )
                    );

            agent.setMinistere(ministere);
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
                agent.getProfil() != null
                        ? agent.getProfil().getCode()
                        : null,
                agent.getProfil() != null
                        ? agent.getProfil().getLibelle()
                        : null,
                agent.getEnabled(),
                agent.getDateNaissance(),
                agent.getDatePriseService(),
                agent.getUnite() != null
                        ? agent.getUnite().getCode()
                        : null,
                agent.getUnite() != null
                        ? agent.getUnite().getLibelle()
                        : null,
                agent.getMinistere() != null
                        ? agent.getMinistere().getCode()
                        : null,
                agent.getMinistere() != null
                        ? agent.getMinistere().getNom()
                        : null
        );
    }
}
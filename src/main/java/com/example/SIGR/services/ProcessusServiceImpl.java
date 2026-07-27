package com.example.SIGR.services;

import com.example.SIGR.dto.request.ProcessusRequest;
import com.example.SIGR.dto.response.ProcessusResponse;
import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.Processus;
import com.example.SIGR.entity.UniteAdministrative;
import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.ProcessusRepository;
import com.example.SIGR.repository.UniteAdministrativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessusServiceImpl implements ProcessusService {

    private final ProcessusRepository processusRepository;
    private final UniteAdministrativeRepository uniteRepository;
    private final AgentRepository agentRepository;

    public ProcessusServiceImpl(
            ProcessusRepository processusRepository,
            UniteAdministrativeRepository uniteRepository,
            AgentRepository agentRepository
    ) {
        this.processusRepository = processusRepository;
        this.uniteRepository = uniteRepository;
        this.agentRepository = agentRepository;
    }

    // ================= CREATE =================
    @Override
    public ProcessusResponse create(ProcessusRequest request) {
        // ✅ Vérification doublon libellé à la création
        if (processusRepository.existsByLibelle(request.getLibelle())) {
            throw new RuntimeException(
                    "Un processus avec ce libellé existe déjà : " + request.getLibelle()
            );
        }

        UniteAdministrative unite = uniteRepository.findByCode(request.getIdUnite())
                .orElseThrow(() ->
                        new RuntimeException("Unité introuvable : " + request.getIdUnite())
                );

        Agent proprietaire = agentRepository.findByMatricule(request.getIdProprietaire())
                .orElseThrow(() ->
                        new RuntimeException("Agent introuvable : " + request.getIdProprietaire())
                );
        verifierProprietaireEstPilote(proprietaire);

        // ================= GENERATION CODE =================
        // Format : P_<sigleUA><séquence sur 3 chiffres>, séquence propre à l'UA
        String sigleUnite = unite.getCode();
        long count = processusRepository.countByUnite_Code(sigleUnite) + 1;
        String code = "P_" + sigleUnite + String.format("%03d", count);

        while (processusRepository.existsByCode(code)) {
            count++;
            code = "P_" + sigleUnite + String.format("%03d", count);
        }

        Processus processus = new Processus();
        processus.setCode(code);
        processus.setLibelle(request.getLibelle());
        processus.setFinalite(request.getFinalite());
        processus.setTypeProcessus(request.getTypeProcessus());
        processus.setUnite(unite);
        processus.setProprietaire(proprietaire);

        Processus saved = processusRepository.save(processus);
        return toResponse(saved);
    }

    // ================= GET BY CODE =================
    @Override
    public ProcessusResponse getByCode(String code) {
        Processus processus = processusRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Processus introuvable : " + code)
                );
        return toResponse(processus);
    }

    // ================= GET ALL =================
    @Override
    public List<ProcessusResponse> getAll() {
        return processusRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE BY CODE =================
    @Override
    public ProcessusResponse updateByCode(String code, ProcessusRequest request) {
        Processus processus = processusRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Processus introuvable : " + code)
                );

        // ✅ CORRECTION : vérifie si un AUTRE processus possède déjà ce libellé
        if (processusRepository.existsByLibelleAndCodeNot(request.getLibelle(), code)) {
            throw new RuntimeException(
                    "Un processus avec ce libellé existe déjà : " + request.getLibelle()
            );
        }

        UniteAdministrative unite = uniteRepository.findByCode(request.getIdUnite())
                .orElseThrow(() ->
                        new RuntimeException("Unité introuvable : " + request.getIdUnite())
                );

        Agent proprietaire = agentRepository.findByMatricule(request.getIdProprietaire())
                .orElseThrow(() ->
                        new RuntimeException("Agent introuvable : " + request.getIdProprietaire())
                );
        verifierProprietaireEstPilote(proprietaire);

        processus.setLibelle(request.getLibelle());
        processus.setFinalite(request.getFinalite());
        processus.setTypeProcessus(request.getTypeProcessus());
        processus.setUnite(unite);
        processus.setProprietaire(proprietaire);

        Processus updated = processusRepository.save(processus);
        return toResponse(updated);
    }

    // ================= DELETE BY CODE =================
    @Override
    public void deleteByCode(String code) {
        Processus processus = processusRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Processus introuvable : " + code)
                );
        processusRepository.delete(processus);
    }

    /**
     * Le propriétaire d'un processus doit obligatoirement avoir le profil
     * métier Pilote (de processus) : c'est ce profil qui est responsable
     * du pilotage du processus dont il est propriétaire.
     */
    private void verifierProprietaireEstPilote(Agent proprietaire) {
        if (proprietaire.getProfil() == null || !"PILOTE".equals(proprietaire.getProfil().getCode())) {
            throw new RuntimeException(
                    "Le propriétaire d'un processus doit avoir le profil Pilote de processus"
            );
        }
    }

    // ================= MAPPER =================
    private ProcessusResponse toResponse(Processus processus) {
        return new ProcessusResponse(
                processus.getId(),
                processus.getCode(),
                processus.getLibelle(),
                processus.getFinalite(),
                processus.getTypeProcessus(),
                processus.getUnite().getCode(),
                processus.getUnite().getLibelle(),
                processus.getProprietaire() != null
                        ? processus.getProprietaire().getMatricule()
                        : null,
                processus.getProprietaire() != null
                        ? processus.getProprietaire().getNom()
                        : null
        );
    }
}
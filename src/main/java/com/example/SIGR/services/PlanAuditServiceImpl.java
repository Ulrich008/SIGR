package com.example.SIGR.services;

import com.example.SIGR.dto.request.PlanAuditRequest;
import com.example.SIGR.dto.response.PlanAuditResponse;
import com.example.SIGR.entity.PlanAudit;
import com.example.SIGR.entity.Processus;
import com.example.SIGR.entity.Risque;
import com.example.SIGR.entity.UniteAdministrative;
import com.example.SIGR.repository.PlanAuditRepository;
import com.example.SIGR.repository.ProcessusRepository;
import com.example.SIGR.repository.RisqueRepository;
import com.example.SIGR.repository.UniteAdministrativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanAuditServiceImpl implements PlanAuditService {

    private final PlanAuditRepository planAuditRepository;
    private final UniteAdministrativeRepository uniteRepository;
    private final ProcessusRepository processusRepository;
    private final RisqueRepository risqueRepository;

    public PlanAuditServiceImpl(
            PlanAuditRepository planAuditRepository,
            UniteAdministrativeRepository uniteRepository,
            ProcessusRepository processusRepository,
            RisqueRepository risqueRepository
    ) {
        this.planAuditRepository = planAuditRepository;
        this.uniteRepository = uniteRepository;
        this.processusRepository = processusRepository;
        this.risqueRepository = risqueRepository;
    }

    // ================= CREATE =================
    @Override
    public PlanAuditResponse create(PlanAuditRequest request) {
        //  Vérification doublon libellé à la création
        if (planAuditRepository.existsByLibelle(request.getLibelle())) {
            throw new RuntimeException(
                    "Un plan d'audit avec ce libellé existe déjà : " + request.getLibelle()
            );
        }

        UniteAdministrative unite = uniteRepository.findByCode(request.getCodeUniteAdministrative())
                .orElseThrow(() ->
                        new RuntimeException("Unité administrative introuvable : " + request.getCodeUniteAdministrative())
                );

        Processus processus = processusRepository.findByCode(request.getCodeProcessus())
                .orElseThrow(() ->
                        new RuntimeException("Processus introuvable : " + request.getCodeProcessus())
                );

        Risque risque = null;
        if (request.getCodeRisque() != null && !request.getCodeRisque().isBlank()) {
            risque = risqueRepository.findByCode(request.getCodeRisque())
                    .orElseThrow(() ->
                            new RuntimeException("Risque introuvable : " + request.getCodeRisque())
                    );
        }

        PlanAudit planAudit = new PlanAudit();
        planAudit.setCode(generateCode(unite.getCode()));
        planAudit.setLibelle(request.getLibelle());
        planAudit.setDateCreation(request.getDateCreation());
        planAudit.setUniteAdministrative(unite);
        planAudit.setProcessus(processus);
        planAudit.setRisque(risque);
        planAudit.setAuditPropose(request.getAuditPropose());
        planAudit.setTypeRevue(request.getTypeRevue());
        planAudit.setObjectifAudit(request.getObjectifAudit());
        planAudit.setEffetAuditIndicatif(request.getEffetAuditIndicatif());

        PlanAudit saved = planAuditRepository.save(planAudit);

        return toResponse(saved);
    }

    // ================= GET ALL =================
    @Override
    public List<PlanAuditResponse> getAll() {
        return planAuditRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= GET BY CODE =================
    @Override
    public PlanAuditResponse getByCode(String code) {
        PlanAudit planAudit = planAuditRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Plan d'audit introuvable : " + code)
                );
        return toResponse(planAudit);
    }

    // ================= UPDATE =================
    @Override
    public PlanAuditResponse update(String code, PlanAuditRequest request) {
        PlanAudit planAudit = planAuditRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Plan d'audit introuvable : " + code)
                );

        // ✅ Vérification doublon libellé à la modification
        if (planAuditRepository.existsByLibelleAndCodeNot(request.getLibelle(), code)) {
            throw new RuntimeException(
                    "Un plan d'audit avec ce libellé existe déjà : " + request.getLibelle()
            );
        }

        UniteAdministrative unite = uniteRepository.findByCode(request.getCodeUniteAdministrative())
                .orElseThrow(() ->
                        new RuntimeException("Unité administrative introuvable : " + request.getCodeUniteAdministrative())
                );

        Processus processus = processusRepository.findByCode(request.getCodeProcessus())
                .orElseThrow(() ->
                        new RuntimeException("Processus introuvable : " + request.getCodeProcessus())
                );

        Risque risque = null;
        if (request.getCodeRisque() != null && !request.getCodeRisque().isBlank()) {
            risque = risqueRepository.findByCode(request.getCodeRisque())
                    .orElseThrow(() ->
                            new RuntimeException("Risque introuvable : " + request.getCodeRisque())
                    );
        }

        planAudit.setLibelle(request.getLibelle());
        planAudit.setDateCreation(request.getDateCreation());
        planAudit.setUniteAdministrative(unite);
        planAudit.setProcessus(processus);
        planAudit.setRisque(risque);
        planAudit.setAuditPropose(request.getAuditPropose());
        planAudit.setTypeRevue(request.getTypeRevue());
        planAudit.setObjectifAudit(request.getObjectifAudit());
        planAudit.setEffetAuditIndicatif(request.getEffetAuditIndicatif());

        PlanAudit updated = planAuditRepository.save(planAudit);

        return toResponse(updated);
    }

    // ================= DELETE =================
    @Override
    public void delete(String code) {
        PlanAudit planAudit = planAuditRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Plan d'audit introuvable : " + code)
                );
        planAuditRepository.delete(planAudit);
    }

    // ================= RESPONSE =================
    private PlanAuditResponse toResponse(PlanAudit planAudit) {
        return new PlanAuditResponse(
                planAudit.getId(),
                planAudit.getCode(),
                planAudit.getLibelle(),
                planAudit.getDateCreation(),
                planAudit.getUniteAdministrative() != null ? planAudit.getUniteAdministrative().getCode() : null,
                planAudit.getUniteAdministrative() != null ? planAudit.getUniteAdministrative().getLibelle() : null,
                planAudit.getProcessus() != null ? planAudit.getProcessus().getCode() : null,
                planAudit.getProcessus() != null ? planAudit.getProcessus().getLibelle() : null,
                planAudit.getRisque() != null ? planAudit.getRisque().getCode() : null,
                planAudit.getRisque() != null ? planAudit.getRisque().getLibelle() : null,
                planAudit.getAuditPropose(),
                planAudit.getTypeRevue(),
                planAudit.getObjectifAudit(),
                planAudit.getEffetAuditIndicatif()
        );
    }

    // ================= CODE GENERATION =================
    // Format : PA_<sigleUA><séquence sur 3 chiffres>, séquence propre à l'UA
    private String generateCode(String sigleUnite) {
        long compteur = planAuditRepository.countByUniteAdministrative_Code(sigleUnite) + 1;

        String code = "PA_" + sigleUnite + String.format("%03d", compteur);
        while (planAuditRepository.existsByCode(code)) {
            compteur++;
            code = "PA_" + sigleUnite + String.format("%03d", compteur);
        }
        return code;
    }
}

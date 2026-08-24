package com.example.SIGR.services;

import com.example.SIGR.dto.request.PlanMitigationRequest;
import com.example.SIGR.dto.response.PlanMitigationResponse;

import com.example.SIGR.entity.Action;
import com.example.SIGR.entity.PlanMitigation;
import com.example.SIGR.entity.Risque;
import com.example.SIGR.entity.StatutAction;
import com.example.SIGR.entity.StatutPlanMitigation;

import com.example.SIGR.repository.ActionRepository;
import com.example.SIGR.repository.PlanMitigationRepository;
import com.example.SIGR.repository.RisqueRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanMitigationServiceImpl implements PlanMitigationService {

    private final PlanMitigationRepository repository;
    private final RisqueRepository risqueRepository;
    private final ActionRepository actionRepository;

    public PlanMitigationServiceImpl(
            PlanMitigationRepository repository,
            RisqueRepository risqueRepository,
            ActionRepository actionRepository
    ) {
        this.repository = repository;
        this.risqueRepository = risqueRepository;
        this.actionRepository = actionRepository;
    }

    // ================= CREATE =================
    @Override
    public PlanMitigationResponse create(PlanMitigationRequest request) {

        // ✅ Vérification doublon libellé à la création
        if (repository.existsByLibelle(request.getLibelle())) {
            throw new RuntimeException(
                    "Un plan de mitigation avec ce libellé existe déjà : " + request.getLibelle()
            );
        }

        // ================= RISQUES =================
        List<Risque> risques = request.getCodesRisques().stream()
                .map(codeRisque -> risqueRepository.findByCode(codeRisque)
                        .orElseThrow(() ->
                                new RuntimeException("Risque introuvable : " + codeRisque)
                        ))
                .collect(Collectors.toList());

        // ================= GENERATION CODE =================
        // Format : PM_<sigleUA><séquence sur 3 chiffres>, séquence propre à l'UA
        // du PREMIER risque de la liste (représentatif, cf. comportement
        // historique lorsque le plan ne portait qu'un seul risque).
        String sigleUnite = risques.get(0).getProcessus().getUnite().getCode();
        long compteur = repository.countByRisque_Processus_Unite_Code(sigleUnite) + 1;

        String code = "PM_" + sigleUnite + String.format("%03d", compteur);
        while (repository.existsByCode(code)) {
            compteur++;
            code = "PM_" + sigleUnite + String.format("%03d", compteur);
        }

        // ================= CREATION =================
        PlanMitigation plan = new PlanMitigation();

        plan.setCode(code);
        plan.setLibelle(request.getLibelle());
        plan.setDescription(request.getDescription());
        plan.setDateCreation(request.getDateCreation());
        // Le statut est désormais entièrement piloté par le système : PLANIFIE
        // à la création (aucune action liée pour l'instant), puis EN_COURS/
        // TERMINE selon les actions (voir recalculerStatut), et CLOTURE via
        // l'action dédiée du CCI (voir cloturer()) — jamais saisi manuellement.
        plan.setStatut(StatutPlanMitigation.PLANIFIE);
        plan.setRisques(risques);

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

        // ✅ Vérification doublon libellé à la mise à jour (exclut le plan courant)
        if (repository.existsByLibelleAndCodeNot(request.getLibelle(), code)) {
            throw new RuntimeException(
                    "Un plan de mitigation avec ce libellé existe déjà : " + request.getLibelle()
            );
        }

        List<Risque> risques = request.getCodesRisques().stream()
                .map(codeRisque -> risqueRepository.findByCode(codeRisque)
                        .orElseThrow(() ->
                                new RuntimeException("Risque introuvable : " + codeRisque)
                        ))
                .collect(Collectors.toList());

        plan.setLibelle(request.getLibelle());
        plan.setDescription(request.getDescription());
        plan.setDateCreation(request.getDateCreation());
        // Le statut n'est pas repris ici : il est piloté par le système
        // (voir create() et recalculerStatut()).
        plan.setRisques(risques);

        return toResponse(repository.save(plan));
    }

    // ================= CLÔTURE (CCI) =================
    @Override
    public PlanMitigationResponse cloturer(String code) {

        PlanMitigation plan = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Plan introuvable : " + code)
                );

        if (plan.getStatut() == StatutPlanMitigation.CLOTURE) {
            throw new RuntimeException("Ce plan est déjà clôturé.");
        }

        plan.setStatut(StatutPlanMitigation.CLOTURE);

        return toResponse(repository.save(plan));
    }

    // ================= RECALCUL AUTOMATIQUE =================
    @Override
    public void recalculerStatut(String codePlanMitigation) {

        PlanMitigation plan = repository.findByCode(codePlanMitigation)
                .orElseThrow(() ->
                        new RuntimeException("Plan introuvable : " + codePlanMitigation)
                );

        // Une fois clôturé par le CCI, plus aucun recalcul automatique.
        if (plan.getStatut() == StatutPlanMitigation.CLOTURE) {
            return;
        }

        List<Action> actions = actionRepository.findByPlanMitigation(plan);

        if (actions.isEmpty()) {
            plan.setStatut(StatutPlanMitigation.PLANIFIE);
        } else if (actions.stream().allMatch(a -> a.getStatut() == StatutAction.TERMINEE)) {
            plan.setStatut(StatutPlanMitigation.TERMINE);
        } else {
            plan.setStatut(StatutPlanMitigation.EN_COURS);
        }

        repository.save(plan);
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

        List<Risque> risques = plan.getRisques();

        return new PlanMitigationResponse(
                plan.getId(),
                plan.getCode(),
                plan.getLibelle(),
                plan.getDescription(),
                plan.getDateCreation(),
                plan.getStatut(),
                risques != null ? risques.stream().map(Risque::getCode).collect(Collectors.toList()) : null,
                risques != null ? risques.stream().map(Risque::getLibelle).collect(Collectors.toList()) : null
        );
    }
}
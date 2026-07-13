package com.example.SIGR.services;

import com.example.SIGR.dto.request.IndicateurPerformanceRequest;
import com.example.SIGR.dto.response.IndicateurPerformanceResponse;

import com.example.SIGR.entity.Action;
import com.example.SIGR.entity.IndicateurPerformance;
import com.example.SIGR.entity.PlanMitigation;
import com.example.SIGR.entity.Processus;
import com.example.SIGR.entity.Risque;
import com.example.SIGR.entity.UniteMesure;

import com.example.SIGR.repository.ActionRepository;
import com.example.SIGR.repository.IndicateurPerformanceRepository;
import com.example.SIGR.repository.PlanMitigationRepository;
import com.example.SIGR.repository.ProcessusRepository;
import com.example.SIGR.repository.RisqueRepository;
import com.example.SIGR.repository.UniteMesureRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndicateurPerformanceServiceImpl
        implements IndicateurPerformanceService {

    private final IndicateurPerformanceRepository repository;
    private final ProcessusRepository processusRepository;
    private final UniteMesureRepository uniteMesureRepository;
    private final RisqueRepository risqueRepository;
    private final PlanMitigationRepository planMitigationRepository;
    private final ActionRepository actionRepository;

    public IndicateurPerformanceServiceImpl(
            IndicateurPerformanceRepository repository,
            ProcessusRepository processusRepository,
            UniteMesureRepository uniteMesureRepository,
            RisqueRepository risqueRepository,
            PlanMitigationRepository planMitigationRepository,
            ActionRepository actionRepository
    ) {
        this.repository = repository;
        this.processusRepository = processusRepository;
        this.uniteMesureRepository = uniteMesureRepository;
        this.risqueRepository = risqueRepository;
        this.planMitigationRepository = planMitigationRepository;
        this.actionRepository = actionRepository;
    }

    // ================= VALIDATION METIER =================

    private void validerChamps(IndicateurPerformanceRequest request) {
        // Les valeurs peuvent être des nombres ou des dates (string)
        // On ne valide que si ce sont des nombres
        String valeurCibleStr = request.getValeurCible();
        String valeurObtenueStr = request.getValeurObtenue();

        try {
            if (valeurCibleStr != null && !valeurCibleStr.isEmpty()) {
                double valeurCible = Double.parseDouble(valeurCibleStr);
                if (valeurCible > 100) {
                    throw new RuntimeException("La valeur cible ne peut pas dépasser 100% (valeur saisie : " + valeurCible + ")");
                }
                if (valeurCible < 0) {
                    throw new RuntimeException("La valeur cible ne peut pas être négative.");
                }
            }
            if (valeurObtenueStr != null && !valeurObtenueStr.isEmpty()) {
                double valeurObtenue = Double.parseDouble(valeurObtenueStr);
                if (valeurObtenue > 100) {
                    throw new RuntimeException("La valeur obtenue ne peut pas dépasser 100% (valeur saisie : " + valeurObtenue + ")");
                }
                if (valeurObtenue < 0) {
                    throw new RuntimeException("La valeur obtenue ne peut pas être négative.");
                }
            }
            // Règle : la valeur cible doit être inférieure ou égale à la valeur obtenue (pour les valeurs numériques)
            if (valeurCibleStr != null && !valeurCibleStr.isEmpty() && valeurObtenueStr != null && !valeurObtenueStr.isEmpty()) {
                double valeurCible = Double.parseDouble(valeurCibleStr);
                double valeurObtenue = Double.parseDouble(valeurObtenueStr);
                if (valeurCible <= valeurObtenue) {
                    throw new RuntimeException("La valeur cible (" + valeurCible + "%) ne peut pas être supérieure à la valeur obtenue (" + valeurObtenue + "%).");
                }
            }
        } catch (NumberFormatException e) {
            // Si ce n'est pas un nombre, on suppose que c'est une date, donc pas de validation numérique
        }

        // Validation du seuil d'alerte par rapport aux dates de début et fin
        LocalDate seuilAlerte = request.getSeuilAlerte();
        LocalDate dateDebut = request.getDateDebut();
        LocalDate dateFin = request.getDateFin();

        // Validation que la date de début est avant la date de fin
        if (dateDebut != null && dateFin != null) {
            if (dateDebut.isAfter(dateFin)) {
                throw new RuntimeException("La date de début ne peut pas être après la date de fin.");
            }
        }

        if (seuilAlerte != null && dateDebut != null && dateFin != null) {
            if (seuilAlerte.isBefore(dateDebut)) {
                throw new RuntimeException("Le seuil d'alerte ne peut pas être avant la date de début.");
            }
            if (seuilAlerte.isAfter(dateFin)) {
                throw new RuntimeException("Le seuil d'alerte ne peut pas être après la date de fin.");
            }
        }
    }

    // ================= CREATE =================
    @Override
    public IndicateurPerformanceResponse create(
            IndicateurPerformanceRequest request
    ) {
        // ✅ Validation métier
        validerChamps(request);

        if (repository.existsByLibelleIgnoreCase(request.getLibelle())) {
            throw new RuntimeException(
                    "Un indicateur avec ce libellé existe déjà : " + request.getLibelle()
            );
        }

        // ================= PROCESSUS =================
        Processus processus = processusRepository
                .findByCode(request.getCodeProcessus())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Processus introuvable : " + request.getCodeProcessus()
                        )
                );

        // ================= UNITE MESURE =================
        UniteMesure uniteMesure = null;
        if (request.getCodeUniteMesure() != null
                && !request.getCodeUniteMesure().isBlank()) {
            uniteMesure = uniteMesureRepository
                    .findByCode(request.getCodeUniteMesure())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Unité de mesure introuvable : "
                                            + request.getCodeUniteMesure()
                            )
                    );
        }

        // ================= RISQUE =================
        Risque risque = null;
        if (request.getCodeRisque() != null && !request.getCodeRisque().isBlank()) {
            risque = risqueRepository
                    .findByCode(request.getCodeRisque())
                    .orElseThrow(() ->
                            new RuntimeException("Risque introuvable : " + request.getCodeRisque())
                    );
        }

        // ================= PLAN MITIGATION =================
        PlanMitigation planMitigation = null;
        if (request.getCodePlanMitigation() != null && !request.getCodePlanMitigation().isBlank()) {
            planMitigation = planMitigationRepository
                    .findByCode(request.getCodePlanMitigation())
                    .orElseThrow(() ->
                            new RuntimeException("Plan de mitigation introuvable : " + request.getCodePlanMitigation())
                    );
        }

        // ================= ACTION =================
        Action action = null;
        if (request.getCodeAction() != null && !request.getCodeAction().isBlank()) {
            action = actionRepository
                    .findByCode(request.getCodeAction())
                    .orElseThrow(() ->
                            new RuntimeException("Action introuvable : " + request.getCodeAction())
                    );
        }

        // ================= GENERATION CODE =================
        // Format : I_A_R_<sigleUA><séquence sur 3 chiffres>, séquence propre à l'UA
        String sigleUnite = processus.getUnite().getCode();
        long total = repository.countByProcessus_Unite_Code(sigleUnite) + 1;
        String code = "I_A_R_" + sigleUnite + String.format("%03d", total);

        while (repository.existsByCode(code)) {
            total++;
            code = "I_A_R_" + sigleUnite + String.format("%03d", total);
        }

        // ================= CREATION =================
        IndicateurPerformance kpi = new IndicateurPerformance();

        kpi.setCode(code);
        kpi.setLibelle(request.getLibelle());
        kpi.setFrequence(request.getFrequence());
        
        // Conversion des valeurs (String vers Double si numérique)
        kpi.setValeurCible(request.getValeurCible());
        kpi.setValeurObtenue(request.getValeurObtenue());
        kpi.setSeuilAlerte(request.getSeuilAlerte());
        
        kpi.setDateDebut(request.getDateDebut());
        kpi.setDateFin(request.getDateFin());
        kpi.setProcessus(processus);
        kpi.setUniteMesure(uniteMesure);
        kpi.setRisque(risque);
        kpi.setPlanMitigation(planMitigation);
        kpi.setAction(action);

        return toResponse(repository.save(kpi));
    }

    // ================= GET BY CODE =================
    @Override
    public IndicateurPerformanceResponse getByCode(String code) {

        IndicateurPerformance kpi = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("KPI introuvable : " + code)
                );

        return toResponse(kpi);
    }

    // ================= GET ALL =================
    @Override
    public List<IndicateurPerformanceResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @Override
    public IndicateurPerformanceResponse update(
            String code,
            IndicateurPerformanceRequest request
    ) {
        // ✅ Validation métier
        validerChamps(request);

        // ================= KPI =================
        IndicateurPerformance kpi = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("KPI introuvable : " + code)
                );

        if (repository.existsByLibelleIgnoreCaseAndCodeNot(request.getLibelle(), code)) {
            throw new RuntimeException(
                    "Un indicateur avec ce libellé existe déjà : " + request.getLibelle()
            );
        }

        // ================= PROCESSUS =================
        Processus processus = processusRepository
                .findByCode(request.getCodeProcessus())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Processus introuvable : " + request.getCodeProcessus()
                        )
                );

        // ================= UNITE MESURE =================
        if (request.getCodeUniteMesure() != null
                && !request.getCodeUniteMesure().isBlank()) {
            UniteMesure uniteMesure = uniteMesureRepository
                    .findByCode(request.getCodeUniteMesure())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Unité de mesure introuvable : "
                                            + request.getCodeUniteMesure()
                            )
                    );
            kpi.setUniteMesure(uniteMesure);
        }

        // ================= RISQUE =================
        Risque risque = null;
        if (request.getCodeRisque() != null && !request.getCodeRisque().isBlank()) {
            risque = risqueRepository
                    .findByCode(request.getCodeRisque())
                    .orElseThrow(() ->
                            new RuntimeException("Risque introuvable : " + request.getCodeRisque())
                    );
        }
        kpi.setRisque(risque);

        // ================= PLAN MITIGATION =================
        PlanMitigation planMitigation = null;
        if (request.getCodePlanMitigation() != null && !request.getCodePlanMitigation().isBlank()) {
            planMitigation = planMitigationRepository
                    .findByCode(request.getCodePlanMitigation())
                    .orElseThrow(() ->
                            new RuntimeException("Plan de mitigation introuvable : " + request.getCodePlanMitigation())
                    );
        }
        kpi.setPlanMitigation(planMitigation);

        // ================= ACTION =================
        Action action = null;
        if (request.getCodeAction() != null && !request.getCodeAction().isBlank()) {
            action = actionRepository
                    .findByCode(request.getCodeAction())
                    .orElseThrow(() ->
                            new RuntimeException("Action introuvable : " + request.getCodeAction())
                    );
        }
        kpi.setAction(action);

        // ================= UPDATE =================
        kpi.setLibelle(request.getLibelle());
        kpi.setFrequence(request.getFrequence());
        
        // Conversion des valeurs (String vers Double si numérique)
        kpi.setValeurCible(request.getValeurCible());
        kpi.setValeurObtenue(request.getValeurObtenue());
        kpi.setSeuilAlerte(request.getSeuilAlerte());
        
        kpi.setDateDebut(request.getDateDebut());
        kpi.setDateFin(request.getDateFin());
        kpi.setProcessus(processus);

        return toResponse(repository.save(kpi));
    }

    // ================= DELETE =================
    @Override
    public void delete(String code) {

        IndicateurPerformance kpi = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("KPI introuvable : " + code)
                );

        repository.delete(kpi);
    }

    // ================= RESPONSE =================
    private IndicateurPerformanceResponse toResponse(IndicateurPerformance kpi) {
        
        String valeurCibleStr = kpi.getValeurCible() != null ? String.valueOf(kpi.getValeurCible()) : null;
        String valeurObtenueStr = kpi.getValeurObtenue() != null ? String.valueOf(kpi.getValeurObtenue()) : null;

        return new IndicateurPerformanceResponse(
                kpi.getId(),
                kpi.getCode(),
                kpi.getLibelle(),
                kpi.getUniteMesure() != null ? kpi.getUniteMesure().getCode() : null,
                kpi.getUniteMesure() != null ? kpi.getUniteMesure().getLibelle() : null,
                kpi.getUniteMesure() != null ? kpi.getUniteMesure().getTypeUnite().name() : null,
                kpi.getFrequence(),
                valeurCibleStr,
                valeurObtenueStr,
                kpi.getSeuilAlerte(),
                kpi.getDateDebut(),
                kpi.getDateFin(),
                kpi.getProcessus() != null ? kpi.getProcessus().getCode()    : null,
                kpi.getProcessus() != null ? kpi.getProcessus().getLibelle() : null,
                kpi.getRisque() != null ? kpi.getRisque().getCode() : null,
                kpi.getRisque() != null ? kpi.getRisque().getLibelle() : null,
                kpi.getPlanMitigation() != null ? kpi.getPlanMitigation().getCode() : null,
                kpi.getPlanMitigation() != null ? kpi.getPlanMitigation().getLibelle() : null,
                kpi.getAction() != null ? kpi.getAction().getCode() : null,
                kpi.getAction() != null ? kpi.getAction().getLibelle() : null,
                kpi.getEcartCible(),
                kpi.getStatut()
        );
    }
}
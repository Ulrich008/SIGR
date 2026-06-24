package com.example.SIGR.services;

import com.example.SIGR.dto.response.AlerteResponse;
import com.example.SIGR.entity.Action;
import com.example.SIGR.entity.IndicateurPerformance;
import com.example.SIGR.entity.PlanMitigation;
import com.example.SIGR.entity.Risque;
import com.example.SIGR.entity.StatutAction;
import com.example.SIGR.entity.StatutRisque;
import com.example.SIGR.repository.ActionRepository;
import com.example.SIGR.repository.IndicateurPerformanceRepository;
import com.example.SIGR.repository.PlanMitigationRepository;
import com.example.SIGR.repository.RisqueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlerteServiceImpl implements AlerteService {

    private final RisqueRepository risqueRepository;
    private final PlanMitigationRepository planMitigationRepository;
    private final ActionRepository actionRepository;
    private final IndicateurPerformanceRepository indicateurPerformanceRepository;

    public AlerteServiceImpl(
            RisqueRepository risqueRepository,
            PlanMitigationRepository planMitigationRepository,
            ActionRepository actionRepository,
            IndicateurPerformanceRepository indicateurPerformanceRepository
    ) {
        this.risqueRepository = risqueRepository;
        this.planMitigationRepository = planMitigationRepository;
        this.actionRepository = actionRepository;
        this.indicateurPerformanceRepository = indicateurPerformanceRepository;
    }

    @Override
    public List<AlerteResponse> detecterRisquesNonGeres() {
        List<AlerteResponse> alertes = new ArrayList<>();
        
        // Récupérer tous les risques actifs ou en cours
        List<Risque> risquesActifs = risqueRepository.findAll().stream()
                .filter(r -> r.getStatut() == StatutRisque.ACTIF || r.getStatut() == StatutRisque.EN_COURS)
                .collect(Collectors.toList());
        
        for (Risque risque : risquesActifs) {
            // Vérifier si le risque a des plans de mitigation
            List<PlanMitigation> plans = planMitigationRepository.findByRisque(risque);
            
            if (plans.isEmpty()) {
                // Risque sans plan de mitigation
                alertes.add(new AlerteResponse(
                    "RISQUE_NON_GERE",
                    "Risque sans plan de mitigation",
                    "Le risque '" + risque.getLibelle() + "' n'a aucun plan de mitigation défini.",
                    risque.getCode(),
                    risque.getLibelle(),
                    "CRITIQUE",
                    risque.getProcessus() != null ? risque.getProcessus().getCode() : null,
                    risque.getProcessus() != null ? risque.getProcessus().getLibelle() : null
                ));
            } else {
                // Vérifier si les plans ont des actions en cours
                boolean hasActionsEnCours = false;
                for (PlanMitigation plan : plans) {
                    List<Action> actions = actionRepository.findByPlanMitigation(plan);
                    for (Action action : actions) {
                        if (action.getStatut() == StatutAction.EN_COURS) {
                            hasActionsEnCours = true;
                            break;
                        }
                    }
                    if (hasActionsEnCours) break;
                }
                
                if (!hasActionsEnCours) {
                    alertes.add(new AlerteResponse(
                        "RISQUE_NON_GERE",
                        "Risque sans actions en cours",
                        "Le risque '" + risque.getLibelle() + "' a des plans de mitigation mais aucune action en cours.",
                        risque.getCode(),
                        risque.getLibelle(),
                        "HAUTE",
                        risque.getProcessus() != null ? risque.getProcessus().getCode() : null,
                        risque.getProcessus() != null ? risque.getProcessus().getLibelle() : null
                    ));
                }
            }
        }
        
        return alertes;
    }

    @Override
    public List<AlerteResponse> detecterIndicateursProchesSeuil() {
        List<AlerteResponse> alertes = new ArrayList<>();
        LocalDate aujourdHui = LocalDate.now();
        
        // Récupérer tous les indicateurs avec un seuil d'alerte
        List<IndicateurPerformance> indicateurs = indicateurPerformanceRepository.findAll().stream()
                .filter(i -> i.getSeuilAlerte() != null)
                .collect(Collectors.toList());
        
        for (IndicateurPerformance indicateur : indicateurs) {
            LocalDate seuilAlerte = indicateur.getSeuilAlerte();
            LocalDate dateFinAction = null;
            
            // Récupérer la date de fin de l'action associée si elle existe
            if (indicateur.getAction() != null) {
                dateFinAction = indicateur.getAction().getDateFin();
            } else if (indicateur.getPlanMitigation() != null) {
                // Si pas d'action directe, vérifier les actions du plan
                List<Action> actions = actionRepository.findByPlanMitigation(indicateur.getPlanMitigation());
                if (!actions.isEmpty()) {
                    dateFinAction = actions.stream()
                            .map(Action::getDateFin)
                            .filter(date -> date != null)
                            .max(LocalDate::compareTo)
                            .orElse(null);
                }
            }
            
            if (dateFinAction != null) {
                long joursRestants = ChronoUnit.DAYS.between(aujourdHui, dateFinAction);
                
                // Vérifier si le seuil d'alerte est dépassé
                if (aujourdHui.isAfter(seuilAlerte)) {
                    alertes.add(new AlerteResponse(
                        "INDICATEUR_PROCHE_SEUIL",
                        "Seuil d'alerte dépassé",
                        "L'indicateur '" + indicateur.getLibelle() + "' a dépassé son seuil d'alerte. " +
                        "Date de fin de l'action: " + dateFinAction + " (" + joursRestants + " jours restants).",
                        indicateur.getCode(),
                        indicateur.getLibelle(),
                        "HAUTE",
                        indicateur.getProcessus() != null ? indicateur.getProcessus().getCode() : null,
                        indicateur.getProcessus() != null ? indicateur.getProcessus().getLibelle() : null
                    ));
                } else if (joursRestants <= 7) {
                    // Alert si moins de 7 jours restants
                    alertes.add(new AlerteResponse(
                        "INDICATEUR_PROCHE_SEUIL",
                        "Échéance proche de l'action",
                        "L'indicateur '" + indicateur.getLibelle() + "' approche de la date de fin de l'action. " +
                        "Date de fin: " + dateFinAction + " (" + joursRestants + " jours restants).",
                        indicateur.getCode(),
                        indicateur.getLibelle(),
                        "MOYENNE",
                        indicateur.getProcessus() != null ? indicateur.getProcessus().getCode() : null,
                        indicateur.getProcessus() != null ? indicateur.getProcessus().getLibelle() : null
                    ));
                }
            }
        }
        
        return alertes;
    }

    @Override
    public List<AlerteResponse> detecterToutesAlertes() {
        List<AlerteResponse> toutesAlertes = new ArrayList<>();
        toutesAlertes.addAll(detecterRisquesNonGeres());
        toutesAlertes.addAll(detecterIndicateursProchesSeuil());
        
        // Générer des IDs uniques pour les alertes
        for (int i = 0; i < toutesAlertes.size(); i++) {
            toutesAlertes.get(i).setId(UUID.randomUUID().toString());
        }
        
        return toutesAlertes;
    }
}

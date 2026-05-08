package com.example.SIGR.services;

import com.example.SIGR.dto.request.ActionRequest;
import com.example.SIGR.dto.response.ActionResponse;
import com.example.SIGR.entity.Action;
import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.PlanMitigation;
import com.example.SIGR.repository.ActionRepository;
import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.PlanMitigationRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActionServiceImpl implements ActionService {

    private final ActionRepository actionRepository;
    private final PlanMitigationRepository planMitigationRepository;
    private final AgentRepository agentRepository;

    public ActionServiceImpl(
            ActionRepository actionRepository,
            PlanMitigationRepository planMitigationRepository,
            AgentRepository agentRepository
    ) {
        this.actionRepository = actionRepository;
        this.planMitigationRepository = planMitigationRepository;
        this.agentRepository = agentRepository;
    }

    // ================= CREATE =================
    @Override
    public ActionResponse create(ActionRequest request) {

        if (actionRepository.existsById(request.getId())) {
            throw new RuntimeException(
                    "Le code de l'action existe déjà : " + request.getId()
            );
        }

        if (actionRepository.existsByLibelle(request.getLibelle())) {
            throw new RuntimeException(
                    "Une action avec ce libellé existe déjà : "
                            + request.getLibelle()
            );
        }

        // ================= VERIFICATION DATES =================
        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new RuntimeException(
                    "La date de fin ne peut pas être inférieure à la date de début"
            );
        }

        PlanMitigation plan = planMitigationRepository.findById(request.getIdPlan())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Plan de mitigation introuvable : "
                                        + request.getIdPlan()
                        )
                );

        Agent responsable = agentRepository.findById(request.getMatriculeResponsable())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent responsable introuvable : "
                                        + request.getMatriculeResponsable()
                        )
                );

        Action action = new Action();

        action.setId(request.getId());
        action.setLibelle(request.getLibelle());
        action.setDateDebut(request.getDateDebut());
        action.setDateFin(request.getDateFin());
        action.setStatut(request.getStatut());

        action.setPlanMitigation(plan);
        action.setResponsable(responsable);

        Action saved = actionRepository.save(action);

        return toResponse(saved);
    }

    // ================= GET BY ID =================
    @Override
    public ActionResponse getById(String id) {

        Action action = actionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Action introuvable : " + id)
                );

        return toResponse(action);
    }

    // ================= GET ALL =================
    @Override
    public List<ActionResponse> getAll() {

        return actionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @Override
    public ActionResponse update(String id, ActionRequest request) {

        Action action = actionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Action introuvable : " + id)
                );

        if (actionRepository.existsByLibelle(request.getLibelle())
                && !action.getLibelle().equals(request.getLibelle())) {

            throw new RuntimeException(
                    "Une action avec ce libellé existe déjà : "
                            + request.getLibelle()
            );
        }

        // ================= VERIFICATION DATES =================
        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new RuntimeException(
                    "La date de fin ne peut pas être inférieure à la date de début"
            );
        }

        PlanMitigation plan = planMitigationRepository.findById(request.getIdPlan())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Plan de mitigation introuvable : "
                                        + request.getIdPlan()
                        )
                );

        Agent responsable = agentRepository.findById(request.getMatriculeResponsable())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent responsable introuvable : "
                                        + request.getMatriculeResponsable()
                        )
                );

        action.setLibelle(request.getLibelle());
        action.setDateDebut(request.getDateDebut());
        action.setDateFin(request.getDateFin());
        action.setStatut(request.getStatut());

        action.setPlanMitigation(plan);
        action.setResponsable(responsable);

        Action updated = actionRepository.save(action);

        return toResponse(updated);
    }

    // ================= DELETE =================
    @Override
    public void delete(String id) {

        if (!actionRepository.existsById(id)) {
            throw new RuntimeException("Action introuvable : " + id);
        }

        actionRepository.deleteById(id);
    }

    // ================= MAPPER =================
    private ActionResponse toResponse(Action action) {

        return new ActionResponse(
                action.getId(),
                action.getLibelle(),
                action.getDateDebut(),
                action.getDateFin(),
                action.getStatut(),
                action.getPlanMitigation().getId(),
                action.getResponsable().getMatricule(),
                action.getResponsable().getNom()
        );
    }
}
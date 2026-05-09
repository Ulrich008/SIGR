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

        if (actionRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Code action déjà existant : " + request.getCode());
        }

        if (actionRepository.existsByLibelle(request.getLibelle())) {
            throw new RuntimeException("Libellé déjà utilisé : " + request.getLibelle());
        }

        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new RuntimeException("La date de fin ne peut pas être inférieure à la date de début");
        }

        PlanMitigation plan = planMitigationRepository.findById(request.getIdPlan())
                .orElseThrow(() ->
                        new RuntimeException("Plan introuvable : " + request.getIdPlan())
                );

        Agent responsable = agentRepository.findById(request.getMatriculeResponsable())
                .orElseThrow(() ->
                        new RuntimeException("Agent introuvable : " + request.getMatriculeResponsable())
                );

        Action action = new Action()
                .setCode(request.getCode())
                .setLibelle(request.getLibelle())
                .setDateDebut(request.getDateDebut())
                .setDateFin(request.getDateFin())
                .setStatut(request.getStatut()) // ENUM maintenant
                .setPlanMitigation(plan)
                .setResponsable(responsable);

        return toResponse(actionRepository.save(action));
    }

    // ================= GET BY ID =================

    @Override
    public ActionResponse getById(String id) {

        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Action introuvable : " + id));

        return toResponse(action);
    }

    // ================= GET BY CODE =================

    @Override
    public ActionResponse getByCode(String code) {

        return actionRepository.findAll()
                .stream()
                .filter(a -> a.getCode().equals(code))
                .findFirst()
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Action introuvable : " + code));
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
                .orElseThrow(() -> new RuntimeException("Action introuvable : " + id));

        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new RuntimeException("La date de fin ne peut pas être inférieure à la date de début");
        }

        if (actionRepository.existsByCode(request.getCode())
                && !action.getCode().equals(request.getCode())) {
            throw new RuntimeException("Code déjà utilisé : " + request.getCode());
        }

        if (actionRepository.existsByLibelle(request.getLibelle())
                && !action.getLibelle().equals(request.getLibelle())) {
            throw new RuntimeException("Libellé déjà utilisé : " + request.getLibelle());
        }

        PlanMitigation plan = planMitigationRepository.findById(request.getIdPlan())
                .orElseThrow(() ->
                        new RuntimeException("Plan introuvable : " + request.getIdPlan())
                );

        Agent responsable = agentRepository.findById(request.getMatriculeResponsable())
                .orElseThrow(() ->
                        new RuntimeException("Agent introuvable : " + request.getMatriculeResponsable())
                );

        action.setCode(request.getCode())
                .setLibelle(request.getLibelle())
                .setDateDebut(request.getDateDebut())
                .setDateFin(request.getDateFin())
                .setStatut(request.getStatut()) // ENUM
                .setPlanMitigation(plan)
                .setResponsable(responsable);

        return toResponse(actionRepository.save(action));
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
                action.getCode(),
                action.getLibelle(),
                action.getDateDebut(),
                action.getDateFin(),
                action.getStatut(), // ENUM directement
                action.getPlanMitigation().getId(),
                action.getResponsable().getMatricule(),
                action.getResponsable().getNom()
        );
    }
}
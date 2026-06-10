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

        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new RuntimeException("Date de fin invalide");
        }

        PlanMitigation plan = planMitigationRepository.findByCode(request.getCodePlan())
                .orElseThrow(() -> new RuntimeException("Plan introuvable : " + request.getCodePlan()));

        Agent responsable = agentRepository.findByMatricule(request.getMatriculeResponsable())
                .orElseThrow(() -> new RuntimeException("Agent introuvable : " + request.getMatriculeResponsable()));

        // ================= GENERATION CODE AUTO =================
        long count = actionRepository.count() + 1;

        String code = String.format("ACT-%03d", count);

        while (actionRepository.existsByCode(code)) {
            count++;
            code = String.format("ACT-%03d", count);
        }

        Action action = new Action()
                .setCode(code)
                .setLibelles(request.getLibelles())
                .setCodeRisque(request.getCodeRisque())
                .setBonnePratique(request.getBonnePratique())
                .setDateDebut(request.getDateDebut())
                .setDateFin(request.getDateFin())
                .setStatut(request.getStatut())
                .setPlanMitigation(plan)
                .setResponsable(responsable);

        return toResponse(actionRepository.save(action));
    }

    // ================= GET BY CODE =================
    @Override
    public ActionResponse getByCode(String code) {

        Action action = actionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Action introuvable : " + code));

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

    // ================= UPDATE BY CODE =================
    @Override
    public ActionResponse update(String code, ActionRequest request) {

        Action action = actionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Action introuvable : " + code));

        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new RuntimeException("Date de fin invalide");
        }

        PlanMitigation plan = planMitigationRepository.findByCode(request.getCodePlan())
                .orElseThrow(() -> new RuntimeException("Plan introuvable : " + request.getCodePlan()));

        Agent responsable = agentRepository.findByMatricule(request.getMatriculeResponsable())
                .orElseThrow(() -> new RuntimeException("Agent introuvable : " + request.getMatriculeResponsable()));

        action.setLibelles(request.getLibelles())
                .setCodeRisque(request.getCodeRisque())
                .setBonnePratique(request.getBonnePratique())
                .setDateDebut(request.getDateDebut())
                .setDateFin(request.getDateFin())
                .setStatut(request.getStatut())
                .setPlanMitigation(plan)
                .setResponsable(responsable);

        return toResponse(actionRepository.save(action));
    }

    // ================= DELETE BY CODE =================
    @Override
    public void delete(String code) {

        Action action = actionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Action introuvable : " + code));

        actionRepository.delete(action);
    }

    // ================= MAPPER =================
    private ActionResponse toResponse(Action action) {

        return new ActionResponse(
                action.getCode(),
                action.getLibelles(),
                action.getDateDebut(),
                action.getDateFin(),
                action.getStatut(),
                action.getPlanMitigation().getCode(),
                action.getCodeRisque(),
                action.getBonnePratique(),
                action.getResponsable().getMatricule(),
                action.getResponsable().getNom()
        );
    }
}
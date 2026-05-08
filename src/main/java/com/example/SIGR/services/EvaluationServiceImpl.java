package com.example.SIGR.services;

import com.example.SIGR.dto.request.EvaluationRequest;
import com.example.SIGR.dto.response.EvaluationResponse;
import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.Evaluation;
import com.example.SIGR.entity.Risque;
import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.EvaluationRepository;
import com.example.SIGR.repository.RisqueRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final RisqueRepository risqueRepository;
    private final AgentRepository agentRepository;

    public EvaluationServiceImpl(
            EvaluationRepository evaluationRepository,
            RisqueRepository risqueRepository,
            AgentRepository agentRepository
    ) {
        this.evaluationRepository = evaluationRepository;
        this.risqueRepository = risqueRepository;
        this.agentRepository = agentRepository;
    }

    @Override
    public EvaluationResponse create(EvaluationRequest request) {

        if (evaluationRepository.existsById(request.getId())) {
            throw new RuntimeException("Evaluation existe déjà : " + request.getId());
        }

        Risque risque = risqueRepository.findById(request.getIdRisque())
                .orElseThrow(() -> new RuntimeException("Risque introuvable"));

        Agent agent = null;

        if (request.getIdAgent() != null) {
            agent = agentRepository.findById(request.getIdAgent())
                    .orElseThrow(() -> new RuntimeException("Agent introuvable"));
        }

        Evaluation evaluation = new Evaluation();

        evaluation.setId(request.getId());
        evaluation.setImpact(request.getImpact());
        evaluation.setProbabilite(request.getProbabilite());
        evaluation.setDateEvaluation(request.getDateEvaluation());
        evaluation.setBonnesPratiques(request.getBonnesPratiques());
        evaluation.setNiveauControle(request.getNiveauControle());
        evaluation.setRisque(risque);
        evaluation.setEvaluePar(agent);

        Evaluation saved = evaluationRepository.save(evaluation);

        return toResponse(saved);
    }

    @Override
    public EvaluationResponse getById(String id) {

        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation introuvable"));

        return toResponse(evaluation);
    }

    @Override
    public List<EvaluationResponse> getAll() {

        return evaluationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationResponse update(String id, EvaluationRequest request) {

        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation introuvable"));

        Risque risque = risqueRepository.findById(request.getIdRisque())
                .orElseThrow(() -> new RuntimeException("Risque introuvable"));

        Agent agent = null;

        if (request.getIdAgent() != null) {
            agent = agentRepository.findById(request.getIdAgent())
                    .orElseThrow(() -> new RuntimeException("Agent introuvable"));
        }

        evaluation.setImpact(request.getImpact());
        evaluation.setProbabilite(request.getProbabilite());
        evaluation.setDateEvaluation(request.getDateEvaluation());
        evaluation.setBonnesPratiques(request.getBonnesPratiques());
        evaluation.setNiveauControle(request.getNiveauControle());
        evaluation.setRisque(risque);
        evaluation.setEvaluePar(agent);

        return toResponse(evaluationRepository.save(evaluation));
    }

    @Override
    public void delete(String id) {

        if (!evaluationRepository.existsById(id)) {
            throw new RuntimeException("Evaluation introuvable");
        }

        evaluationRepository.deleteById(id);
    }

    private EvaluationResponse toResponse(Evaluation e) {

        return new EvaluationResponse(
                e.getId(),
                e.getImpact(),
                e.getProbabilite(),
                e.getDateEvaluation(),
                e.getBonnesPratiques(),
                e.getNiveauControle(),
                e.getScoreInitial(),
                e.getRisque().getId(),
                e.getRisque().getLibelle(),
                e.getEvaluePar() != null ? e.getEvaluePar().getId() : null,
                e.getEvaluePar() != null ? e.getEvaluePar().getNom() : null
        );
    }
}
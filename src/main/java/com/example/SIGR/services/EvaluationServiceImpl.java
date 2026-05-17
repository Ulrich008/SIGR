
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

    /**
     * ================= CREATION =================
     */
    @Override
    public EvaluationResponse create(
            EvaluationRequest request
    ) {

        // ================= RISQUE =================

        Risque risque =
                risqueRepository
                        .findByCode(request.getCodeRisque())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Risque introuvable : "
                                                + request.getCodeRisque()
                                )
                        );

        // ================= AGENT =================

        Agent agent = null;

        if (request.getMatriculeAgent() != null
                && !request.getMatriculeAgent().isBlank()) {

            agent = agentRepository
                    .findByMatricule(
                            request.getMatriculeAgent()
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Agent introuvable : "
                                            + request.getMatriculeAgent()
                            )
                    );
        }

        // ================= GENERATION CODE =================

        long total =
                evaluationRepository.count() + 1;

        String code =
                String.format(
                        "EVAL-%03d",
                        total
                );

        while (evaluationRepository.existsByCode(code)) {

            total++;

            code = String.format(
                    "EVAL-%03d",
                    total
            );
        }

        // ================= CREATION =================

        Evaluation evaluation = new Evaluation();

        evaluation.setCode(code);

        evaluation.setImpact(
                request.getImpact()
        );

        evaluation.setProbabilite(
                request.getProbabilite()
        );

        evaluation.setDateEvaluation(
                request.getDateEvaluation()
        );

        evaluation.setBonnesPratiques(
                request.getBonnesPratiques()
        );

        evaluation.setNiveauControle(
                request.getNiveauControle()
        );

        evaluation.setRisque(risque);

        evaluation.setEvaluePar(agent);

        Evaluation saved =
                evaluationRepository.save(evaluation);

        return toResponse(saved);
    }

    /**
     * ================= GET BY CODE =================
     */
    @Override
    public EvaluationResponse getByCode(
            String code
    ) {

        Evaluation evaluation =
                evaluationRepository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Evaluation introuvable"
                                )
                        );

        return toResponse(evaluation);
    }

    /**
     * ================= GET ALL =================
     */
    @Override
    public List<EvaluationResponse> getAll() {

        return evaluationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * ================= UPDATE =================
     */
    @Override
    public EvaluationResponse update(
            String code,
            EvaluationRequest request
    ) {

        // ================= EVALUATION =================

        Evaluation evaluation =
                evaluationRepository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Evaluation introuvable : " + code
                                )
                        );

        // ================= RISQUE =================

        Risque risque =
                risqueRepository
                        .findByCode(request.getCodeRisque())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Risque introuvable : "
                                                + request.getCodeRisque()
                                )
                        );

        // ================= AGENT =================

        Agent agent = null;

        if (request.getMatriculeAgent() != null
                && !request.getMatriculeAgent().isBlank()) {

            agent = agentRepository
                    .findByMatricule(
                            request.getMatriculeAgent()
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Agent introuvable : "
                                            + request.getMatriculeAgent()
                            )
                    );
        }

        // ================= UPDATE =================

        evaluation.setImpact(
                request.getImpact()
        );

        evaluation.setProbabilite(
                request.getProbabilite()
        );

        evaluation.setDateEvaluation(
                request.getDateEvaluation()
        );

        evaluation.setBonnesPratiques(
                request.getBonnesPratiques()
        );

        evaluation.setNiveauControle(
                request.getNiveauControle()
        );

        evaluation.setRisque(risque);

        evaluation.setEvaluePar(agent);

        Evaluation updated =
                evaluationRepository.save(evaluation);

        return toResponse(updated);
    }

    /**
     * ================= DELETE =================
     */
    @Override
    public void delete(
            String code
    ) {

        Evaluation evaluation =
                evaluationRepository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Evaluation introuvable : " + code
                                )
                        );

        evaluationRepository.delete(evaluation);
    }

    /**
     * ================= RESPONSE =================
     */
    private EvaluationResponse toResponse(
            Evaluation e
    ) {

        return new EvaluationResponse(
                e.getId(),
                e.getCode(),
                e.getImpact(),
                e.getProbabilite(),
                e.getDateEvaluation(),
                e.getBonnesPratiques(),
                e.getNiveauControle(),
                e.getScoreInitial(),

                e.getRisque() != null
                        ? e.getRisque().getCode()
                        : null,

                e.getRisque() != null
                        ? e.getRisque().getLibelle()
                        : null,

                e.getEvaluePar() != null
                        ? e.getEvaluePar().getMatricule()
                        : null,

                e.getEvaluePar() != null
                        ? e.getEvaluePar().getNom()
                        : null
        );
    }
}

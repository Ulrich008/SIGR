package com.example.SIGR.services;

import com.example.SIGR.dto.request.RisqueResiduelRequest;
import com.example.SIGR.dto.response.RisqueResiduelResponse;
import com.example.SIGR.entity.Evaluation;
import com.example.SIGR.entity.Risque;
import com.example.SIGR.entity.RisqueResiduel;
import com.example.SIGR.repository.EvaluationRepository;
import com.example.SIGR.repository.RisqueRepository;
import com.example.SIGR.repository.RisqueResiduelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RisqueResiduelServiceImpl implements RisqueResiduelService {

    private final RisqueResiduelRepository repository;
    private final EvaluationRepository evaluationRepository;
    private final RisqueRepository risqueRepository;

    public RisqueResiduelServiceImpl(
            RisqueResiduelRepository repository,
            EvaluationRepository evaluationRepository,
            RisqueRepository risqueRepository
    ) {
        this.repository = repository;
        this.evaluationRepository = evaluationRepository;
        this.risqueRepository = risqueRepository;
    }

    /**
     * ================= CREATE =================
     */
    @Override
    public RisqueResiduelResponse create(RisqueResiduelRequest request) {

        Evaluation evaluation = evaluationRepository
                .findByCode(request.getCodeEvaluation())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Evaluation introuvable : "
                                        + request.getCodeEvaluation()
                        )
                );

        Risque risque = risqueRepository
                .findByCode(request.getCodeRisque())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Risque introuvable : "
                                        + request.getCodeRisque()
                        )
                );

        // ===== GENERATION CODE =====

        long total = repository.count() + 1;

        String code = String.format(
                "RR-%03d",
                total
        );

        while (repository.existsByCode(code)) {

            total++;

            code = String.format(
                    "RR-%03d",
                    total
            );
        }

        RisqueResiduel rr = new RisqueResiduel();

        rr.setCode(code);

        rr.setImpactResiduel(
                request.getImpactResiduel()
        );

        rr.setProbabiliteResiduelle(
                request.getProbabiliteResiduelle()
        );

        rr.setEvaluation(evaluation);

        rr.setRisque(risque);

        return toResponse(repository.save(rr));
    }

    /**
     * ================= GET BY CODE =================
     */
    @Override
    public RisqueResiduelResponse getByCode(String code) {

        RisqueResiduel rr = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Risque résiduel introuvable : "
                                        + code
                        )
                );

        return toResponse(rr);
    }

    /**
     * ================= GET ALL =================
     */
    @Override
    public List<RisqueResiduelResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * ================= UPDATE =================
     */
    @Override
    public RisqueResiduelResponse updateByCode(
            String code,
            RisqueResiduelRequest request
    ) {

        RisqueResiduel rr = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Risque résiduel introuvable : "
                                        + code
                        )
                );

        Evaluation evaluation = evaluationRepository
                .findByCode(request.getCodeEvaluation())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Evaluation introuvable : "
                                        + request.getCodeEvaluation()
                        )
                );

        Risque risque = risqueRepository
                .findByCode(request.getCodeRisque())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Risque introuvable : "
                                        + request.getCodeRisque()
                        )
                );

        rr.setImpactResiduel(
                request.getImpactResiduel()
        );

        rr.setProbabiliteResiduelle(
                request.getProbabiliteResiduelle()
        );

        rr.setEvaluation(evaluation);

        rr.setRisque(risque);

        return toResponse(repository.save(rr));
    }

    /**
     * ================= DELETE =================
     */
    @Override
    public void deleteByCode(String code) {

        RisqueResiduel rr = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Risque résiduel introuvable : "
                                        + code
                        )
                );

        repository.delete(rr);
    }

    /**
     * ================= PAR EVALUATION =================
     */
    @Override
    public List<RisqueResiduelResponse> getByEvaluation(
            String codeEvaluation
    ) {

        return repository
                .findByEvaluation_Code(codeEvaluation)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * ================= PAR RISQUE =================
     */
    @Override
    public List<RisqueResiduelResponse> getByRisque(
            String codeRisque
    ) {

        return repository
                .findByRisque_Code(codeRisque)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * ================= RISQUES ELEVES =================
     */
    @Override
    public List<RisqueResiduelResponse> getRisquesEleves() {

        return repository.findAll()
                .stream()
                .filter(rr ->
                        rr.getScoreResiduel() != null
                                && rr.getScoreResiduel() > 15
                )
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * ================= RESPONSE =================
     */
    private RisqueResiduelResponse toResponse(
            RisqueResiduel rr
    ) {

        return new RisqueResiduelResponse(
                rr.getId(),
                rr.getCode(),
                rr.getImpactResiduel(),
                rr.getProbabiliteResiduelle(),
                rr.getScoreResiduel(),
                rr.getNiveauRisque(),

                rr.getEvaluation() != null
                        ? rr.getEvaluation().getCode()
                        : null,

                rr.getRisque() != null
                        ? rr.getRisque().getCode()
                        : null,

                rr.getRisque() != null
                        ? rr.getRisque().getLibelle()
                        : null
        );
    }
}
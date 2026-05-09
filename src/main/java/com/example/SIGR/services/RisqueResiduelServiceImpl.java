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

    // ================= CREATE =================
    @Override
    public RisqueResiduelResponse create(RisqueResiduelRequest request) {

        Evaluation evaluation = evaluationRepository.findById(request.getIdEvaluation())
                .orElseThrow(() -> new RuntimeException("Évaluation introuvable"));

        Risque risque = risqueRepository.findById(request.getIdRisque())
                .orElseThrow(() -> new RuntimeException("Risque introuvable"));

        RisqueResiduel rr = new RisqueResiduel()
                .setCode(request.getCode())
                .setImpactResiduel(request.getImpactResiduel())
                .setProbabiliteResiduelle(request.getProbabiliteResiduelle())
                .setEvaluation(evaluation)
                .setRisque(risque);

        return toResponse(repository.save(rr));
    }

    // ================= GET BY ID =================
    @Override
    public RisqueResiduelResponse getById(String id) {

        RisqueResiduel rr = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Risque résiduel introuvable"));

        return toResponse(rr);
    }

    // ================= GET ALL =================
    @Override
    public List<RisqueResiduelResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @Override
    public RisqueResiduelResponse updateBycode(String code, RisqueResiduelRequest request) {

        RisqueResiduel rr = repository.findById(code)
                .orElseThrow(() -> new RuntimeException("Risque résiduel introuvable"));

        Evaluation evaluation = evaluationRepository.findById(request.getIdEvaluation())
                .orElseThrow(() -> new RuntimeException("Évaluation introuvable"));

        Risque risque = risqueRepository.findById(request.getIdRisque())
                .orElseThrow(() -> new RuntimeException("Risque introuvable"));

        rr.setCode(request.getCode());
        rr.setImpactResiduel(request.getImpactResiduel());
        rr.setProbabiliteResiduelle(request.getProbabiliteResiduelle());
        rr.setEvaluation(evaluation);
        rr.setRisque(risque);

        return toResponse(repository.save(rr));
    }

    // ================= DELETE =================
    @Override
    public void deleteBycode(String code) {
        repository.deleteById(code);
    }

    // ================= MÉTIER =================

    @Override
    public RisqueResiduelResponse getByCode(String code) {

        RisqueResiduel rr = repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Risque résiduel introuvable avec le code : " + code));

        return toResponse(rr);
    }

    @Override
    public List<RisqueResiduelResponse> getByEvaluation(String idEvaluation) {

        return repository.findByEvaluation_Id(idEvaluation)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RisqueResiduelResponse> getByRisque(String idRisque) {

        return repository.findByRisque_Id(idRisque)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RisqueResiduelResponse> getRisquesElevés() {

        return repository.findAll()
                .stream()
                .filter(rr -> rr.getScoreResiduel() != null && rr.getScoreResiduel() > 15)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= MAPPER =================
    private RisqueResiduelResponse toResponse(RisqueResiduel rr) {

        return new RisqueResiduelResponse(
                rr.getId(),
                rr.getCode(),
                rr.getImpactResiduel(),
                rr.getProbabiliteResiduelle(),
                rr.getScoreResiduel(),
                rr.getNiveauRisque(),
                rr.getEvaluation().getId(),
                rr.getRisque().getId(),
                rr.getRisque().getLibelle()
        );
    }
}
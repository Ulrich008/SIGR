package com.example.SIGR.services;
import com.example.SIGR.dto.request.EvaluationRequest;
import com.example.SIGR.dto.response.CartographieRisqueDetailResponse;
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

    // ================= CARTOGRAPHIE =================
    @Override
    public List<CartographieRisqueDetailResponse> getCartographieDetail() {
        return evaluationRepository.findCartographieRisquesDetail();
    }

    // ================= CREATE =================
    @Override
    public EvaluationResponse create(EvaluationRequest request) {

        Risque risque = risqueRepository.findByCode(request.getCodeRisque())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Risque introuvable : " + request.getCodeRisque()
                        )
                );

        Agent agent = null;

        if (request.getMatriculeAgent() != null
                && !request.getMatriculeAgent().isBlank()) {

            agent = agentRepository.findByMatricule(request.getMatriculeAgent())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Agent introuvable : "
                                            + request.getMatriculeAgent()
                            )
                    );
        }

        long total = evaluationRepository.count() + 1;
        String code = String.format("EVAL-%03d", total);

        while (evaluationRepository.existsByCode(code)) {
            total++;
            code = String.format("EVAL-%03d", total);
        }

        Evaluation evaluation = new Evaluation();

        evaluation.setCode(code);

        mapRequestToEntity(
                evaluation,
                request,
                risque,
                agent
        );

        // ================= PRIORITE =================
        evaluation.setRangPriorite(
                calculerRangPriorite(
                        evaluation.getScoreResiduel()
                )
        );

        Evaluation saved = evaluationRepository.save(evaluation);

        return toResponse(saved);
    }

    // ================= GET BY CODE =================
    @Override
    public EvaluationResponse getByCode(String code) {

        Evaluation evaluation = evaluationRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Evaluation introuvable : " + code
                        )
                );

        return toResponse(evaluation);
    }

    // ================= GET ALL =================
    @Override
    public List<EvaluationResponse> getAll() {

        return evaluationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @Override
    public EvaluationResponse update(
            String code,
            EvaluationRequest request
    ) {

        Evaluation evaluation = evaluationRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Evaluation introuvable : " + code
                        )
                );

        Risque risque = risqueRepository.findByCode(request.getCodeRisque())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Risque introuvable : "
                                        + request.getCodeRisque()
                        )
                );

        Agent agent = null;

        if (request.getMatriculeAgent() != null
                && !request.getMatriculeAgent().isBlank()) {

            agent = agentRepository.findByMatricule(request.getMatriculeAgent())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Agent introuvable : "
                                            + request.getMatriculeAgent()
                            )
                    );
        }

        mapRequestToEntity(
                evaluation,
                request,
                risque,
                agent
        );

        // ================= RECALCUL PRIORITE =================
        evaluation.setRangPriorite(
                calculerRangPriorite(
                        evaluation.getScoreResiduel()
                )
        );

        Evaluation updated = evaluationRepository.save(evaluation);

        return toResponse(updated);
    }

    // ================= DELETE =================
    @Override
    public void delete(String code) {

        Evaluation evaluation = evaluationRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Evaluation introuvable : " + code
                        )
                );

        evaluationRepository.delete(evaluation);
    }

    // ================= MAPPING =================
    private void mapRequestToEntity(
            Evaluation evaluation,
            EvaluationRequest request,
            Risque risque,
            Agent agent
    ) {

        // ================= RISQUE INHERENT =================
        evaluation.setImpactInherent(
                request.getImpactInherent()
        );

        evaluation.setProbabiliteInherente(
                request.getProbabiliteInherente()
        );

        // ================= MAITRISE =================
        evaluation.setProtection(
                request.getProtection()
        );

        evaluation.setPrevention(
                request.getPrevention()
        );

        // ================= CONTROLES =================
        evaluation.setControleExistants(
                request.getControleExistants()
        );

        evaluation.setControleInexistants(
                request.getControleInexistants()
        );

        evaluation.setDejaSurvenu(
                request.getDejaSurvenu()
        );

        // ================= DATES =================
        if (request.getDateDebut() != null
                && request.getDateFin() != null) {

            if (!request.getDateFin()
                    .isAfter(request.getDateDebut())) {

                throw new IllegalArgumentException(
                        "La date de fin doit être supérieure à la date de début"
                );
            }
        }

        evaluation.setDateDebut(
                request.getDateDebut()
        );

        evaluation.setDateFin(
                request.getDateFin()
        );

        // ================= TEXTE =================
        evaluation.setBonnesPratiques(
                request.getBonnesPratiques()
        );

        evaluation.setRecommandation(
                request.getRecommandation()
        );

        // ================= RELATIONS =================
        evaluation.setRisque(risque);

        evaluation.setEvaluePar(agent);
    }

    // ================= CALCUL PRIORITE =================
    private Integer calculerRangPriorite(Integer scoreResiduel) {

        if (scoreResiduel == null) {
            return 1;
        }

        // Risque critique
        if (scoreResiduel >= 15) {
            return 3;
        }

        // Risque moyen
        if (scoreResiduel >= 8) {
            return 2;
        }

        // Risque faible
        return 1;
    }

    // ================= LIBELLE PRIORITE =================
    private String getLibellePriorite(Integer rangPriorite) {

        if (rangPriorite == null) {
            return "Zone d’observation (Actions non nécessaires - Réévaluation périodique)";
        }

        return switch (rangPriorite) {

            case 1 ->
                    "1-Zone d’audit et de traitement des risques prioritaires "
                            + "(Actions de remédiation immédiates)";

            case 2 ->
                    "2- Zone d’amélioration "
                            + "(Actions de remédiation à moyen terme)";

            case 3 ->
                    "3 -Zone d’observation "
                            + "(Actions non nécessaires - Réévaluation périodique)";

            default ->
                    "1 - Zone d’observation "
                            + "(Actions non nécessaires - Réévaluation périodique)";
        };
    }

    // ================= RESPONSE =================
    private EvaluationResponse toResponse(Evaluation e) {

        return new EvaluationResponse(

                // ================= IDENTIFIANTS =================
                e.getId(),
                e.getCode(),

                // ================= INHERENT =================
                e.getImpactInherent(),
                e.getProbabiliteInherente(),
                e.getScoreInherent(),

                // ================= MAITRISE =================
                e.getProtection(),
                e.getPrevention(),

                // ================= CONTROLES =================
                e.getControleExistants(),
                e.getControleInexistants(),
                e.getDejaSurvenu(),

                // ================= RESIDUEL =================
                e.getImpactResiduel(),
                e.getProbabiliteResiduelle(),
                e.getScoreResiduel(),

                // ================= PRIORITE =================
                e.getRangPriorite(),
                getLibellePriorite(e.getRangPriorite()),

                // ================= DATES =================
                e.getDateDebut(),
                e.getDateFin(),

                // ================= TEXTE =================
                e.getRecommandation(),
                e.getBonnesPratiques(),

                // ================= RISQUE =================
                e.getRisque() != null
                        ? e.getRisque().getId()
                        : null,

                e.getRisque() != null
                        ? e.getRisque().getLibelle()
                        : null,

                // ================= AGENT =================
                e.getEvaluePar() != null
                        ? e.getEvaluePar().getMatricule()
                        : null,

                e.getEvaluePar() != null
                        ? e.getEvaluePar().getNom()
                        : null
        );
    }
}

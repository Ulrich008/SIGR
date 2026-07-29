package com.example.SIGR.services;
import com.example.SIGR.dto.request.EvaluationRequest;
import com.example.SIGR.dto.response.CartographieRisqueDetailResponse;
import com.example.SIGR.dto.response.EvaluationResponse;
import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.EtapeValidation;
import com.example.SIGR.entity.Evaluation;
import com.example.SIGR.entity.Risque;
import com.example.SIGR.entity.StatutRisque;
import com.example.SIGR.entity.StrategieRisque;
import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.EvaluationRepository;
import com.example.SIGR.repository.RisqueRepository;
import com.example.SIGR.security.SecurityUtils;

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
        return evaluationRepository.findCartographieRisquesDetail(null);
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

        // Format : Re_<sigleUA><séquence sur 3 chiffres>, séquence propre à l'UA
        String sigleUnite = risque.getProcessus().getUnite().getCode();
        long total = evaluationRepository.countByRisque_Processus_Unite_Code(sigleUnite) + 1;
        String code = "Re_" + sigleUnite + String.format("%03d", total);

        while (evaluationRepository.existsByCode(code)) {
            total++;
            code = "Re_" + sigleUnite + String.format("%03d", total);
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

        // ================= STRATEGIE RISQUE + STATUT =================
        // Appliquer automatiquement la stratégie de risque et le statut
        // (EN_COURS / MAITRISE) au risque parent.
        boolean risqueModifie = mettreAJourStatutRisque(risque, evaluation.getRangPriorite());

        StrategieRisque strategieCalculee = evaluation.getStrategieRisqueCalculee();
        if (strategieCalculee != null) {
            risque.setStrategieRisque(strategieCalculee);
            risqueModifie = true;
        }

        if (risqueModifie) {
            risqueRepository.save(risque);
        }

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

        verifierRisqueModifiable(risque);

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

        // ================= STRATEGIE RISQUE + STATUT =================
        // Appliquer automatiquement la stratégie de risque et le statut
        // (EN_COURS / MAITRISE) au risque parent.
        boolean risqueModifie = mettreAJourStatutRisque(risque, evaluation.getRangPriorite());

        StrategieRisque strategieCalculee = evaluation.getStrategieRisqueCalculee();
        if (strategieCalculee != null) {
            risque.setStrategieRisque(strategieCalculee);
            risqueModifie = true;
        }

        if (risqueModifie) {
            risqueRepository.save(risque);
        }

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

        // ================= MAITRISE (calculée automatiquement) =================
        // Protection et prévention ne sont jamais pris depuis le client : ils
        // sont recalculés ici à partir des bonnes pratiques du risque
        // réellement présentes dans controleExistants, selon la même formule
        // que l'aperçu affiché au frontend — (nb présentes × 3) / nb total du
        // type, arrondi, plafonné à 3. Le serveur reste ainsi la source de
        // vérité même en cas d'appel API direct contournant l'UI.
        evaluation.setProtection(
                calculerScoreMaitrise(risque.getBonnesPratiquesList(), "[Protection]", request.getControleExistants())
        );

        evaluation.setPrevention(
                calculerScoreMaitrise(risque.getBonnesPratiquesList(), "[Prévention]", request.getControleExistants())
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

    /**
     * Un risque transmis (etapeValidation != FORMALISATION) est verrouillé
     * pour le Responsable des risques : son évaluation ne redevient
     * modifiable qu'une fois le risque revenu à Formalisation (différé
     * jusqu'au bout par le Pilote). Le SUPER_ADMIN n'est pas soumis à ce
     * verrou (accès total).
     */
    private void verifierRisqueModifiable(Risque risque) {
        if (SecurityUtils.hasAuthority("SUPER_ADMIN")) {
            return;
        }

        boolean enCoursDeValidation = Boolean.TRUE.equals(risque.getTransmis())
                && risque.getEtapeValidation() != EtapeValidation.FORMALISATION;

        if (enCoursDeValidation) {
            throw new RuntimeException(
                    "Ce risque est en cours de validation (transmis) : son évaluation ne peut plus être modifiée."
            );
        }
    }

    // ================= CALCUL PROTECTION / PREVENTION =================
    /**
     * (nombre de bonnes pratiques du type présentes dans controleExistants × 3)
     * / nombre total de bonnes pratiques de ce type sur le risque, arrondi et
     * plafonné à 3. Réplique exactement la formule du frontend
     * (calculateScoresFromExistants dans evaluations-form.component.ts).
     */
    private Integer calculerScoreMaitrise(
            List<String> pratiquesRisque,
            String prefixeType,
            String controleExistants
    ) {

        List<String> pratiquesDuType = pratiquesRisque.stream()
                .filter(p -> p.startsWith(prefixeType))
                .collect(Collectors.toList());

        if (pratiquesDuType.isEmpty()) {
            return 0;
        }

        String existantsMinuscule = controleExistants == null
                ? ""
                : controleExistants.toLowerCase();

        long nbPresentes = pratiquesDuType.stream()
                .filter(p -> existantsMinuscule.contains(
                        p.substring(prefixeType.length()).trim().toLowerCase()
                ))
                .count();

        long score = Math.round((nbPresentes * 3.0) / pratiquesDuType.size());
        return (int) Math.min(3, score);
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

    // ================= STATUT AUTOMATIQUE DU RISQUE =================
    /**
     * Le statut du risque est entièrement piloté par le système : ACTIF à
     * la création (voir RisqueServiceImpl), puis ici EN_COURS dès qu'une
     * évaluation existe, ou MAITRISE si le score résiduel de la dernière
     * évaluation tombe dans le palier "Faible" (rangPriorite == 1). Les
     * statuts CLOTURE/SUPPRIME sont des décisions humaines (clôture par
     * le CCI, suppression) : une évaluation ne les remet jamais en cause.
     * Renvoie true si le statut du risque a changé (pour éviter une
     * sauvegarde inutile si rien n'a bougé).
     */
    private boolean mettreAJourStatutRisque(Risque risque, Integer rangPriorite) {
        StatutRisque statutActuel = risque.getStatut();
        if (statutActuel == StatutRisque.CLOTURE || statutActuel == StatutRisque.SUPPRIME) {
            return false;
        }

        StatutRisque nouveauStatut = (rangPriorite != null && rangPriorite == 1)
                ? StatutRisque.MAITRISE
                : StatutRisque.EN_COURS;

        if (nouveauStatut == statutActuel) {
            return false;
        }

        risque.setStatut(nouveauStatut);
        return true;
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

        StrategieRisque strategieCalculee = e.getStrategieRisqueCalculee();
        String strategieDescription = null;
        
        if (strategieCalculee != null) {
            strategieDescription = switch (strategieCalculee) {
                case TRAITER -> "Mettre en place des dispositifs de contrôle interne pour réduire le risque";
                case TRANSFERER -> "Partager le risque avec une autre entité (ex : contrat d'assurance, externalisation, sous-traitance)";
                case TOLERER -> "Ne prendre aucune mesure particulière - le risque est jugé comme étant acceptable";
                case TERMINER -> "Abandonner l'activité à laquelle le risque est lié";
            };
        }

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

                // ================= STRATEGIE RISQUE =================
                strategieCalculee,
                strategieDescription,

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
                        ? e.getRisque().getCode()
                        : null,

                e.getRisque() != null
                        ? e.getRisque().getLibelle()
                        : null,

                e.getRisque() != null
                        ? e.getRisque().getTransmis()
                        : null,

                e.getRisque() != null
                        ? e.getRisque().getEtapeValidation()
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

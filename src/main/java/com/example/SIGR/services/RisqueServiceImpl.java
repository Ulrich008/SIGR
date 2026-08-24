package com.example.SIGR.services;

import com.example.SIGR.dto.request.AvisRisqueRequest;
import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.request.SuiviRecommandationRequest;
import com.example.SIGR.dto.response.AvisHistoriqueResponse;
import com.example.SIGR.dto.response.RisqueResponse;
import com.example.SIGR.entity.*;
import com.example.SIGR.repository.*;
import com.example.SIGR.security.SecurityUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.query.AuditEntity;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RisqueServiceImpl implements RisqueService {

    private final RisqueRepository risqueRepository;
    private final ProcessusRepository processusRepository;
    private final CartographieRisquesRepository cartographieRepository;
    private final AgentRepository agentRepository;
    private final EvaluationRepository evaluationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public RisqueServiceImpl(
            RisqueRepository risqueRepository,
            ProcessusRepository processusRepository,
            CartographieRisquesRepository cartographieRepository,
            AgentRepository agentRepository,
            EvaluationRepository evaluationRepository
    ) {
        this.risqueRepository = risqueRepository;
        this.processusRepository = processusRepository;
        this.cartographieRepository = cartographieRepository;
        this.agentRepository = agentRepository;
        this.evaluationRepository = evaluationRepository;
    }

    /**
     * ================= CREATION =================
     */
    @Override
    public RisqueResponse create(
            RisqueRequest request
    ) {

        // ================= LIBELLE =================

        if (risqueRepository.existsByLibelleIgnoreCase(
                request.getLibelle()
        )) {

            throw new RuntimeException(
                    "Libellé déjà utilisé : "
                            + request.getLibelle()
            );
        }

        // ================= PROCESSUS =================

        Processus processus =
                processusRepository
                        .findByCode(request.getCodeProcessus())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Processus introuvable : "
                                                + request.getCodeProcessus()
                                )
                        );

        // ================= FINALITE =================

        verifierFinaliteAppartientAuProcessus(processus, request.getFinalite());

        // ================= CARTOGRAPHIE =================

        CartographieRisques cartographie =
                getCartographie(
                        request.getCodeCartographie()
                );

        // ================= GENERATION CODE =================
        // Format : R_<sigleUA><séquence sur 3 chiffres>, séquence propre à l'UA

        String sigleUnite = processus.getUnite().getCode();

        long total =
                risqueRepository.countByProcessus_Unite_Code(sigleUnite) + 1;

        String code =
                "R_" + sigleUnite + String.format("%03d", total);

        while (risqueRepository.existsByCode(code)) {

            total++;

            code = "R_" + sigleUnite + String.format("%03d", total);
        }

        // ================= CREATION =================

        Risque risque = new Risque();

        risque.setCode(code);

        risque.setLibelle(request.getLibelle());
        risque.setFinalite(request.getFinalite());
        risque.setCauseProbableList(request.getCauseProbable());
        risque.setConsequenceProbableList(
                request.getConsequenceProbable()
        );
        risque.setBonnesPratiquesList(request.getBonnesPratiques());
        // Le statut est désormais entièrement piloté par le système : ACTIF
        // à la création, puis EN_COURS/MAITRISE selon les évaluations
        // (voir EvaluationServiceImpl), et CLOTURE via l'action dédiée du
        // CCI (voir cloturer() ci-dessous) — jamais saisi manuellement.
        risque.setStatut(StatutRisque.ACTIF);
        risque.setStrategieRisque(request.getStrategieRisque());
        risque.setDateIdentification(
                request.getDateIdentification()
        );
        risque.setTypeRisque(
                request.getTypeRisque()
        );

        risque.setProcessus(processus);
        risque.setCartographie(cartographie);
        risque.setAvis(request.getAvis());
        risque.setMotif(request.getMotif());
        risque.setTransmis(request.getTransmis() != null ? request.getTransmis() : false);

        Risque saved =
                risqueRepository.save(risque);

        return toResponse(saved);
    }

    /**
     * ================= GET BY CODE =================
     */
    @Override
    public RisqueResponse getByCode(
            String code
    ) {

        Risque risque =
                risqueRepository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Risque introuvable : "
                                                + code
                                )
                        );

        return toResponse(risque);
    }

    /**
     * ================= GET ALL =================
     */
    @Override
    public List<RisqueResponse> getAll() {

        return risqueRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * ================= UPDATE =================
     */
    @Override
    public RisqueResponse updateByCode(
            String code,
            RisqueRequest request
    ) {

        Risque risque =
                risqueRepository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Risque introuvable : "
                                                + code
                                )
                        );

        verifierRisqueModifiable(risque);

        return updateEntity(
                risque,
                request
        );
    }

    /**
     * Un risque transmis (etapeValidation != FORMALISATION) est verrouillé
     * pour le Responsable des risques : il ne redevient modifiable qu'une
     * fois revenu à Formalisation (différé jusqu'au bout par le Pilote).
     * Le SUPER_ADMIN n'est pas soumis à ce verrou (accès total).
     */
    private void verifierRisqueModifiable(Risque risque) {
        if (SecurityUtils.hasAuthority("SUPER_ADMIN")) {
            return;
        }

        boolean enCoursDeValidation = Boolean.TRUE.equals(risque.getTransmis())
                && risque.getEtapeValidation() != EtapeValidation.FORMALISATION;

        if (enCoursDeValidation) {
            throw new RuntimeException(
                    "Ce risque est en cours de validation (transmis) et ne peut plus être modifié."
            );
        }
    }

    /**
     * ================= TRANSMETTRE =================
     *
     * Relaie le dossier à l'étape suivante du circuit — sans avis, à la
     * différence de validerAvis() ci-dessous. Utilisé par :
     * - le Correspondant Risque (ou le Manager Risque) pour faire entrer le
     *   dossier dans le circuit (Formalisation -> Manager Risque) ;
     * - le Manager Risque pour relayer vers le CCI (-> CCI_VERS_RESPONSABLE),
     *   ou, si le dossier revient d'un différé du Responsable/CMMR, pour le
     *   renvoyer au Correspondant pour correction (-> Formalisation) ;
     * - le CCI, deux fois, pour relayer vers le Responsable puis vers le
     *   CMMR (CCI_VERS_RESPONSABLE -> Responsable, CCI_VERS_CMMR -> CMMR).
     */
    @Override
    public RisqueResponse transmettre(String code) {

        Risque risque =
                risqueRepository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Risque introuvable : " + code
                                )
                        );

        EtapeValidation etapeActuelle = risque.getEtapeValidation();

        verifierAutoriteTransmission(etapeActuelle);

        switch (etapeActuelle) {
            case FORMALISATION -> {
                if (!evaluationRepository.existsByRisque_Code(code)) {
                    throw new RuntimeException(
                            "Ce risque doit être évalué avant de pouvoir être transmis"
                    );
                }
                risque.setEtapeValidation(EtapeValidation.MANAGER_RISQUE);
                risque.setAvis(AvisRisque.EN_ATTENTE);
                risque.setTransmis(true);
                // Le motif d'un éventuel différé précédent est volontairement
                // conservé (pas de remise à null) : il reste consultable tant
                // qu'un nouvel avis ne l'a pas explicitement remplacé.
            }
            case MANAGER_RISQUE -> {
                if (risque.getAvis() == AvisRisque.DIFFERE) {
                    // Le dossier revient d'un différé du Responsable ou du
                    // CMMR : le Manager Risque le renvoie au Correspondant
                    // pour correction, plutôt que de le relayer vers le CCI.
                    risque.setEtapeValidation(EtapeValidation.FORMALISATION);
                    risque.setTransmis(false);
                } else {
                    risque.setEtapeValidation(EtapeValidation.CCI_VERS_RESPONSABLE);
                }
            }
            case CCI_VERS_RESPONSABLE -> risque.setEtapeValidation(EtapeValidation.RESPONSABLE);
            case CCI_VERS_CMMR -> risque.setEtapeValidation(EtapeValidation.CMMR);
            default -> throw new RuntimeException(
                    "Ce risque n'est pas à une étape de transmission (il attend un avis, ou le circuit est terminé)."
            );
        }

        return toResponse(risqueRepository.save(risque));
    }

    /**
     * Seul le profil chargé de relayer l'étape actuelle peut transmettre
     * (SUPER_ADMIN contourne cette règle).
     */
    private void verifierAutoriteTransmission(EtapeValidation etape) {

        if (SecurityUtils.hasAuthority("SUPER_ADMIN")) {
            return;
        }

        boolean autorise = switch (etape) {
            case FORMALISATION -> SecurityUtils.hasAuthority("CORRESPONDANT_RISQUE")
                    || SecurityUtils.hasAuthority("MANAGER_RISQUE");
            case MANAGER_RISQUE -> SecurityUtils.hasAuthority("MANAGER_RISQUE");
            case CCI_VERS_RESPONSABLE, CCI_VERS_CMMR -> SecurityUtils.hasAuthority("CCI");
            default -> false;
        };

        if (!autorise) {
            throw new AccessDeniedException(
                    "Ce dossier n'est pas à votre étape de transmission"
            );
        }
    }

    /**
     * ================= VALIDER / DIFFÉRER / REJETER =================
     *
     * Action réservée aux deux profils qui rendent un véritable avis dans
     * le circuit (Responsable Risque, CMMR) : contrairement à
     * updateByCode, ne modifie que l'avis, le motif et l'étape du
     * circuit — jamais le contenu du risque lui-même.
     *
     * - Valider  : passage à l'étape suivante (CCI_VERS_CMMR depuis
     *   Responsable, VALIDEE depuis CMMR).
     * - Différer : retour à Manager Risque, qui renverra ensuite le dossier
     *   au Correspondant pour correction (motif obligatoire).
     * - Rejeter  : clôture définitive, sans retour (motif obligatoire).
     */
    @Override
    public RisqueResponse validerAvis(
            String code,
            AvisRisqueRequest request
    ) {

        Risque risque =
                risqueRepository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Risque introuvable : "
                                                + code
                                )
                        );

        EtapeValidation etapeActuelle = risque.getEtapeValidation();

        verifierEtapeCorrespondAuProfil(etapeActuelle);

        boolean motifRequis =
                request.getAvis() == AvisRisque.DIFFERE
                        || request.getAvis() == AvisRisque.REJETE;

        if (motifRequis
                && (request.getMotif() == null || request.getMotif().isBlank())) {

            throw new RuntimeException(
                    "Le motif est obligatoire en cas de différé ou de rejet"
            );
        }

        risque.setAvis(request.getAvis());
        risque.setMotif(request.getMotif());

        String matriculeEmetteur = SecurityUtils.getCurrentUser();
        if (matriculeEmetteur != null) {
            agentRepository.findByMatricule(matriculeEmetteur)
                    .ifPresent(risque::setEmetteurAvis);
        }

        switch (request.getAvis()) {
            case VALIDE -> risque.setEtapeValidation(etapeSuivante(etapeActuelle));
            // Un différé (Responsable ou CMMR) revient toujours à Manager
            // Risque, qui le relaiera explicitement au Correspondant pour
            // correction via un nouveau transmettre() (voir ce méthode).
            case DIFFERE -> risque.setEtapeValidation(EtapeValidation.MANAGER_RISQUE);
            case REJETE -> risque.setEtapeValidation(EtapeValidation.REJETEE);
            default -> { /* EN_ATTENTE : ne devrait pas arriver ici */ }
        }

        return toResponse(risqueRepository.save(risque));
    }

    /**
     * Un dossier ne peut recevoir un avis que du profil correspondant à
     * son étape actuelle (SUPER_ADMIN contourne cette règle).
     */
    private void verifierEtapeCorrespondAuProfil(EtapeValidation etapeActuelle) {

        if (SecurityUtils.hasAuthority("SUPER_ADMIN")) {
            return;
        }

        boolean autorise = switch (etapeActuelle) {
            case RESPONSABLE -> SecurityUtils.hasAuthority("RESPONSABLE_RISQUES");
            case CMMR -> SecurityUtils.hasAuthority("CMMR");
            default -> false;
        };

        if (!autorise) {
            throw new AccessDeniedException(
                    "Ce dossier n'est pas à votre étape de validation"
            );
        }
    }

    private EtapeValidation etapeSuivante(EtapeValidation etape) {
        return switch (etape) {
            case RESPONSABLE -> EtapeValidation.CCI_VERS_CMMR;
            case CMMR -> EtapeValidation.VALIDEE;
            default -> etape;
        };
    }

    /**
     * ================= DELETE =================
     */
    @Override
    public void deleteByCode(
            String code
    ) {

        Risque risque =
                risqueRepository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Risque introuvable : "
                                                + code
                                )
                        );

        risqueRepository.delete(risque);
    }

    /**
     * ================= CLÔTURE =================
     *
     * Action manuelle réservée au CCI (indépendante du circuit de
     * validation de la cartographie) : confirme que le risque est
     * effectivement résolu, à n'importe quelle étape.
     */
    @Override
    public RisqueResponse cloturer(String code) {

        Risque risque = risqueRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Risque introuvable : " + code)
                );

        if (risque.getStatut() == StatutRisque.CLOTURE) {
            throw new RuntimeException("Ce risque est déjà clôturé.");
        }

        risque.setStatut(StatutRisque.CLOTURE);

        return toResponse(risqueRepository.save(risque));
    }

    /**
     * ================= HISTORIQUE DES AVIS DE VALIDATION =================
     *
     * avis/motif/etapeValidation sont de simples colonnes sur Risque,
     * écrasées à chaque nouvelle décision (voir validerAvis/transmettre) :
     * seul le dernier avis est donc visible via RisqueResponse. Ici, on
     * reconstitue l'historique complet en relisant les révisions Envers de
     * l'entité (déjà activées via @Audited sur Risque, jusque-là jamais
     * exploitées), en ne gardant que les révisions où avis, motif ou
     * étape ont réellement changé — les autres sauvegardes de Risque
     * (déclenchées par exemple par la création d'une évaluation) sont
     * ainsi filtrées.
     */
    @Override
    public List<AvisHistoriqueResponse> getHistoriqueAvis(String code) {

        Risque risque = risqueRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Risque introuvable : " + code)
                );

        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        @SuppressWarnings("unchecked")
        List<Object[]> revisions = auditReader.createQuery()
                .forRevisionsOfEntity(Risque.class, false, true)
                .add(AuditEntity.id().eq(risque.getId()))
                .addOrder(AuditEntity.revisionNumber().asc())
                .getResultList();

        List<AvisHistoriqueResponse> historique = new ArrayList<>();

        // État "avant création" : permet à la boucle de détecter tout
        // changement dès la première transmission, sans faire apparaître
        // la révision de création elle-même (avis encore null à ce stade).
        AvisRisque dernierAvis = null;
        EtapeValidation derniereEtape = EtapeValidation.FORMALISATION;
        String dernierMotif = null;

        for (Object[] tuple : revisions) {

            Risque snapshot = (Risque) tuple[0];
            DefaultRevisionEntity revisionInfo = (DefaultRevisionEntity) tuple[1];

            AvisRisque avisSnapshot = snapshot.getAvis();
            EtapeValidation etapeSnapshot = snapshot.getEtapeValidation();
            String motifSnapshot = snapshot.getMotif();

            boolean change =
                    !Objects.equals(avisSnapshot, dernierAvis)
                            || !Objects.equals(etapeSnapshot, derniereEtape)
                            || !Objects.equals(motifSnapshot, dernierMotif);

            if (change) {

                String matriculeAuteur = snapshot.getUpdatedBy();

                String nomAuteur = matriculeAuteur != null
                        ? agentRepository.findByMatricule(matriculeAuteur)
                                .map(a -> a.getNom() + " " + a.getPrenoms())
                                .orElse(matriculeAuteur)
                        : null;

                historique.add(new AvisHistoriqueResponse(
                        LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(revisionInfo.getTimestamp()),
                                ZoneId.systemDefault()
                        ),
                        avisSnapshot,
                        motifSnapshot,
                        etapeSnapshot,
                        matriculeAuteur,
                        nomAuteur
                ));
            }

            dernierAvis = avisSnapshot;
            derniereEtape = etapeSnapshot;
            dernierMotif = motifSnapshot;
        }

        return historique;
    }

    /**
     * ================= UPDATE ENTITY =================
     */
    private RisqueResponse updateEntity(
            Risque risque,
            RisqueRequest request
    ) {

        // ================= LIBELLE =================

        if (risqueRepository.existsByLibelleIgnoreCaseAndCodeNot(
                request.getLibelle(), risque.getCode()
        )) {

            throw new RuntimeException(
                    "Libellé déjà utilisé : " + request.getLibelle()
            );
        }

        // ================= PROCESSUS =================

        Processus processus =
                processusRepository
                        .findByCode(request.getCodeProcessus())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Processus introuvable : "
                                                + request.getCodeProcessus()
                                )
                        );

        // ================= FINALITE =================

        verifierFinaliteAppartientAuProcessus(processus, request.getFinalite());

        // ================= CARTOGRAPHIE =================

        CartographieRisques cartographie =
                getCartographie(
                        request.getCodeCartographie()
                );

        // ================= UPDATE =================

        risque.setLibelle(request.getLibelle());
        risque.setFinalite(request.getFinalite());
        risque.setCauseProbableList(
                request.getCauseProbable()
        );
        risque.setConsequenceProbableList(
                request.getConsequenceProbable()
        );
        risque.setBonnesPratiquesList(request.getBonnesPratiques());
        risque.setStrategieRisque(request.getStrategieRisque());
        risque.setDateIdentification(
                request.getDateIdentification()
        );
        risque.setTypeRisque(
                request.getTypeRisque()
        );

        risque.setProcessus(processus);
        risque.setCartographie(cartographie);
        // avis / motif / transmis / etapeValidation ne sont volontairement PAS
        // repris depuis la requête ici : ce sont les seules données du circuit
        // de validation, et cette mise à jour générique du contenu du risque
        // (accessible à MANAGER_RISQUE/SUPER_ADMIN) ne doit pas pouvoir
        // les contourner. Seuls transmettre() et validerAvis() — avec leurs
        // vérifications d'étape et de motif obligatoire — sont autorisés à
        // les modifier.
        // statut n'est pas non plus repris : il est désormais piloté par le
        // système (ACTIF à la création, EN_COURS/MAITRISE selon les
        // évaluations, CLOTURE via cloturer() réservé au CCI).

        Risque updated =
                risqueRepository.save(risque);

        return toResponse(updated);
    }

    /**
     * ================= RESPONSE =================
     */
    private RisqueResponse toResponse(
            Risque risque
    ) {

        return new RisqueResponse(
                risque.getId(),
                risque.getCode(),
                risque.getLibelle(),
                risque.getFinalite(),
                risque.getCauseProbableList(),
                risque.getConsequenceProbableList(),
                risque.getBonnesPratiquesList(),
                risque.getStatut(),
                risque.getStrategieRisque(),
                risque.getDateIdentification(),

                risque.getProcessus() != null
                        ? risque.getProcessus().getCode()
                        : null,

                risque.getProcessus() != null
                        ? risque.getProcessus().getLibelle()
                        : null,

                risque.getCartographie() != null
                        ? risque.getCartographie().getCode()
                        : null,

                risque.getTypeRisque(),

                risque.getAvis(),
                risque.getMotif(),
                risque.getTransmis() != null ? risque.getTransmis() : false,
                risque.getEtapeValidation(),

                risque.getEmetteurAvis() != null
                        ? risque.getEmetteurAvis().getMatricule()
                        : null,

                risque.getEmetteurAvis() != null
                        ? risque.getEmetteurAvis().getNom() + " " + risque.getEmetteurAvis().getPrenoms()
                        : null,

                risque.getEmetteurAvis() != null && risque.getEmetteurAvis().getProfil() != null
                        ? risque.getEmetteurAvis().getProfil().getCode()
                        : null,

                risque.getEmetteurAvis() != null && risque.getEmetteurAvis().getProfil() != null
                        ? risque.getEmetteurAvis().getProfil().getLibelle()
                        : null,

                evaluationRepository.existsByRisque_Code(risque.getCode()),

                risque.getStatutSuivi(),
                risque.getDecisionSuivi(),
                risque.getDateDecisionSuivi()
        );
    }

    /**
     * ================= SUIVI DES ACTIONS DE MITIGATION =================
     */
    @Override
    public RisqueResponse enregistrerSuivi(String code, SuiviRecommandationRequest request) {
        Risque risque = risqueRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Risque introuvable : " + code));

        risque.setStatutSuivi(request.getStatutSuivi());
        risque.setDecisionSuivi(request.getDecision());
        risque.setDateDecisionSuivi(LocalDateTime.now());

        return toResponse(risqueRepository.save(risque));
    }

    /**
     * La finalité renseignée doit être l'une de celles réellement formalisées
     * sur le processus sélectionné (Processus.finalite, texte séparé par des
     * points-virgules) : évite qu'un risque se retrouve rattaché à une
     * finalité arbitraire, y compris en cas d'appel API direct contournant
     * le sélecteur du formulaire.
     */
    private void verifierFinaliteAppartientAuProcessus(Processus processus, String finalite) {

        List<String> finalitesDuProcessus = processus.getFinalite() == null
                ? List.of()
                : java.util.Arrays.stream(processus.getFinalite().split(";"))
                        .map(String::trim)
                        .filter(f -> !f.isEmpty())
                        .collect(Collectors.toList());

        boolean appartient = finalitesDuProcessus.stream()
                .anyMatch(f -> f.equalsIgnoreCase(finalite == null ? null : finalite.trim()));

        if (!appartient) {
            throw new RuntimeException(
                    "La finalité sélectionnée ne fait pas partie des finalités du processus " + processus.getCode()
            );
        }
    }

    /**
     * ================= CARTOGRAPHIE =================
     */
    private CartographieRisques getCartographie(
            String codeCartographie
    ) {

        if (codeCartographie == null
                || codeCartographie.isBlank()) {

            return null;
        }

        return cartographieRepository
                .findByCode(codeCartographie)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cartographie introuvable : "
                                        + codeCartographie
                        )
                );
    }
}
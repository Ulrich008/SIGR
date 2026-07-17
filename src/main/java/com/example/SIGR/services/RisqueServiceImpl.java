package com.example.SIGR.services;

import com.example.SIGR.dto.request.AvisRisqueRequest;
import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.response.RisqueResponse;
import com.example.SIGR.entity.*;
import com.example.SIGR.repository.*;
import com.example.SIGR.security.SecurityUtils;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RisqueServiceImpl implements RisqueService {

    private final RisqueRepository risqueRepository;
    private final ProcessusRepository processusRepository;
    private final CartographieRisquesRepository cartographieRepository;

    public RisqueServiceImpl(
            RisqueRepository risqueRepository,
            ProcessusRepository processusRepository,
            CartographieRisquesRepository cartographieRepository
    ) {
        this.risqueRepository = risqueRepository;
        this.processusRepository = processusRepository;
        this.cartographieRepository = cartographieRepository;
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
        risque.setCauseProbableList(request.getCauseProbable());
        risque.setConsequenceProbableList(
                request.getConsequenceProbable()
        );
        risque.setBonnesPratiquesList(request.getBonnesPratiques());
        risque.setStatut(request.getStatut());
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

        return updateEntity(
                risque,
                request
        );
    }

    /**
     * ================= TRANSMETTRE =================
     *
     * Le Responsable des risques fait entrer le dossier dans le
     * circuit de validation : Formalisation -> Pilote.
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

        if (risque.getEtapeValidation() != EtapeValidation.FORMALISATION) {
            throw new RuntimeException(
                    "Ce risque a déjà été transmis et est en cours de validation"
            );
        }

        risque.setEtapeValidation(EtapeValidation.PILOTE);
        risque.setAvis(AvisRisque.EN_ATTENTE);
        risque.setMotif(null);
        risque.setTransmis(true);

        return toResponse(risqueRepository.save(risque));
    }

    /**
     * ================= VALIDER / DIFFÉRER / REJETER =================
     *
     * Action réservée aux profils de validation (CMMR, CCI, Pilote de
     * processus) : contrairement à updateByCode, ne modifie que
     * l'avis, le motif et l'étape du circuit — jamais le contenu du
     * risque lui-même.
     *
     * Circuit : Pilote -> CCI -> CMMR -> Validée.
     * - Valider  : passage à l'étape suivante (Validée si déjà CMMR).
     * - Différer : retour à l'étape précédente (motif obligatoire).
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

        switch (request.getAvis()) {
            case VALIDE -> risque.setEtapeValidation(etapeSuivante(etapeActuelle));
            case DIFFERE -> risque.setEtapeValidation(etapePrecedente(etapeActuelle));
            case REJETE -> risque.setEtapeValidation(EtapeValidation.REJETEE);
            default -> { /* EN_ATTENTE : ne devrait pas arriver ici */ }
        }

        return toResponse(risqueRepository.save(risque));
    }

    /**
     * Un dossier ne peut être traité que par le profil correspondant
     * à son étape actuelle (SUPER_ADMIN contourne cette règle).
     */
    private void verifierEtapeCorrespondAuProfil(EtapeValidation etapeActuelle) {

        if (SecurityUtils.hasAuthority("SUPER_ADMIN")) {
            return;
        }

        boolean autorise = switch (etapeActuelle) {
            case PILOTE -> SecurityUtils.hasAuthority("PILOTE");
            case CCI -> SecurityUtils.hasAuthority("CCI");
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
            case PILOTE -> EtapeValidation.CCI;
            case CCI -> EtapeValidation.CMMR;
            case CMMR -> EtapeValidation.VALIDEE;
            default -> etape;
        };
    }

    private EtapeValidation etapePrecedente(EtapeValidation etape) {
        return switch (etape) {
            case PILOTE -> EtapeValidation.FORMALISATION;
            case CCI -> EtapeValidation.PILOTE;
            case CMMR -> EtapeValidation.CCI;
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

        // ================= CARTOGRAPHIE =================

        CartographieRisques cartographie =
                getCartographie(
                        request.getCodeCartographie()
                );

        // ================= UPDATE =================

        risque.setLibelle(request.getLibelle());
        risque.setCauseProbableList(
                request.getCauseProbable()
        );
        risque.setConsequenceProbableList(
                request.getConsequenceProbable()
        );
        risque.setBonnesPratiquesList(request.getBonnesPratiques());
        risque.setStatut(request.getStatut());
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
        if (request.getTransmis() != null) {
            risque.setTransmis(request.getTransmis());
        }

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
                risque.getEtapeValidation()
        );
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
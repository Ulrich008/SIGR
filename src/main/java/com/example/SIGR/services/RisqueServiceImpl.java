package com.example.SIGR.services;

import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.response.RisqueResponse;
import com.example.SIGR.entity.*;
import com.example.SIGR.repository.*;

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

        long total =
                risqueRepository.count() + 1;

        String code =
                String.format(
                        "RIS-%03d",
                        total
                );

        while (risqueRepository.existsByCode(code)) {

            total++;

            code = String.format(
                    "RIS-%03d",
                    total
            );
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

                risque.getRisquesResiduels() != null
                        ? risque.getRisquesResiduels()
                          .stream()
                          .map(RisqueResiduel::getCode)
                          .toList()
                        : List.of(),

                risque.getAvis(),
                risque.getMotif(),
                risque.getTransmis() != null ? risque.getTransmis() : false
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
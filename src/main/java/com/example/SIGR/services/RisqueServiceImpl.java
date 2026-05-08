package com.example.SIGR.services;

import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.response.RisqueResponse;
import com.example.SIGR.entity.CartographieRisques;
import com.example.SIGR.entity.Processus;
import com.example.SIGR.entity.Risque;
import com.example.SIGR.repository.CartographieRisquesRepository;
import com.example.SIGR.repository.ProcessusRepository;
import com.example.SIGR.repository.RisqueRepository;

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

    // ================= CREATE =================
    @Override
    public RisqueResponse create(RisqueRequest request) {

        if (risqueRepository.existsById(request.getId())) {
            throw new RuntimeException(
                    "Le code du risque existe déjà : " + request.getId()
            );
        }

        if (risqueRepository.existsByLibelle(request.getLibelle())) {
            throw new RuntimeException(
                    "Un risque avec ce libellé existe déjà : " + request.getLibelle()
            );
        }

        Processus processus = processusRepository.findById(request.getCodeProcessus())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Processus introuvable : " + request.getCodeProcessus()
                        )
                );

        CartographieRisques cartographie = null;

        if (request.getIdCartographie() != null
                && !request.getIdCartographie().isBlank()) {

            cartographie = cartographieRepository.findById(request.getIdCartographie())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Cartographie introuvable : "
                                            + request.getIdCartographie()
                            )
                    );
        }

        Risque risque = new Risque();

        risque.setId(request.getId());
        risque.setLibelle(request.getLibelle());
        risque.setCategorie(request.getCategorie());
        risque.setCauseProbable(request.getCauseProbable());
        risque.setConsequenceProbable(request.getConsequenceProbable());
        risque.setStatut(request.getStatut());
        risque.setDateIdentification(request.getDateIdentification());
        risque.setTypeRisque(request.getTypeRisque());

        risque.setProcessus(processus);
        risque.setCartographie(cartographie);

        Risque saved = risqueRepository.save(risque);

        return toResponse(saved);
    }

    // ================= GET BY ID =================
    @Override
    public RisqueResponse getById(String id) {

        Risque risque = risqueRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Risque introuvable : " + id)
                );

        return toResponse(risque);
    }

    // ================= GET ALL =================
    @Override
    public List<RisqueResponse> getAll() {

        return risqueRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @Override
    public RisqueResponse update(String id, RisqueRequest request) {

        Risque risque = risqueRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Risque introuvable : " + id)
                );

        if (risqueRepository.existsByLibelle(request.getLibelle())
                && !risque.getLibelle().equals(request.getLibelle())) {

            throw new RuntimeException(
                    "Un risque avec ce libellé existe déjà : "
                            + request.getLibelle()
            );
        }

        Processus processus = processusRepository.findById(request.getCodeProcessus())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Processus introuvable : "
                                        + request.getCodeProcessus()
                        )
                );

        CartographieRisques cartographie = null;

        if (request.getIdCartographie() != null
                && !request.getIdCartographie().isBlank()) {

            cartographie = cartographieRepository.findById(request.getIdCartographie())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Cartographie introuvable : "
                                            + request.getIdCartographie()
                            )
                    );
        }

        risque.setLibelle(request.getLibelle());
        risque.setCategorie(request.getCategorie());
        risque.setCauseProbable(request.getCauseProbable());
        risque.setConsequenceProbable(request.getConsequenceProbable());
        risque.setStatut(request.getStatut());
        risque.setDateIdentification(request.getDateIdentification());
        risque.setTypeRisque(request.getTypeRisque());

        risque.setProcessus(processus);
        risque.setCartographie(cartographie);

        Risque updated = risqueRepository.save(risque);

        return toResponse(updated);
    }

    // ================= DELETE =================
    @Override
    public void delete(String id) {

        if (!risqueRepository.existsById(id)) {
            throw new RuntimeException("Risque introuvable : " + id);
        }

        risqueRepository.deleteById(id);
    }

    // ================= MAPPER =================
    private RisqueResponse toResponse(Risque risque) {

        return new RisqueResponse(
                risque.getId(),
                risque.getLibelle(),
                risque.getCategorie(),
                risque.getCauseProbable(),
                risque.getConsequenceProbable(),
                risque.getStatut(),
                risque.getDateIdentification(),
                risque.getProcessus().getCode(),
                risque.getProcessus().getNom(),
                risque.getCartographie() != null
                        ? risque.getCartographie().getId()
                        : null,
                risque.getTypeRisque()
        );
    }
}
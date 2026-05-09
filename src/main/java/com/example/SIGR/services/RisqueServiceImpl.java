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

    @Override
    public RisqueResponse create(RisqueRequest request) {

        if (risqueRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Code déjà utilisé : " + request.getCode());
        }

        if (risqueRepository.existsByLibelleIgnoreCase(request.getLibelle())) {
            throw new RuntimeException("Libellé déjà utilisé : " + request.getLibelle());
        }

        Processus processus = processusRepository.findById(request.getCodeProcessus())
                .orElseThrow(() -> new RuntimeException("Processus introuvable : " + request.getCodeProcessus()));

        CartographieRisques cartographie = getCartographie(request.getIdCartographie());

        Risque risque = new Risque();
        risque.setCode(request.getCode());
        risque.setLibelle(request.getLibelle());
        risque.setCategorie(request.getCategorie());
        risque.setCauseProbable(request.getCauseProbable());
        risque.setConsequenceProbable(request.getConsequenceProbable());
        risque.setStatut(request.getStatut());
        risque.setDateIdentification(request.getDateIdentification());
        risque.setTypeRisque(request.getTypeRisque());

        risque.setProcessus(processus);
        risque.setCartographie(cartographie);

        return toResponse(risqueRepository.save(risque));
    }

    @Override
    public RisqueResponse getById(String id) {
        Risque risque = risqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Risque introuvable : " + id));

        return toResponse(risque);
    }

    @Override
    public RisqueResponse getByCode(String code) {
        Risque risque = risqueRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Risque introuvable : " + code));

        return toResponse(risque);
    }

    @Override
    public List<RisqueResponse> getAll() {
        return risqueRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RisqueResponse updateById(String id, RisqueRequest request) {

        Risque risque = risqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Risque introuvable : " + id));

        return updateEntity(risque, request);
    }

    @Override
    public RisqueResponse updateByCode(String code, RisqueRequest request) {

        Risque risque = risqueRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Risque introuvable : " + code));

        return updateEntity(risque, request);
    }

    @Override
    public void delete(String id) {
        risqueRepository.deleteById(id);
    }

    @Override
    public void deleteByCode(String code) {
        Risque risque = risqueRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Risque introuvable : " + code));

        risqueRepository.delete(risque);
    }

    private RisqueResponse updateEntity(Risque risque, RisqueRequest request) {

        Processus processus = processusRepository.findById(request.getCodeProcessus())
                .orElseThrow(() -> new RuntimeException("Processus introuvable : " + request.getCodeProcessus()));

        CartographieRisques cartographie = getCartographie(request.getIdCartographie());

        risque.setLibelle(request.getLibelle());
        risque.setCategorie(request.getCategorie());
        risque.setCauseProbable(request.getCauseProbable());
        risque.setConsequenceProbable(request.getConsequenceProbable());
        risque.setStatut(request.getStatut());
        risque.setDateIdentification(request.getDateIdentification());
        risque.setTypeRisque(request.getTypeRisque());
        risque.setProcessus(processus);
        risque.setCartographie(cartographie);

        return toResponse(risqueRepository.save(risque));
    }

    private RisqueResponse toResponse(Risque risque) {
        return new RisqueResponse(
                risque.getId(),
                risque.getCode(),
                risque.getLibelle(),
                risque.getCategorie(),
                risque.getCauseProbable(),
                risque.getConsequenceProbable(),
                risque.getStatut(),
                risque.getDateIdentification(),
                risque.getProcessus().getCode(),
                risque.getProcessus().getLibelle(),
                risque.getCartographie() != null ? risque.getCartographie().getId() : null,
                risque.getTypeRisque(),
                risque.getRisquesResiduels() != null
                        ? risque.getRisquesResiduels().stream().map(RisqueResiduel::getId).toList()
                        : List.of()
        );
    }

    private CartographieRisques getCartographie(String idCarto) {
        if (idCarto == null || idCarto.isBlank()) return null;

        return cartographieRepository.findById(idCarto)
                .orElseThrow(() -> new RuntimeException("Cartographie introuvable : " + idCarto));
    }
}
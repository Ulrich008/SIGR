package com.example.SIGR.services;

import com.example.SIGR.dto.request.CartographieRisquesRequest;
import com.example.SIGR.dto.response.CartographieRisquesResponse;
import com.example.SIGR.entity.CartographieRisques;
import com.example.SIGR.repository.CartographieRisquesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartographieRisquesServiceImpl implements CartographieRisquesService {

    private final CartographieRisquesRepository repository;

    public CartographieRisquesServiceImpl(CartographieRisquesRepository repository) {
        this.repository = repository;
    }

    // ================= GENERATE CODE =================
    private String generateCode() {

        long count = repository.count() + 1;

        return String.format("CARTO-%03d", count);
    }

    // ================= CREATE =================
    @Override
    public CartographieRisquesResponse create(CartographieRisquesRequest request) {

        if (repository.existsByTitre(request.getTitre())) {
            throw new RuntimeException(
                    "Titre déjà utilisé : " + request.getTitre());
        }

        String code = generateCode();

        CartographieRisques entity = new CartographieRisques()
                .setCode(code)
                .setTitre(request.getTitre())
                .setPeriode(request.getPeriode())
                .setSeuilFaible(request.getSeuilFaible())
                .setSeuilMoyen(request.getSeuilMoyen())
                .setSeuilEleve(request.getSeuilEleve())
                .setStatut(request.getStatut());

        return toResponse(repository.save(entity));
    }

    // ================= GET BY CODE =================
    @Override
    public CartographieRisquesResponse getByCode(String code) {

        CartographieRisques entity = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Cartographie introuvable : " + code));

        return toResponse(entity);
    }

    // ================= GET ALL =================
    @Override
    public List<CartographieRisquesResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @Override
    public CartographieRisquesResponse update(
            String code,
            CartographieRisquesRequest request
    ) {

        CartographieRisques entity = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Cartographie introuvable : " + code));

        // Vérification unicité titre
        if (!entity.getTitre().equals(request.getTitre())
                && repository.existsByTitre(request.getTitre())) {

            throw new RuntimeException(
                    "Titre déjà utilisé : " + request.getTitre());
        }

        entity.setTitre(request.getTitre());
        entity.setPeriode(request.getPeriode());
        entity.setSeuilFaible(request.getSeuilFaible());
        entity.setSeuilMoyen(request.getSeuilMoyen());
        entity.setSeuilEleve(request.getSeuilEleve());
        entity.setStatut(request.getStatut());

        return toResponse(repository.save(entity));
    }

    // ================= DELETE =================
    @Override
    public void delete(String code) {

        CartographieRisques entity = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Cartographie introuvable : " + code));

        repository.delete(entity);
    }

    // ================= MAPPER =================
    private CartographieRisquesResponse toResponse(
            CartographieRisques entity
    ) {

        int nbRisques = entity.getRisques() != null
                ? entity.getRisques().size()
                : 0;

        return new CartographieRisquesResponse(
                entity.getId(),
                entity.getCode(),
                entity.getTitre(),
                entity.getPeriode(),
                entity.getSeuilFaible(),
                entity.getSeuilMoyen(),
                entity.getSeuilEleve(),
                entity.getStatut(),
                nbRisques
        );
    }
}
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

    // ================= CREATE =================
    @Override
    public CartographieRisquesResponse create(CartographieRisquesRequest request) {

        if (repository.existsById(request.getId())) {
            throw new RuntimeException("Cartographie déjà existante : " + request.getId());
        }

        if (repository.existsByTitre(request.getTitre())) {
            throw new RuntimeException("Titre déjà utilisé : " + request.getTitre());
        }

        CartographieRisques entity = new CartographieRisques()
                .setId(request.getId())
                .setTitre(request.getTitre())
                .setPeriode(request.getPeriode())
                .setSeuilFaible(request.getSeuilFaible())
                .setSeuilMoyen(request.getSeuilMoyen())
                .setSeuilEleve(request.getSeuilEleve())
                .setStatut(request.getStatut());

        return toResponse(repository.save(entity));
    }

    // ================= GET BY ID =================
    @Override
    public CartographieRisquesResponse getById(String id) {

        CartographieRisques entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Introuvable : " + id));

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
    public CartographieRisquesResponse update(String id, CartographieRisquesRequest request) {

        CartographieRisques entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Introuvable : " + id));

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
    public void delete(String id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Introuvable : " + id);
        }

        repository.deleteById(id);
    }

    // ================= MAPPER =================
    private CartographieRisquesResponse toResponse(CartographieRisques entity) {

        int nbRisques = entity.getRisques() != null
                ? entity.getRisques().size()
                : 0;

        return new CartographieRisquesResponse(
                entity.getId(),
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
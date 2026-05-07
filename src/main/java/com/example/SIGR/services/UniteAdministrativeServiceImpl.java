package com.example.SIGR.services;

import com.example.SIGR.dto.request.UniteAdministrativeRequest;
import com.example.SIGR.dto.response.UniteAdministrativeResponse;
import com.example.SIGR.entity.Ministere;
import com.example.SIGR.entity.TypeUnite;
import com.example.SIGR.entity.UniteAdministrative;
import com.example.SIGR.repository.MinistereRepository;
import com.example.SIGR.repository.TypeUniteRepository;
import com.example.SIGR.repository.UniteAdministrativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UniteAdministrativeServiceImpl implements UniteAdministrativeService {

    private final UniteAdministrativeRepository uniteRepository;
    private final MinistereRepository ministereRepository;
    private final TypeUniteRepository typeUniteRepository;

    public UniteAdministrativeServiceImpl(UniteAdministrativeRepository uniteRepository,
                                          MinistereRepository ministereRepository,
                                          TypeUniteRepository typeUniteRepository) {
        this.uniteRepository = uniteRepository;
        this.ministereRepository = ministereRepository;
        this.typeUniteRepository = typeUniteRepository;
    }

    @Override
    public UniteAdministrativeResponse create(UniteAdministrativeRequest request) {

        if (uniteRepository.existsById(request.getId())) {
            throw new RuntimeException("Une unité avec cet ID existe déjà : " + request.getId());
        }

        Ministere ministere = ministereRepository.findById(request.getCodeMinistere())
                .orElseThrow(() -> new RuntimeException("Ministère introuvable : " + request.getCodeMinistere()));

        TypeUnite typeUnite = typeUniteRepository.findById(request.getIdTypeUnite())
                .orElseThrow(() -> new RuntimeException("Type d'unité introuvable : " + request.getIdTypeUnite()));

        UniteAdministrative parent = null;
        if (request.getIdUniteParent() != null && !request.getIdUniteParent().isEmpty()) {
            parent = uniteRepository.findById(request.getIdUniteParent())
                    .orElseThrow(() -> new RuntimeException("Unité parente introuvable : " + request.getIdUniteParent()));
        }

        UniteAdministrative unite = new UniteAdministrative();
        unite.setId(request.getId());
        unite.setLibelle(request.getLibelle());
        unite.setTypeUnite(typeUnite);
        unite.setMinistere(ministere);
        unite.setParent(parent);
        unite.setNiveauHierarchique(request.getNiveauHierarchique());

        UniteAdministrative saved = uniteRepository.save(unite);
        return toResponse(saved);
    }

    @Override
    public UniteAdministrativeResponse getById(String id) {
        UniteAdministrative unite = uniteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unité introuvable, id : " + id));
        return toResponse(unite);
    }

    @Override
    public List<UniteAdministrativeResponse> getAll() {
        return uniteRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UniteAdministrativeResponse update(String id, UniteAdministrativeRequest request) {

        UniteAdministrative unite = uniteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unité introuvable, id : " + id));

        if (request.getLibelle() != null) {
            unite.setLibelle(request.getLibelle());
        }

        if (request.getIdTypeUnite() != null) {
            TypeUnite typeUnite = typeUniteRepository.findById(request.getIdTypeUnite())
                    .orElseThrow(() -> new RuntimeException("Type d'unité introuvable"));
            unite.setTypeUnite(typeUnite);
        }

        if (request.getCodeMinistere() != null) {
            Ministere ministere = ministereRepository.findById(request.getCodeMinistere())
                    .orElseThrow(() -> new RuntimeException("Ministère introuvable"));
            unite.setMinistere(ministere);
        }

        if (request.getIdUniteParent() != null) {
            if (request.getIdUniteParent().isEmpty()) {
                unite.setParent(null);
            } else {
                UniteAdministrative parent = uniteRepository.findById(request.getIdUniteParent())
                        .orElseThrow(() -> new RuntimeException("Unité parente introuvable"));
                unite.setParent(parent);
            }
        }

        if (request.getNiveauHierarchique() != null) {
            unite.setNiveauHierarchique(request.getNiveauHierarchique());
        }

        UniteAdministrative updated = uniteRepository.save(unite);
        return toResponse(updated);
    }

    @Override
    public void delete(String id) {
        if (!uniteRepository.existsById(id)) {
            throw new RuntimeException("Unité introuvable, id : " + id);
        }
        uniteRepository.deleteById(id);
    }

    private UniteAdministrativeResponse toResponse(UniteAdministrative unite) {
        return new UniteAdministrativeResponse(
                unite.getId(),
                unite.getLibelle(),
                unite.getTypeUnite() != null ? unite.getTypeUnite().getId() : null,
                unite.getTypeUnite() != null ? unite.getTypeUnite().getLibelle() : null,
                unite.getMinistere() != null ? unite.getMinistere().getCode() : null,
                unite.getMinistere() != null ? unite.getMinistere().getNom() : null,
                unite.getParent() != null ? unite.getParent().getId() : null,
                unite.getNiveauHierarchique()
        );
    }
}
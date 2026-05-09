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

        if (uniteRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Une unité avec ce code existe déjà : " + request.getCode());
        }

        Ministere ministere = ministereRepository.findById(request.getCodeMinistere())
                .orElseThrow(() -> new RuntimeException("Ministère introuvable : " + request.getCodeMinistere()));

        TypeUnite typeUnite = typeUniteRepository.findById(request.getIdTypeUnite())
                .orElseThrow(() -> new RuntimeException("Type d'unité introuvable : " + request.getIdTypeUnite()));

        UniteAdministrative parent = null;
        if (request.getIdUniteParent() != null && !request.getIdUniteParent().isEmpty()) {
            parent = uniteRepository.findByCode(request.getIdUniteParent())
                    .orElseThrow(() -> new RuntimeException("Unité parente introuvable : " + request.getIdUniteParent()));
        }

        UniteAdministrative unite = new UniteAdministrative();
        unite.setCode(request.getCode());
        unite.setLibelle(request.getLibelle());
        unite.setTypeUnite(typeUnite);
        unite.setMinistere(ministere);
        unite.setParent(parent);
        unite.setNiveauHierarchique(request.getNiveauHierarchique());

        UniteAdministrative saved = uniteRepository.save(unite);
        return toResponse(saved);
    }

    @Override
    public UniteAdministrativeResponse getByCode(String code) {
        UniteAdministrative unite = uniteRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Unité introuvable, code : " + code));
        return toResponse(unite);
    }

    @Override
    public List<UniteAdministrativeResponse> getAll() {
        return uniteRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UniteAdministrativeResponse update(String code, UniteAdministrativeRequest request) {

        UniteAdministrative unite = uniteRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Unité introuvable, code : " + code));

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
                UniteAdministrative parent = uniteRepository.findByCode(request.getIdUniteParent())
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
    public void delete(String code) {
        if (!uniteRepository.existsByCode(code)) {
            throw new RuntimeException("Unité introuvable, code : " + code);
        }
        uniteRepository.delete(uniteRepository.findByCode(code).get());
    }

    private UniteAdministrativeResponse toResponse(UniteAdministrative unite) {
        return new UniteAdministrativeResponse(
                unite.getId(),
                unite.getCode(),
                unite.getLibelle(),
                unite.getTypeUnite() != null ? unite.getTypeUnite().getId() : null,
                unite.getTypeUnite() != null ? unite.getTypeUnite().getLibelle() : null,
                unite.getMinistere() != null ? unite.getMinistere().getCode() : null,
                unite.getMinistere() != null ? unite.getMinistere().getNom() : null,
                unite.getParent() != null ? unite.getParent().getCode() : null,
                unite.getNiveauHierarchique()
        );
    }
}
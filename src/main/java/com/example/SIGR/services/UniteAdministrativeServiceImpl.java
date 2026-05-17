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

@Service
public class UniteAdministrativeServiceImpl implements UniteAdministrativeService {

    private final UniteAdministrativeRepository uniteRepository;
    private final MinistereRepository ministereRepository;
    private final TypeUniteRepository typeUniteRepository;

    public UniteAdministrativeServiceImpl(
            UniteAdministrativeRepository uniteRepository,
            MinistereRepository ministereRepository,
            TypeUniteRepository typeUniteRepository
    ) {
        this.uniteRepository = uniteRepository;
        this.ministereRepository = ministereRepository;
        this.typeUniteRepository = typeUniteRepository;
    }

    // =========================================================
    // CREATE
    // =========================================================
    @Override
    public UniteAdministrativeResponse create(UniteAdministrativeRequest request) {

        if (uniteRepository.existsByCode(request.getCode())) {
            throw new RuntimeException(
                    "Une unité existe déjà avec ce code : " + request.getCode()
            );
        }

        Ministere ministere = ministereRepository.findByCode(request.getCodeMinistere())
                .orElseThrow(() ->
                        new RuntimeException("Ministère introuvable : " + request.getCodeMinistere())
                );

        TypeUnite typeUnite = typeUniteRepository.findByCode(request.getIdTypeUnite())
                .orElseThrow(() ->
                        new RuntimeException("Type unité introuvable : " + request.getIdTypeUnite())
                );

        UniteAdministrative parent = resolveParent(request.getIdUniteParent());

        UniteAdministrative unite = new UniteAdministrative();
        unite.setCode(request.getCode());
        unite.setLibelle(request.getLibelle());
        unite.setTypeUnite(typeUnite);
        unite.setMinistere(ministere);
        unite.setParent(parent);
        unite.setNiveauHierarchique(request.getNiveauHierarchique());

        return toResponse(uniteRepository.save(unite));
    }

    // =========================================================
    // GET BY CODE
    // =========================================================
    @Override
    public UniteAdministrativeResponse getByCode(String code) {

        UniteAdministrative unite = uniteRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Unité introuvable : " + code)
                );

        return toResponse(unite);
    }

    // =========================================================
    // GET ALL
    // =========================================================
    @Override
    public List<UniteAdministrativeResponse> getAll() {
        return uniteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // UPDATE (code uniquement via URL)
    // =========================================================
    @Override
    public UniteAdministrativeResponse update(String code, UniteAdministrativeRequest request) {

        UniteAdministrative unite = uniteRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Unité introuvable : " + code)
                );

        if (request.getLibelle() != null) {
            unite.setLibelle(request.getLibelle());
        }

        // TYPE UNITE (CODE)
        if (request.getIdTypeUnite() != null && !request.getIdTypeUnite().isBlank()) {
            TypeUnite typeUnite = typeUniteRepository.findByCode(request.getIdTypeUnite())
                    .orElseThrow(() ->
                            new RuntimeException("Type unité introuvable : " + request.getIdTypeUnite())
                    );
            unite.setTypeUnite(typeUnite);
        }

        // MINISTERE (CODE)
        if (request.getCodeMinistere() != null && !request.getCodeMinistere().isBlank()) {
            Ministere ministere = ministereRepository.findByCode(request.getCodeMinistere())
                    .orElseThrow(() ->
                            new RuntimeException("Ministère introuvable : " + request.getCodeMinistere())
                    );
            unite.setMinistere(ministere);
        }

        // PARENT
        if (request.getIdUniteParent() != null) {

            if (request.getIdUniteParent().isBlank()) {
                unite.setParent(null);
            } else {
                UniteAdministrative parent = uniteRepository.findByCode(request.getIdUniteParent())
                        .orElseThrow(() ->
                                new RuntimeException("Unité parent introuvable : " + request.getIdUniteParent())
                        );
                unite.setParent(parent);
            }
        }

        if (request.getNiveauHierarchique() != null) {
            unite.setNiveauHierarchique(request.getNiveauHierarchique());
        }

        return toResponse(uniteRepository.save(unite));
    }

    // =========================================================
    // DELETE
    // =========================================================
    @Override
    public void delete(String code) {

        UniteAdministrative unite = uniteRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Unité introuvable : " + code)
                );

        uniteRepository.delete(unite);
    }

    // =========================================================
    // HELPERS
    // =========================================================
    private UniteAdministrative resolveParent(String idUniteParent) {

        if (idUniteParent == null || idUniteParent.isBlank()) {
            return null;
        }

        return uniteRepository.findByCode(idUniteParent)
                .orElseThrow(() ->
                        new RuntimeException("Unité parent introuvable : " + idUniteParent)
                );
    }

    private UniteAdministrativeResponse toResponse(UniteAdministrative unite) {
        return new UniteAdministrativeResponse(
                unite.getId(),
                unite.getCode(),
                unite.getLibelle(),
                unite.getTypeUnite() != null ? unite.getTypeUnite().getCode() : null,
                unite.getTypeUnite() != null ? unite.getTypeUnite().getLibelle() : null,
                unite.getMinistere() != null ? unite.getMinistere().getCode() : null,
                unite.getMinistere() != null ? unite.getMinistere().getNom() : null,
                unite.getParent() != null ? unite.getParent().getCode() : null,
                unite.getNiveauHierarchique()
        );
    }
}
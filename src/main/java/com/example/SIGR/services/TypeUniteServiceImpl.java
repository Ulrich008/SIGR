package com.example.SIGR.services;

import com.example.SIGR.dto.request.TypeUniteRequest;
import com.example.SIGR.dto.response.TypeUniteResponse;
import com.example.SIGR.entity.TypeUnite;
import com.example.SIGR.repository.TypeUniteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TypeUniteServiceImpl implements TypeUniteService {

    private final TypeUniteRepository typeUniteRepository;

    public TypeUniteServiceImpl(TypeUniteRepository typeUniteRepository) {
        this.typeUniteRepository = typeUniteRepository;
    }

    // ================= CREATE =================
    @Override
    public TypeUniteResponse create(TypeUniteRequest request) {

        // Vérification unicité métier
        if (typeUniteRepository.existsByCode(request.getCode())) {
            throw new RuntimeException(
                    "Un type d'unité existe déjà avec le code : " + request.getCode()
            );
        }

        TypeUnite typeUnite = new TypeUnite();

        typeUnite.setCode(request.getCode());
        typeUnite.setLibelle(request.getLibelle());
        typeUnite.setDescription(request.getDescription());
        typeUnite.setCreePar(request.getCreePar());

        TypeUnite saved = typeUniteRepository.save(typeUnite);
        return toResponse(saved);
    }

    // ================= GET BY ID =================
    @Override
    public TypeUniteResponse getById(String id) {

        TypeUnite typeUnite = typeUniteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Type d'unité introuvable (id) : " + id)
                );

        return toResponse(typeUnite);
    }

    // ================= GET BY CODE =================
    @Override
    public TypeUniteResponse getByCode(String code) {

        TypeUnite typeUnite = typeUniteRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Type d'unité introuvable (code) : " + code)
                );

        return toResponse(typeUnite);
    }

    // ================= GET ALL =================
    @Override
    public List<TypeUniteResponse> getAll() {
        return typeUniteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @Override
    public TypeUniteResponse update(String id, TypeUniteRequest request) {

        TypeUnite typeUnite = typeUniteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Type d'unité introuvable (id) : " + id)
                );

        // Vérification code si changé
        if (!typeUnite.getCode().equals(request.getCode())
                && typeUniteRepository.existsByCode(request.getCode())) {
            throw new RuntimeException(
                    "Un type d'unité existe déjà avec le code : " + request.getCode()
            );
        }

        typeUnite.setCode(request.getCode());
        typeUnite.setLibelle(request.getLibelle());
        typeUnite.setDescription(request.getDescription());
        typeUnite.setCreePar(request.getCreePar());

        TypeUnite updated = typeUniteRepository.save(typeUnite);
        return toResponse(updated);
    }

    // ================= DELETE =================
    @Override
    public void delete(String id) {

        if (!typeUniteRepository.existsById(id)) {
            throw new RuntimeException("Type d'unité introuvable (id) : " + id);
        }

        typeUniteRepository.deleteById(id);
    }

    // ================= MAPPER =================
    private TypeUniteResponse toResponse(TypeUnite typeUnite) {
        return new TypeUniteResponse(
                typeUnite.getId(),
                typeUnite.getCode(),
                typeUnite.getLibelle(),
                typeUnite.getDescription(),
                typeUnite.getCreePar()
        );
    }
}
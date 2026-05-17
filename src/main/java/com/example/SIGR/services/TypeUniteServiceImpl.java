package com.example.SIGR.services;

import com.example.SIGR.dto.request.TypeUniteRequest;
import com.example.SIGR.dto.response.TypeUniteResponse;
import com.example.SIGR.entity.TypeUnite;
import com.example.SIGR.repository.TypeUniteRepository;
import com.example.SIGR.security.SecurityUtils;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeUniteServiceImpl implements TypeUniteService {

    private final TypeUniteRepository typeUniteRepository;

    public TypeUniteServiceImpl(
            TypeUniteRepository typeUniteRepository
    ) {
        this.typeUniteRepository = typeUniteRepository;
    }

    // ================= CREATE =================
    @Override
    public TypeUniteResponse create(
            TypeUniteRequest request
    ) {

        // Vérification unicité métier
        if (typeUniteRepository.existsByCode(request.getCode())) {

            throw new RuntimeException(
                    "Un type d'unité existe déjà avec le code : "
                            + request.getCode()
            );
        }

        TypeUnite typeUnite = new TypeUnite();

        typeUnite.setCode(request.getCode());
        typeUnite.setLibelle(request.getLibelle());
        typeUnite.setDescription(request.getDescription());
        typeUnite.setCreePar(SecurityUtils.getCurrentRole());

        TypeUnite saved =
                typeUniteRepository.save(typeUnite);

        return toResponse(saved);
    }

    // ================= GET BY CODE =================
    @Override
    public TypeUniteResponse getByCode(
            String code
    ) {

        TypeUnite typeUnite =
                typeUniteRepository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Type d'unité introuvable (code) : " + code
                                )
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
    public TypeUniteResponse update(
            String code,
            TypeUniteRequest request
    ) {

        TypeUnite typeUnite =
                typeUniteRepository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Type d'unité introuvable (code) : " + code
                                )
                        );

        /**
         * Le code ne doit pas être modifié
         */

        typeUnite.setLibelle(
                request.getLibelle()
        );

        typeUnite.setDescription(
                request.getDescription()
        );

        typeUnite.setCreePar(
                SecurityUtils.getCurrentRole()
        );

        TypeUnite updated =
                typeUniteRepository.save(typeUnite);

        return toResponse(updated);
    }

    // ================= DELETE =================
    @Override
    public void delete(
            String code
    ) {

        TypeUnite typeUnite =
                typeUniteRepository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Type d'unité introuvable (code) : " + code
                                )
                        );

        typeUniteRepository.delete(typeUnite);
    }

    // ================= MAPPER =================
    private TypeUniteResponse toResponse(
            TypeUnite typeUnite
    ) {

        return new TypeUniteResponse(
                typeUnite.getId(),
                typeUnite.getCode(),
                typeUnite.getLibelle(),
                typeUnite.getDescription(),
                typeUnite.getCreePar()
        );
    }
}
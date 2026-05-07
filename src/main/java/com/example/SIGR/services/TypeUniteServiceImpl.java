package com.example.SIGR.services;

import com.example.SIGR.dto.request.TypeUniteRequest;
import com.example.SIGR.dto.response.TypeUniteResponse;
import com.example.SIGR.entity.TypeUnite;
import com.example.SIGR.repository.TypeUniteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeUniteServiceImpl implements TypeUniteService {

    private final TypeUniteRepository typeUniteRepository;

    public TypeUniteServiceImpl(TypeUniteRepository typeUniteRepository) {
        this.typeUniteRepository = typeUniteRepository;
    }

    @Override
    public TypeUniteResponse create(TypeUniteRequest request) {

        if (typeUniteRepository.existsById(request.getId())) {
            throw new RuntimeException("Un type d'unité avec cet ID existe déjà : " + request.getId());
        }

        TypeUnite typeUnite = new TypeUnite();
        typeUnite.setId(request.getId());
        typeUnite.setLibelle(request.getLibelle());
        typeUnite.setDescription(request.getDescription());
        typeUnite.setCreePar(request.getCreePar());

        TypeUnite saved = typeUniteRepository.save(typeUnite);
        return toResponse(saved);
    }

    @Override
    public TypeUniteResponse getById(String id) {
        TypeUnite typeUnite = typeUniteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type d'unité introuvable, id : " + id));
        return toResponse(typeUnite);
    }

    @Override
    public List<TypeUniteResponse> getAll() {
        return typeUniteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TypeUniteResponse update(String id, TypeUniteRequest request) {

        TypeUnite typeUnite = typeUniteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type d'unité introuvable, id : " + id));

        typeUnite.setLibelle(request.getLibelle());
        typeUnite.setDescription(request.getDescription());
        typeUnite.setCreePar(request.getCreePar());

        TypeUnite updated = typeUniteRepository.save(typeUnite);
        return toResponse(updated);
    }

    @Override
    public void delete(String id) {
        if (!typeUniteRepository.existsById(id)) {
            throw new RuntimeException("Type d'unité introuvable, id : " + id);
        }
        typeUniteRepository.deleteById(id);
    }

    private TypeUniteResponse toResponse(TypeUnite typeUnite) {
        return new TypeUniteResponse(
                typeUnite.getId(),
                typeUnite.getLibelle(),
                typeUnite.getDescription(),
                typeUnite.getCreePar()
        );
    }
}
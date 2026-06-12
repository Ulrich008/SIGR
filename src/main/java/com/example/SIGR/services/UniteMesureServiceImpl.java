package com.example.SIGR.services;

import com.example.SIGR.dto.request.UniteMesureRequest;
import com.example.SIGR.dto.response.UniteMesureResponse;
import com.example.SIGR.entity.UniteMesure;
import com.example.SIGR.repository.UniteMesureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UniteMesureServiceImpl implements UniteMesureService {

    private final UniteMesureRepository repository;

    public UniteMesureServiceImpl(UniteMesureRepository repository) {
        this.repository = repository;
    }

    @Override
    public UniteMesureResponse create(UniteMesureRequest request) {

        if (request.getCode() == null || request.getCode().isBlank()) {
            throw new RuntimeException("Le code est obligatoire");
        }

        if (repository.existsByCode(request.getCode())) {
            throw new RuntimeException("Une unité de mesure avec ce code existe déjà : " + request.getCode());
        }

        UniteMesure entity = new UniteMesure();
        entity.setCode(request.getCode());
        entity.setLibelle(request.getLibelle());
        entity.setSymbole(request.getSymbole());
        entity.setDescription(request.getDescription());

        UniteMesure saved = repository.save(entity);
        return toResponse(saved);
    }

    @Override
    public UniteMesureResponse getByCode(String code) {
        UniteMesure entity = repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Unité de mesure introuvable : " + code));
        return toResponse(entity);
    }

    @Override
    public List<UniteMesureResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UniteMesureResponse update(String code, UniteMesureRequest request) {
        UniteMesure entity = repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Unité de mesure introuvable : " + code));

        if (request.getLibelle() != null && !request.getLibelle().isBlank()) {
            entity.setLibelle(request.getLibelle());
        }

        if (request.getSymbole() != null && !request.getSymbole().isBlank()) {
            entity.setSymbole(request.getSymbole());
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            entity.setDescription(request.getDescription());
        }

        UniteMesure updated = repository.save(entity);
        return toResponse(updated);
    }

    @Override
    public void delete(String code) {
        UniteMesure entity = repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Unité de mesure introuvable : " + code));
        repository.delete(entity);
    }

    private UniteMesureResponse toResponse(UniteMesure entity) {
        return new UniteMesureResponse(
                entity.getId(),
                entity.getCode(),
                entity.getLibelle(),
                entity.getSymbole(),
                entity.getDescription(),
                entity.getTypeUnite() != null ? entity.getTypeUnite().name() : null
        );
    }
}

package com.example.SIGR.services;

import com.example.SIGR.dto.request.MinistereRequest;
import com.example.SIGR.dto.response.MinistereResponse;
import com.example.SIGR.entity.Ministere;
import com.example.SIGR.repository.MinistereRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MinistereServiceImpl implements MinistereService {

    private final MinistereRepository ministereRepository;

    public MinistereServiceImpl(MinistereRepository ministereRepository) {
        this.ministereRepository = ministereRepository;
    }

    // ================= CREATE =================
    @Override
    public MinistereResponse create(MinistereRequest request) {

        if (ministereRepository.existsByCode(request.getCode())){
            throw new RuntimeException("Le code de ce ministère existe déjà " + request.getCode());
        }

        if (ministereRepository.existsByNom(request.getNom())) {
            throw new RuntimeException("Un ministère avec ce nom existe déjà : " + request.getNom());
        }

        Ministere ministere = new Ministere();
        ministere.setCode(request.getCode());
        ministere.setNom(request.getNom());
        ministere.setSigle(request.getSigle());
        ministere.setDescription(request.getDescription());
        ministere.setCreePar(request.getCreePar());

        Ministere saved = ministereRepository.save(ministere);

        return toResponse(saved);
    }

    // ================= GET BY ID =================
    @Override
    public MinistereResponse getById(String code) {

        Ministere ministere = ministereRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Ministère introuvable, code : " + code));

        return toResponse(ministere);
    }

    // ================= GET ALL =================
    @Override
    public List<MinistereResponse> getAll() {
        return ministereRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @Override
    public MinistereResponse update(String code, MinistereRequest request) {

        Ministere ministere = ministereRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Ministère introuvable, code : " + code));

        // 🔥 Vérification doublon nom (corrigée)
        if (ministereRepository.existsByNom(request.getNom())
                && !ministere.getNom().equals(request.getNom())) {
            throw new RuntimeException("Un ministère avec ce nom existe déjà : " + request.getNom());
        }

        ministere.setNom(request.getNom());
        ministere.setSigle(request.getSigle());
        ministere.setDescription(request.getDescription());
        ministere.setCreePar(request.getCreePar());

        Ministere updated = ministereRepository.save(ministere);

        return toResponse(updated);
    }

    // ================= DELETE =================
    @Override
    public void delete(String code) {

        if (!ministereRepository.existsById(code)) {
            throw new RuntimeException("Ministère introuvable, code : " + code);
        }

        ministereRepository.deleteById(code);
    }

    // ================= MAPPER =================
    private MinistereResponse toResponse(Ministere ministere) {

        return new MinistereResponse(
                ministere.getCode(),
                ministere.getNom(),
                ministere.getSigle(),
                ministere.getDescription(),
                ministere.getCreePar()
        );
    }
}
package com.example.SIGR.services;

import com.example.SIGR.dto.request.MinistereRequest;
import com.example.SIGR.dto.response.MinistereResponse;
import com.example.SIGR.entity.Ministere;
import com.example.SIGR.repository.MinistereRepository;
import org.springframework.stereotype.Service;
import com.example.SIGR.security.SecurityUtils;

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

        if (ministereRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Code déjà utilisé : " + request.getCode());
        }

        if (ministereRepository.existsByNom(request.getNom())) {
            throw new RuntimeException("Nom déjà utilisé : " + request.getNom());
        }

        Ministere ministere = new Ministere();
        ministere.setCode(request.getCode());
        ministere.setNom(request.getNom());
        ministere.setSigle(request.getSigle());
        ministere.setDescription(request.getDescription());
        ministere.setCreePar(SecurityUtils.getCurrentRole());

        Ministere saved = ministereRepository.save(ministere);

        return toResponse(saved);
    }

    // ================= GET BY ID =================
    @Override
    public MinistereResponse getById(String id) {

        Ministere ministere = ministereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ministère introuvable, id : " + id));

        return toResponse(ministere);
    }
    public MinistereResponse getByCode(String code) {
        Ministere ministere = ministereRepository.findByCode(code)
                .orElseThrow(()-> new RuntimeException("Ministere introuvable, id :" + code));

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
    public MinistereResponse update(String id, MinistereRequest request) {

        Ministere ministere = ministereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ministère introuvable, id : " + id));

        if (ministereRepository.existsByNom(request.getNom())
                && !ministere.getNom().equals(request.getNom())) {
            throw new RuntimeException("Nom déjà utilisé : " + request.getNom());
        }

        ministere.setNom(request.getNom());
        ministere.setSigle(request.getSigle());
        ministere.setDescription(request.getDescription());
        ministere.setCreePar(SecurityUtils.getCurrentRole());

        Ministere updated = ministereRepository.save(ministere);

        return toResponse(updated);
    }

    // ================= DELETE =================
    @Override
    public void delete(String id) {

        if (!ministereRepository.existsById(id)) {
            throw new RuntimeException("Ministère introuvable, id : " + id);
        }

        ministereRepository.deleteById(id);
    }

    // ================= MAPPER =================
    private MinistereResponse toResponse(Ministere ministere) {

        return new MinistereResponse(
                ministere.getId(),
                ministere.getCode(),
                ministere.getNom(),
                ministere.getSigle(),
                ministere.getDescription(),
                ministere.getCreePar()
        );
    }

    // ================= GET CODE MINISTERE OF CURRENT USER =================
    @Override
    public String getCodeMinistereOfCurrentUser() {
        return SecurityUtils.getCurrentMinistereCode();
    }
}
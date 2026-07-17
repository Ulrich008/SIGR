package com.example.SIGR.services;

import com.example.SIGR.dto.request.ProfilRequest;
import com.example.SIGR.dto.response.ProfilResponse;

import com.example.SIGR.entity.Profil;

import com.example.SIGR.repository.ProfilRepository;

import com.example.SIGR.services.ProfilService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class    ProfilServiceImpl implements ProfilService {

    private final ProfilRepository profilRepository;

    public ProfilServiceImpl(
            ProfilRepository profilRepository
    ) {
        this.profilRepository = profilRepository;
    }

    /**
     * ================= CREATE =================
     */
    @Override
    public ProfilResponse create(
            ProfilRequest request
    ) {

        if (profilRepository.existsByCode(request.getCode())) {
            throw new RuntimeException(
                    "Un profil avec ce code existe déjà"
            );
        }

        if (profilRepository.existsByLibelleIgnoreCase(request.getLibelle())) {
            throw new RuntimeException(
                    "Un profil avec ce libellé existe déjà : " + request.getLibelle()
            );
        }

        Profil profil = new Profil();

        profil.setCode(request.getCode());
        profil.setLibelle(request.getLibelle());
        profil.setDescription(request.getDescription());

        profil = profilRepository.save(profil);

        return mapToResponse(profil);
    }

    /**
     * ================= GET ALL =================
     */
    @Override
    public List<ProfilResponse> getAll() {

        return profilRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * ================= GET BY CODE =================
     */
    @Override
    public ProfilResponse getByCode(
            String code
    ) {

        Profil profil = profilRepository
                .findByCode(code)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Profil introuvable avec le code : " + code
                        )
                );

        return mapToResponse(profil);
    }

    /**
     * ================= UPDATE =================
     */
    @Override
    public ProfilResponse update(
            String code,
            ProfilRequest request
    ) {

        Profil profil = profilRepository
                .findByCode(code)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Profil introuvable avec le code : " + code
                        )
                );

        if (profilRepository.existsByLibelleIgnoreCaseAndCodeNot(request.getLibelle(), code)) {
            throw new RuntimeException(
                    "Un profil avec ce libellé existe déjà : " + request.getLibelle()
            );
        }

        profil.setLibelle(request.getLibelle());
        profil.setDescription(request.getDescription());

        profil = profilRepository.save(profil);

        return mapToResponse(profil);
    }

    /**
     * ================= DELETE =================
     */
    @Override
    public void delete(
            String code
    ) {

        Profil profil = profilRepository
                .findByCode(code)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Profil introuvable avec le code : " + code
                        )
                );

        profilRepository.delete(profil);
    }

    /**
     * ================= MAPPING =================
     */
    private ProfilResponse mapToResponse(
            Profil profil
    ) {

        return new ProfilResponse()
                .setId(profil.getId())
                .setCode(profil.getCode())
                .setLibelle(profil.getLibelle())
                .setDescription(profil.getDescription());
    }
}
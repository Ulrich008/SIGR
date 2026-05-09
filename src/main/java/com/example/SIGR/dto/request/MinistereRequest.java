package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MinistereRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 200, message = "Le nom ne doit pas dépasser 200 caractères")
    private String nom;

    @Size(max = 20, message = "Le sigle ne doit pas dépasser 20 caractères")
    private String sigle;

    @Size(max = 1000, message = "La description est trop longue")
    private String description;

    @Size(max = 100)
    private String creePar;

    // ================= GETTERS / SETTERS =================

    public String getCode() {
        return code;
    }

    public MinistereRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public MinistereRequest setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getSigle() {
        return sigle;
    }

    public MinistereRequest setSigle(String sigle) {
        this.sigle = sigle;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MinistereRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCreePar() {
        return creePar;
    }

    public MinistereRequest setCreePar(String creePar) {
        this.creePar = creePar;
        return this;
    }
}
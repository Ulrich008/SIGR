package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;

public class MinistereRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String nom;

    private String sigle;

    private String description;

    private String creePar;

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
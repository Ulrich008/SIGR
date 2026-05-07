package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;

public class TypeUniteRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String libelle;

    private String description;

    private String creePar;

    public String getId() {
        return id;
    }

    public TypeUniteRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public TypeUniteRequest setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TypeUniteRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCreePar() {
        return creePar;
    }

    public TypeUniteRequest setCreePar(String creePar) {
        this.creePar = creePar;
        return this;
    }
}
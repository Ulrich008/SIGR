package com.example.SIGR.dto.request;

import jakarta.validation.constraints.*;

public class TypeUniteRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 200, message = "Le libellé ne doit pas dépasser 200 caractères")
    private String libelle;

    @Size(max = 1000, message = "La description est trop longue")
    private String description;

    // ================= GETTERS / SETTERS =================

    public String getCode() {
        return code;
    }

    public TypeUniteRequest setCode(String code) {
        this.code = code;
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
}
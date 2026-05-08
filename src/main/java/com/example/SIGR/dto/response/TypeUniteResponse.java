package com.example.SIGR.dto.response;

public class TypeUniteResponse {

    private String id;
    private String libelle;
    private String description;
    private String creePar;

    public TypeUniteResponse(String id, String libelle, String description, String creePar) {
        this.id = id;
        this.libelle = libelle;
        this.description = description;
        this.creePar = creePar;
    }

    public String getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDescription() {
        return description;
    }

    public String getCreePar() {
        return creePar;
    }
}
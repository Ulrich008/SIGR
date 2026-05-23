package com.example.SIGR.dto.response;

public class ProfilResponse {

    private String id;

    private String code;

    private String libelle;

    private String description;

    // ================= GETTERS & SETTERS =================

    public String getId() {
        return id;
    }

    public ProfilResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ProfilResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public ProfilResponse setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProfilResponse setDescription(String description) {
        this.description = description;
        return this;
    }
}
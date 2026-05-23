package com.example.SIGR.dto.response;

public class MinistereResponse {

    private String id;
    private String code;
    private String nom;
    private String sigle;
    private String description;
    private String creePar;

    public MinistereResponse(String id, String code, String nom, String sigle, String description, String creePar) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.sigle = sigle;
        this.description = description;
        this.creePar = creePar;
    }

    // ================= GETTERS =================

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

    public String getSigle() {
        return sigle;
    }

    public String getDescription() {
        return description;
    }

    public String getCreePar() {
        return creePar;
    }
}
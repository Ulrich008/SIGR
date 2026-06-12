package com.example.SIGR.dto.response;

public class UniteMesureResponse {

    private String id;
    private String code;
    private String libelle;
    private String symbole;
    private String description;
    private String typeUnite;

    // ================= CONSTRUCTEUR =================

    public UniteMesureResponse() {
    }

    public UniteMesureResponse(String id, String code, String libelle, String symbole, String description, String typeUnite) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.symbole = symbole;
        this.description = description;
        this.typeUnite = typeUnite;
    }

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public UniteMesureResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public UniteMesureResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public UniteMesureResponse setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getSymbole() {
        return symbole;
    }

    public UniteMesureResponse setSymbole(String symbole) {
        this.symbole = symbole;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UniteMesureResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTypeUnite() {
        return typeUnite;
    }

    public UniteMesureResponse setTypeUnite(String typeUnite) {
        this.typeUnite = typeUnite;
        return this;
    }
}

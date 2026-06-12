package com.example.SIGR.dto.request;

public class UniteMesureRequest {

    private String code;
    private String libelle;
    private String symbole;
    private String description;

    // ================= CONSTRUCTEUR =================

    public UniteMesureRequest() {
    }

    public UniteMesureRequest(String code, String libelle, String symbole, String description) {
        this.code = code;
        this.libelle = libelle;
        this.symbole = symbole;
        this.description = description;
    }

    // ================= GETTERS / SETTERS =================

    public String getCode() {
        return code;
    }

    public UniteMesureRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public UniteMesureRequest setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getSymbole() {
        return symbole;
    }

    public UniteMesureRequest setSymbole(String symbole) {
        this.symbole = symbole;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UniteMesureRequest setDescription(String description) {
        this.description = description;
        return this;
    }
}

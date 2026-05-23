package com.example.SIGR.dto.request;

import jakarta.validation.constraints.*;

public class UniteAdministrativeRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 200, message = "Le libellé ne doit pas dépasser 200 caractères")
    private String libelle;

    @NotBlank(message = "L'identifiant du type d'unité est obligatoire")
    private String idTypeUnite;

    @NotBlank(message = "Le code du ministère est obligatoire")
    private String codeMinistere;

    private String idUniteParent;

    @NotNull(message = "Le niveau hiérarchique est obligatoire")
    @Min(value = 1, message = "Le niveau hiérarchique doit être >= 1")
    @Max(value = 10, message = "Le niveau hiérarchique doit être <= 10")
    private Integer niveauHierarchique;

    // ===================== GETTERS / SETTERS =====================

    public String getCode() {
        return code;
    }

    public UniteAdministrativeRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public UniteAdministrativeRequest setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getIdTypeUnite() {
        return idTypeUnite;
    }

    public UniteAdministrativeRequest setIdTypeUnite(String idTypeUnite) {
        this.idTypeUnite = idTypeUnite;
        return this;
    }

    public String getCodeMinistere() {
        return codeMinistere;
    }

    public UniteAdministrativeRequest setCodeMinistere(String codeMinistere) {
        this.codeMinistere = codeMinistere;
        return this;
    }

    public String getIdUniteParent() {
        return idUniteParent;
    }

    public UniteAdministrativeRequest setIdUniteParent(String idUniteParent) {
        this.idUniteParent = idUniteParent;
        return this;
    }

    public Integer getNiveauHierarchique() {
        return niveauHierarchique;
    }

    public UniteAdministrativeRequest setNiveauHierarchique(Integer niveauHierarchique) {
        this.niveauHierarchique = niveauHierarchique;
        return this;
    }
}
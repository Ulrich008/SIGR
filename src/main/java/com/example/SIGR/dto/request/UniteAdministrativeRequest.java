package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UniteAdministrativeRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String libelle;

    @NotBlank
    private String idTypeUnite;

    @NotBlank
    private String codeMinistere;

    private String idUniteParent;

    private Integer niveauHierarchique;

    // Getters et setters
    public String getId() {
        return id;
    }

    public UniteAdministrativeRequest setId(String id) {
        this.id = id;
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
package com.example.SIGR.dto.response;

public class UniteAdministrativeResponse {

    private String id;
    private String code;
    private String libelle;

    private String typeUniteId;
    private String typeUniteLibelle;

    private String codeMinistere;
    private String nomMinistere;

    private String idUniteParent;

    private Integer niveauHierarchique;

    public UniteAdministrativeResponse(String id,
                                       String code,
                                       String libelle,
                                       String typeUniteId,
                                       String typeUniteLibelle,
                                       String codeMinistere,
                                       String nomMinistere,
                                       String idUniteParent,
                                       Integer niveauHierarchique) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.typeUniteId = typeUniteId;
        this.typeUniteLibelle = typeUniteLibelle;
        this.codeMinistere = codeMinistere;
        this.nomMinistere = nomMinistere;
        this.idUniteParent = idUniteParent;
        this.niveauHierarchique = niveauHierarchique;
    }

    // ===================== GETTERS =====================

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getTypeUniteId() {
        return typeUniteId;
    }

    public String getTypeUniteLibelle() {
        return typeUniteLibelle;
    }

    public String getCodeMinistere() {
        return codeMinistere;
    }

    public String getNomMinistere() {
        return nomMinistere;
    }

    public String getIdUniteParent() {
        return idUniteParent;
    }

    public Integer getNiveauHierarchique() {
        return niveauHierarchique;
    }
}
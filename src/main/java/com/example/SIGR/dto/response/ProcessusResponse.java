package com.example.SIGR.dto.response;

import com.example.SIGR.entity.TypeProcessus;

public class ProcessusResponse {

    private String id;
    private String code;

    private String libelle;
    private String finalite;

    private TypeProcessus typeProcessus;

    private String idUnite;
    private String nomUnite;

    private String idProprietaire;
    private String nomProprietaire;

    public ProcessusResponse(
            String id,
            String code,
            String libelle,
            String finalite,
            TypeProcessus typeProcessus,
            String idUnite,
            String nomUnite,
            String idProprietaire,
            String nomProprietaire
    ) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.finalite = finalite;
        this.typeProcessus = typeProcessus;
        this.idUnite = idUnite;
        this.nomUnite = nomUnite;
        this.idProprietaire = idProprietaire;
        this.nomProprietaire = nomProprietaire;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getFinalite() {
        return finalite;
    }

    public TypeProcessus getTypeProcessus() {
        return typeProcessus;
    }

    public String getIdUnite() {
        return idUnite;
    }

    public String getNomUnite() {
        return nomUnite;
    }

    public String getIdProprietaire() {
        return idProprietaire;
    }

    public String getNomProprietaire() {
        return nomProprietaire;
    }
}
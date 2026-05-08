package com.example.SIGR.dto.response;

import com.example.SIGR.entity.TypeRisque;

import java.time.LocalDate;

public class RisqueResponse {

    private String id;
    private String libelle;
    private String categorie;
    private String causeProbable;
    private String consequenceProbable;
    private String statut;
    private LocalDate dateIdentification;

    private String codeProcessus;
    private String nomProcessus;

    private String idCartographie;

    private TypeRisque typeRisque;

    public RisqueResponse(
            String id,
            String libelle,
            String categorie,
            String causeProbable,
            String consequenceProbable,
            String statut,
            LocalDate dateIdentification,
            String codeProcessus,
            String nomProcessus,
            String idCartographie,
            TypeRisque typeRisque
    ) {
        this.id = id;
        this.libelle = libelle;
        this.categorie = categorie;
        this.causeProbable = causeProbable;
        this.consequenceProbable = consequenceProbable;
        this.statut = statut;
        this.dateIdentification = dateIdentification;
        this.codeProcessus = codeProcessus;
        this.nomProcessus = nomProcessus;
        this.idCartographie = idCartographie;
        this.typeRisque = typeRisque;
    }

    public String getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getCauseProbable() {
        return causeProbable;
    }

    public String getConsequenceProbable() {
        return consequenceProbable;
    }

    public String getStatut() {
        return statut;
    }

    public LocalDate getDateIdentification() {
        return dateIdentification;
    }

    public String getCodeProcessus() {
        return codeProcessus;
    }

    public String getNomProcessus() {
        return nomProcessus;
    }

    public String getIdCartographie() {
        return idCartographie;
    }

    public TypeRisque getTypeRisque() {
        return typeRisque;
    }
}
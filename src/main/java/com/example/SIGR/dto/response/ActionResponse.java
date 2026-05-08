package com.example.SIGR.dto.response;

import java.time.LocalDate;

public class ActionResponse {

    private String id;
    private String libelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statut;

    private String idPlan;

    private String matriculeResponsable;
    private String nomResponsable;

    public ActionResponse(
            String id,
            String libelle,
            LocalDate dateDebut,
            LocalDate dateFin,
            String statut,
            String idPlan,
            String matriculeResponsable,
            String nomResponsable
    ) {
        this.id = id;
        this.libelle = libelle;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.idPlan = idPlan;
        this.matriculeResponsable = matriculeResponsable;
        this.nomResponsable = nomResponsable;
    }

    public String getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public String getStatut() {
        return statut;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public String getMatriculeResponsable() {
        return matriculeResponsable;
    }

    public String getNomResponsable() {
        return nomResponsable;
    }
}
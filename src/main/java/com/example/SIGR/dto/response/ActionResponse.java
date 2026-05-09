package com.example.SIGR.dto.response;

import com.example.SIGR.entity.StatutAction;

import java.time.LocalDate;

public class ActionResponse {

    private String id;
    private String code;
    private String libelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    /**
     * Statut typé (ENUM)
     */
    private StatutAction statut;

    private String idPlan;

    private String matriculeResponsable;
    private String nomResponsable;

    public ActionResponse(
            String id,
            String code,
            String libelle,
            LocalDate dateDebut,
            LocalDate dateFin,
            StatutAction statut,
            String idPlan,
            String matriculeResponsable,
            String nomResponsable
    ) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.idPlan = idPlan;
        this.matriculeResponsable = matriculeResponsable;
        this.nomResponsable = nomResponsable;
    }

    // ================= GETTERS =================

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
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

    public StatutAction getStatut() {
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
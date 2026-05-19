package com.example.SIGR.dto.response;

import com.example.SIGR.entity.StatutAction;

import java.time.LocalDate;

public class ActionResponse {

    private String code;
    private String libelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    private StatutAction statut;

    /**
     * Références métier uniquement
     */
    private String codePlan;

    private String matriculeResponsable;
    private String nomResponsable;

    public ActionResponse(
            String code,
            String libelle,
            LocalDate dateDebut,
            LocalDate dateFin,
            StatutAction statut,
            String codePlan,
            String matriculeResponsable,
            String nomResponsable
    ) {
        this.code = code;
        this.libelle = libelle;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.codePlan = codePlan;
        this.matriculeResponsable = matriculeResponsable;
        this.nomResponsable = nomResponsable;
    }

    // ================= GETTERS =================

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

    public String getCodePlan() {
        return codePlan;
    }

    public String getMatriculeResponsable() {
        return matriculeResponsable;
    }

    public String getNomResponsable() {
        return nomResponsable;
    }
}
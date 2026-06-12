package com.example.SIGR.dto.response;

import com.example.SIGR.entity.StatutAction;

import java.time.LocalDate;
import java.util.List;

public class ActionResponse {

    private String code;
    private List<String> libelles;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    private StatutAction statut;

    /**
     * Références métier uniquement
     */
    private String codePlan;
    private String codeRisque;
    private String bonnePratique;

    private String matriculeResponsable;
    private String nomResponsable;

    public ActionResponse(
            String code,
            List<String> libelles,
            LocalDate dateDebut,
            LocalDate dateFin,
            StatutAction statut,
            String codePlan,
            String codeRisque,
            String bonnePratique,
            String matriculeResponsable,
            String nomResponsable
    ) {
        this.code = code;
        this.libelles = libelles;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.codePlan = codePlan;
        this.codeRisque = codeRisque;
        this.bonnePratique = bonnePratique;
        this.matriculeResponsable = matriculeResponsable;
        this.nomResponsable = nomResponsable;
    }

    // ================= GETTERS =================

    public String getCode() {
        return code;
    }

    public List<String> getLibelles() {
        return libelles;
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

    public String getCodeRisque() {
        return codeRisque;
    }

    public String getBonnePratique() {
        return bonnePratique;
    }

    public String getMatriculeResponsable() {
        return matriculeResponsable;
    }

    public String getNomResponsable() {
        return nomResponsable;
    }
}
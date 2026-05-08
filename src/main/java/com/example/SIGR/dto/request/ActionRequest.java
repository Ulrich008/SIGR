package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ActionRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String libelle;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    @NotBlank
    private String statut;

    /**
     * Plan de mitigation lié
     */
    @NotBlank
    private String idPlan;

    /**
     * Responsable de l'action
     */
    @NotBlank
    private String matriculeResponsable;

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public ActionRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public ActionRequest setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public ActionRequest setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public ActionRequest setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public String getStatut() {
        return statut;
    }

    public ActionRequest setStatut(String statut) {
        this.statut = statut;
        return this;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public ActionRequest setIdPlan(String idPlan) {
        this.idPlan = idPlan;
        return this;
    }

    public String getMatriculeResponsable() {
        return matriculeResponsable;
    }

    public ActionRequest setMatriculeResponsable(String matriculeResponsable) {
        this.matriculeResponsable = matriculeResponsable;
        return this;
    }
}
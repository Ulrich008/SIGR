package com.example.SIGR.dto.request;

import com.example.SIGR.entity.StatutAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ActionRequest {

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 200, message = "Le libellé ne doit pas dépasser 200 caractères")
    private String libelle;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotNull(message = "Le statut est obligatoire")
    private StatutAction statut;


    @NotBlank(message = "Le code du plan est obligatoire")
    private String codePlan;

    @NotBlank(message = "Le matricule du responsable est obligatoire")
    private String matriculeResponsable;



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

    public StatutAction getStatut() {
        return statut;
    }

    public ActionRequest setStatut(StatutAction statut) {
        this.statut = statut;
        return this;
    }

    public String getCodePlan() {
        return codePlan;
    }

    public ActionRequest setCodePlan(String codePlan) {
        this.codePlan = codePlan;
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
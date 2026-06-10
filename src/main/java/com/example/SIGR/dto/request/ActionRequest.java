package com.example.SIGR.dto.request;

import com.example.SIGR.entity.StatutAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class ActionRequest {

    @NotEmpty(message = "Au moins un libellé est obligatoire")
    @Size(min = 1, message = "Au moins un libellé est obligatoire")
    private List<String> libelles;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotNull(message = "Le statut est obligatoire")
    private StatutAction statut;


    @NotBlank(message = "Le code du plan est obligatoire")
    private String codePlan;

    @NotBlank(message = "Le code du risque est obligatoire")
    private String codeRisque;

    @NotBlank(message = "La bonne pratique est obligatoire")
    private String bonnePratique;

    @NotBlank(message = "Le matricule du responsable est obligatoire")
    private String matriculeResponsable;



    public List<String> getLibelles() {
        return libelles;
    }

    public ActionRequest setLibelles(List<String> libelles) {
        this.libelles = libelles;
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

    public String getCodeRisque() {
        return codeRisque;
    }

    public ActionRequest setCodeRisque(String codeRisque) {
        this.codeRisque = codeRisque;
        return this;
    }

    public String getBonnePratique() {
        return bonnePratique;
    }

    public ActionRequest setBonnePratique(String bonnePratique) {
        this.bonnePratique = bonnePratique;
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
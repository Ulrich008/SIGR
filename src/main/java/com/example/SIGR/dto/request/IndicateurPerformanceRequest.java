package com.example.SIGR.dto.request;

import com.example.SIGR.entity.Frequence;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class IndicateurPerformanceRequest {

    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 200, message = "Le libellé ne doit pas dépasser 200 caractères")
    private String libelle;

    @NotNull(message = "La fréquence est obligatoire")
    private Frequence frequence;

    private String valeurCible; // Peut être un nombre ou une date (string)

    private String valeurObtenue; // Peut être un nombre ou une date (string)

    private LocalDate seuilAlerte;

    private String codeUniteMesure;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @NotBlank(message = "Le code du processus est obligatoire")
    private String codeProcessus;

    private String codeRisque;

    private String codePlanMitigation;

    private String codeAction;

    // ================= GETTERS / SETTERS =================

    public String getCode() {
        return code;
    }

    public IndicateurPerformanceRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public IndicateurPerformanceRequest setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public Frequence getFrequence() {
        return frequence;
    }

    public IndicateurPerformanceRequest setFrequence(Frequence frequence) {
        this.frequence = frequence;
        return this;
    }

    public String getValeurCible() {
        return valeurCible;
    }

    public IndicateurPerformanceRequest setValeurCible(String valeurCible) {
        this.valeurCible = valeurCible;
        return this;
    }

    public String getValeurObtenue() {
        return valeurObtenue;
    }

    public IndicateurPerformanceRequest setValeurObtenue(String valeurObtenue) {
        this.valeurObtenue = valeurObtenue;
        return this;
    }

    public LocalDate getSeuilAlerte() {
        return seuilAlerte;
    }

    public IndicateurPerformanceRequest setSeuilAlerte(LocalDate seuilAlerte) {
        this.seuilAlerte = seuilAlerte;
        return this;
    }

    public String getCodeUniteMesure() {
        return codeUniteMesure;
    }

    public IndicateurPerformanceRequest setCodeUniteMesure(String codeUniteMesure) {
        this.codeUniteMesure = codeUniteMesure;
        return this;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public IndicateurPerformanceRequest setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public IndicateurPerformanceRequest setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public String getCodeProcessus() {
        return codeProcessus;
    }

    public IndicateurPerformanceRequest setCodeProcessus(String codeProcessus) {
        this.codeProcessus = codeProcessus;
        return this;
    }

    public String getCodeRisque() {
        return codeRisque;
    }

    public IndicateurPerformanceRequest setCodeRisque(String codeRisque) {
        this.codeRisque = codeRisque;
        return this;
    }

    public String getCodePlanMitigation() {
        return codePlanMitigation;
    }

    public IndicateurPerformanceRequest setCodePlanMitigation(String codePlanMitigation) {
        this.codePlanMitigation = codePlanMitigation;
        return this;
    }

    public String getCodeAction() {
        return codeAction;
    }

    public IndicateurPerformanceRequest setCodeAction(String codeAction) {
        this.codeAction = codeAction;
        return this;
    }
}
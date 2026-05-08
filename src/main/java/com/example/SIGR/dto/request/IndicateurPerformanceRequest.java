package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class IndicateurPerformanceRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String libelle;

    private String uniteMesure;

    private String frequence;

    private Double valeurCible;

    private Double valeurObtenue;

    private Double seuilAlerte;

    @NotNull
    private LocalDate dateMesure;

    @NotBlank
    private String codeProcessus;

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

    public String getUniteMesure() {
        return uniteMesure;
    }

    public IndicateurPerformanceRequest setUniteMesure(String uniteMesure) {
        this.uniteMesure = uniteMesure;
        return this;
    }

    public String getFrequence() {
        return frequence;
    }

    public IndicateurPerformanceRequest setFrequence(String frequence) {
        this.frequence = frequence;
        return this;
    }

    public Double getValeurCible() {
        return valeurCible;
    }

    public IndicateurPerformanceRequest setValeurCible(Double valeurCible) {
        this.valeurCible = valeurCible;
        return this;
    }

    public Double getValeurObtenue() {
        return valeurObtenue;
    }

    public IndicateurPerformanceRequest setValeurObtenue(Double valeurObtenue) {
        this.valeurObtenue = valeurObtenue;
        return this;
    }

    public Double getSeuilAlerte() {
        return seuilAlerte;
    }

    public IndicateurPerformanceRequest setSeuilAlerte(Double seuilAlerte) {
        this.seuilAlerte = seuilAlerte;
        return this;
    }

    public LocalDate getDateMesure() {
        return dateMesure;
    }

    public IndicateurPerformanceRequest setDateMesure(LocalDate dateMesure) {
        this.dateMesure = dateMesure;
        return this;
    }

    public String getCodeProcessus() {
        return codeProcessus;
    }

    public IndicateurPerformanceRequest setCodeProcessus(String codeProcessus) {
        this.codeProcessus = codeProcessus;
        return this;
    }
}
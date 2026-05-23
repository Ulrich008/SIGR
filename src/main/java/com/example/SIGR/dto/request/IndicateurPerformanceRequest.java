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

    @PositiveOrZero(message = "La valeur cible doit être >= 0")
    private Double valeurCible;

    @PositiveOrZero(message = "La valeur obtenue doit être >= 0")
    private Double valeurObtenue;

    @PositiveOrZero(message = "Le seuil d'alerte doit être >= 0")
    private Double seuilAlerte;

    @NotNull(message = "La date de mesure est obligatoire")
    private LocalDate dateMesure;

    @NotBlank(message = "Le code du processus est obligatoire")
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

    public Frequence getFrequence() {
        return frequence;
    }

    public IndicateurPerformanceRequest setFrequence(Frequence frequence) {
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
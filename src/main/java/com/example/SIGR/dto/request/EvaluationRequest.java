package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class EvaluationRequest {

    @NotBlank
    private String id;

    @NotNull
    private Integer impact;

    @NotNull
    private Integer probabilite;

    @NotNull
    private LocalDate dateEvaluation;

    private String bonnesPratiques;

    @NotNull
    private Integer niveauControle;

    @NotBlank
    private String idRisque;

    private String idAgent;

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public EvaluationRequest setId(String id) {
        this.id = id;
        return this;
    }

    public Integer getImpact() {
        return impact;
    }

    public EvaluationRequest setImpact(Integer impact) {
        this.impact = impact;
        return this;
    }

    public Integer getProbabilite() {
        return probabilite;
    }

    public EvaluationRequest setProbabilite(Integer probabilite) {
        this.probabilite = probabilite;
        return this;
    }

    public LocalDate getDateEvaluation() {
        return dateEvaluation;
    }

    public EvaluationRequest setDateEvaluation(LocalDate dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
        return this;
    }

    public String getBonnesPratiques() {
        return bonnesPratiques;
    }

    public EvaluationRequest setBonnesPratiques(String bonnesPratiques) {
        this.bonnesPratiques = bonnesPratiques;
        return this;
    }

    public Integer getNiveauControle() {
        return niveauControle;
    }

    public EvaluationRequest setNiveauControle(Integer niveauControle) {
        this.niveauControle = niveauControle;
        return this;
    }

    public String getIdRisque() {
        return idRisque;
    }

    public EvaluationRequest setIdRisque(String idRisque) {
        this.idRisque = idRisque;
        return this;
    }

    public String getIdAgent() {
        return idAgent;
    }

    public EvaluationRequest setIdAgent(String idAgent) {
        this.idAgent = idAgent;
        return this;
    }
}
package com.example.SIGR.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class EvaluationRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotNull(message = "L'impact est obligatoire")
    @Min(value = 1, message = "L'impact doit être >= 1")
    @Max(value = 5, message = "L'impact doit être <= 5")
    private Integer impact;

    @NotNull(message = "La probabilité est obligatoire")
    @Min(value = 1, message = "La probabilité doit être >= 1")
    @Max(value = 5, message = "La probabilité doit être <= 5")
    private Integer probabilite;

    @NotNull(message = "La date d'évaluation est obligatoire")
    private LocalDate dateEvaluation;

    @Size(max = 1000, message = "Les bonnes pratiques sont trop longues")
    private String bonnesPratiques;

    @NotNull(message = "Le niveau de contrôle est obligatoire")
    @Min(value = 1, message = "Le niveau de contrôle doit être >= 1")
    @Max(value = 5, message = "Le niveau de contrôle doit être <= 5")
    private Integer niveauControle;

    @NotBlank(message = "L'identifiant du risque est obligatoire")
    private String idRisque;

    private String idAgent;

    // ================= GETTERS / SETTERS =================

    public String getCode() {
        return code;
    }

    public EvaluationRequest setCode(String code) {
        this.code = code;
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
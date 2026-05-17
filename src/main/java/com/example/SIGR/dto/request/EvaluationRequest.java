package com.example.SIGR.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class EvaluationRequest {

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

    @Size(
            max = 1000,
            message = "Les bonnes pratiques ne doivent pas dépasser 1000 caractères"
    )
    private String bonnesPratiques;

    @NotNull(message = "Le niveau de contrôle est obligatoire")
    @Min(value = 1, message = "Le niveau de contrôle doit être >= 1")
    @Max(value = 5, message = "Le niveau de contrôle doit être <= 5")
    private Integer niveauControle;

    /**
     * ================= RISQUE =================
     * Utilisation du code métier
     */
    @NotBlank(message = "Le code du risque est obligatoire")
    private String codeRisque;

    /**
     * ================= AGENT =================
     * Utilisation du matricule
     */
    private String matriculeAgent;

    // ================= GETTERS / SETTERS =================

    public Integer getImpact() {
        return impact;
    }

    public EvaluationRequest setImpact(
            Integer impact
    ) {
        this.impact = impact;
        return this;
    }

    public Integer getProbabilite() {
        return probabilite;
    }

    public EvaluationRequest setProbabilite(
            Integer probabilite
    ) {
        this.probabilite = probabilite;
        return this;
    }

    public LocalDate getDateEvaluation() {
        return dateEvaluation;
    }

    public EvaluationRequest setDateEvaluation(
            LocalDate dateEvaluation
    ) {
        this.dateEvaluation = dateEvaluation;
        return this;
    }

    public String getBonnesPratiques() {
        return bonnesPratiques;
    }

    public EvaluationRequest setBonnesPratiques(
            String bonnesPratiques
    ) {
        this.bonnesPratiques = bonnesPratiques;
        return this;
    }

    public Integer getNiveauControle() {
        return niveauControle;
    }

    public EvaluationRequest setNiveauControle(
            Integer niveauControle
    ) {
        this.niveauControle = niveauControle;
        return this;
    }

    public String getCodeRisque() {
        return codeRisque;
    }

    public EvaluationRequest setCodeRisque(
            String codeRisque
    ) {
        this.codeRisque = codeRisque;
        return this;
    }

    public String getMatriculeAgent() {
        return matriculeAgent;
    }

    public EvaluationRequest setMatriculeAgent(
            String matriculeAgent
    ) {
        this.matriculeAgent = matriculeAgent;
        return this;
    }
}
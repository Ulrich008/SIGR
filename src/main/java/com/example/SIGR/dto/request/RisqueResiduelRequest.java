package com.example.SIGR.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RisqueResiduelRequest {

    @NotNull(message = "L'impact résiduel est obligatoire")
    @Min(value = 1, message = "L'impact résiduel doit être >= 1")
    @Max(value = 5, message = "L'impact résiduel doit être <= 5")
    private Integer impactResiduel;

    @NotNull(message = "La probabilité résiduelle est obligatoire")
    @Min(value = 1, message = "La probabilité résiduelle doit être >= 1")
    @Max(value = 5, message = "La probabilité résiduelle doit être <= 5")
    private Integer probabiliteResiduelle;

    @NotBlank(message = "Le code de l'évaluation est obligatoire")
    private String codeEvaluation;

    @NotBlank(message = "Le code du risque est obligatoire")
    private String codeRisque;

    // ================= GETTERS / SETTERS =================

    public Integer getImpactResiduel() {
        return impactResiduel;
    }

    public RisqueResiduelRequest setImpactResiduel(
            Integer impactResiduel
    ) {
        this.impactResiduel = impactResiduel;
        return this;
    }

    public Integer getProbabiliteResiduelle() {
        return probabiliteResiduelle;
    }

    public RisqueResiduelRequest setProbabiliteResiduelle(
            Integer probabiliteResiduelle
    ) {
        this.probabiliteResiduelle = probabiliteResiduelle;
        return this;
    }

    public String getCodeEvaluation() {
        return codeEvaluation;
    }

    public RisqueResiduelRequest setCodeEvaluation(
            String codeEvaluation
    ) {
        this.codeEvaluation = codeEvaluation;
        return this;
    }

    public String getCodeRisque() {
        return codeRisque;
    }

    public RisqueResiduelRequest setCodeRisque(
            String codeRisque
    ) {
        this.codeRisque = codeRisque;
        return this;
    }
}
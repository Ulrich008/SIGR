package com.example.SIGR.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RisqueResiduelRequest {

    @NotBlank(message = "Le code est obligatoire")
    private String code;

    @NotNull(message = "L'impact résiduel est obligatoire")
    @Min(value = 1, message = "L'impact résiduel doit être >= 1")
    @Max(value = 5, message = "L'impact résiduel doit être <= 5")
    private Integer impactResiduel;

    @NotNull(message = "La probabilité résiduelle est obligatoire")
    @Min(value = 1, message = "La probabilité résiduelle doit être >= 1")
    @Max(value = 5, message = "La probabilité résiduelle doit être <= 5")
    private Integer probabiliteResiduelle;

    @NotBlank(message = "L'identifiant de l'évaluation est obligatoire")
    private String idEvaluation;

    @NotBlank(message = "L'identifiant du risque est obligatoire")
    private String idRisque;

    // ================= GETTERS / SETTERS =================

    public String getCode() {
        return code;
    }

    public RisqueResiduelRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public Integer getImpactResiduel() {
        return impactResiduel;
    }

    public RisqueResiduelRequest setImpactResiduel(Integer impactResiduel) {
        this.impactResiduel = impactResiduel;
        return this;
    }

    public Integer getProbabiliteResiduelle() {
        return probabiliteResiduelle;
    }

    public RisqueResiduelRequest setProbabiliteResiduelle(Integer probabiliteResiduelle) {
        this.probabiliteResiduelle = probabiliteResiduelle;
        return this;
    }

    public String getIdEvaluation() {
        return idEvaluation;
    }

    public RisqueResiduelRequest setIdEvaluation(String idEvaluation) {
        this.idEvaluation = idEvaluation;
        return this;
    }

    public String getIdRisque() {
        return idRisque;
    }

    public RisqueResiduelRequest setIdRisque(String idRisque) {
        this.idRisque = idRisque;
        return this;
    }
}
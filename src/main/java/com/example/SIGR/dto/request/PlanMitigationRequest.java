package com.example.SIGR.dto.request;

import com.example.SIGR.entity.StatutPlanMitigation;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class PlanMitigationRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @Size(max = 1000, message = "La description est trop longue")
    private String description;

    @NotNull(message = "La date de création est obligatoire")
    private LocalDate dateCreation;

    @NotNull(message = "Le statut est obligatoire")
    private StatutPlanMitigation statut;

    @NotBlank(message = "L'identifiant du risque est obligatoire")
    private String idRisque;

    // GETTERS / SETTERS

    public String getCode() {
        return code;
    }

    public PlanMitigationRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PlanMitigationRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public PlanMitigationRequest setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public StatutPlanMitigation getStatut() {
        return statut;
    }

    public PlanMitigationRequest setStatut(StatutPlanMitigation statut) {
        this.statut = statut;
        return this;
    }

    public String getIdRisque() {
        return idRisque;
    }

    public PlanMitigationRequest setIdRisque(String idRisque) {
        this.idRisque = idRisque;
        return this;
    }
}
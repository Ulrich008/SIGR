package com.example.SIGR.dto.request;

import com.example.SIGR.entity.StatutPlanMitigation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PlanMitigationRequest {

    @NotBlank
    private String id;

    private String description;

    @NotNull
    private LocalDate dateCreation;

    @NotNull
    private StatutPlanMitigation statut;

    @NotBlank
    private String idRisque;

    // GETTERS / SETTERS

    public String getId() { return id; }

    public PlanMitigationRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getDescription() { return description; }

    public PlanMitigationRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getDateCreation() { return dateCreation; }

    public PlanMitigationRequest setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public StatutPlanMitigation getStatut() { return statut; }

    public PlanMitigationRequest setStatut(StatutPlanMitigation statut) {
        this.statut = statut;
        return this;
    }

    public String getIdRisque() { return idRisque; }

    public PlanMitigationRequest setIdRisque(String idRisque) {
        this.idRisque = idRisque;
        return this;
    }
}
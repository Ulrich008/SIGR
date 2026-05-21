package com.example.SIGR.dto.request;

import com.example.SIGR.entity.StatutPlanMitigation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PlanMitigationRequest {

    /**
     * Le code est généré automatiquement
     * donc supprimé du request
     */
    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 200, message = "Le libellé ne doit pas dépasser 200 caractères")
    private String libelle;

    public String getLibelle() {
        return libelle;
    }

    @Size(
            max = 1000,
            message = "La description ne doit pas dépasser 1000 caractères"
    )
    private String description;

    @NotNull(
            message = "La date de création est obligatoire"
    )
    private LocalDate dateCreation;

    @NotNull(
            message = "Le statut est obligatoire"
    )
    private StatutPlanMitigation statut;

    /**
     * Utilisation du CODE métier
     * et non de l'id technique
     */
    @NotBlank(
            message = "Le code du risque est obligatoire"
    )
    private String codeRisque;

    // ================= GETTERS / SETTERS =================

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public PlanMitigationRequest setDescription(
            String description
    ) {
        this.description = description;
        return this;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public PlanMitigationRequest setDateCreation(
            LocalDate dateCreation
    ) {
        this.dateCreation = dateCreation;
        return this;
    }

    public StatutPlanMitigation getStatut() {
        return statut;
    }

    public PlanMitigationRequest setStatut(
            StatutPlanMitigation statut
    ) {
        this.statut = statut;
        return this;
    }

    public String getCodeRisque() {
        return codeRisque;
    }

    public PlanMitigationRequest setCodeRisque(
            String codeRisque
    ) {
        this.codeRisque = codeRisque;
        return this;
    }
}
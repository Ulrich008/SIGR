package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

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

    /**
     * Utilisation des CODES métier
     * et non des ids techniques.
     * Un plan de mitigation peut désormais couvrir plusieurs risques.
     */
    @NotEmpty(
            message = "Au moins un risque est obligatoire"
    )
    private List<String> codesRisques;

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

    public List<String> getCodesRisques() {
        return codesRisques;
    }

    public PlanMitigationRequest setCodesRisques(
            List<String> codesRisques
    ) {
        this.codesRisques = codesRisques;
        return this;
    }
}
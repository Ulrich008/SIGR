package com.example.SIGR.dto.response;

import com.example.SIGR.entity.StatutPlanMitigation;

import java.time.LocalDate;

public class PlanMitigationResponse {

    private String id;
    private String description;
    private LocalDate dateCreation;
    private StatutPlanMitigation statut;

    private String idRisque;
    private String libelleRisque;

    public PlanMitigationResponse(
            String id,
            String description,
            LocalDate dateCreation,
            StatutPlanMitigation statut,
            String idRisque,
            String libelleRisque
    ) {
        this.id = id;
        this.description = description;
        this.dateCreation = dateCreation;
        this.statut = statut;
        this.idRisque = idRisque;
        this.libelleRisque = libelleRisque;
    }

    // GETTERS

    public String getId() { return id; }

    public String getDescription() { return description; }

    public LocalDate getDateCreation() { return dateCreation; }

    public StatutPlanMitigation getStatut() { return statut; }

    public String getIdRisque() { return idRisque; }

    public String getLibelleRisque() { return libelleRisque; }
}
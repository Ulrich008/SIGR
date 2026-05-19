package com.example.SIGR.dto.response;

import com.example.SIGR.entity.StatutPlanMitigation;

import java.time.LocalDate;

public class PlanMitigationResponse {

    private String id;

    private String code;

    private String description;

    private LocalDate dateCreation;

    private StatutPlanMitigation statut;

    /**
     * Utilisation du CODE métier
     * au lieu de l'id technique
     */
    private String codeRisque;

    private String libelleRisque;

    public PlanMitigationResponse(
            String id,
            String code,
            String description,
            LocalDate dateCreation,
            StatutPlanMitigation statut,
            String codeRisque,
            String libelleRisque
    ) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.dateCreation = dateCreation;
        this.statut = statut;
        this.codeRisque = codeRisque;
        this.libelleRisque = libelleRisque;
    }

    // ================= GETTERS =================

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public StatutPlanMitigation getStatut() {
        return statut;
    }

    public String getCodeRisque() {
        return codeRisque;
    }

    public String getLibelleRisque() {
        return libelleRisque;
    }
}
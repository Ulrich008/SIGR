package com.example.SIGR.dto.response;

import com.example.SIGR.entity.StatutPlanMitigation;

import java.time.LocalDate;
import java.util.List;

public class PlanMitigationResponse {

    private String id;

    private String code;

    private String libelle;

    private String description;

    private LocalDate dateCreation;

    private StatutPlanMitigation statut;

    /**
     * Utilisation des CODES métier
     * au lieu des ids techniques.
     * Un plan de mitigation peut désormais couvrir plusieurs risques.
     */
    private List<String> codesRisques;

    private List<String> libellesRisques;

    public String getLibelle() {
        return libelle;
    }

    public PlanMitigationResponse(
            String id,
            String code,
            String libelle,
            String description,
            LocalDate dateCreation,
            StatutPlanMitigation statut,
            List<String> codesRisques,
            List<String> libellesRisques
    ) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.description = description;
        this.dateCreation = dateCreation;
        this.statut = statut;
        this.codesRisques = codesRisques;
        this.libellesRisques = libellesRisques;
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

    public List<String> getCodesRisques() {
        return codesRisques;
    }

    public List<String> getLibellesRisques() {
        return libellesRisques;
    }
}
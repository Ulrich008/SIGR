package com.example.SIGR.dto.response;

import com.example.SIGR.entity.AuditPropose;
import com.example.SIGR.entity.StatutSuiviRecommandation;
import com.example.SIGR.entity.TypeRevue;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlanAuditResponse {

    private String id;
    private String code;
    private String libelle;
    private LocalDate dateCreation;
    private String codeUniteAdministrative;
    private String nomUniteAdministrative;
    private String codeProcessus;
    private String nomProcessus;
    private String codeRisque;
    private String libelleRisque;
    private AuditPropose auditPropose;
    private TypeRevue typeRevue;
    private String objectifAudit;
    private String effetAuditIndicatif;
    private String recommandation;
    private StatutSuiviRecommandation statutSuivi;
    private String decisionSuivi;
    private LocalDateTime dateDecisionSuivi;

    public PlanAuditResponse(
            String id,
            String code,
            String libelle,
            LocalDate dateCreation,
            String codeUniteAdministrative,
            String nomUniteAdministrative,
            String codeProcessus,
            String nomProcessus,
            String codeRisque,
            String libelleRisque,
            AuditPropose auditPropose,
            TypeRevue typeRevue,
            String objectifAudit,
            String effetAuditIndicatif,
            String recommandation,
            StatutSuiviRecommandation statutSuivi,
            String decisionSuivi,
            LocalDateTime dateDecisionSuivi
    ) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.dateCreation = dateCreation;
        this.codeUniteAdministrative = codeUniteAdministrative;
        this.nomUniteAdministrative = nomUniteAdministrative;
        this.codeProcessus = codeProcessus;
        this.nomProcessus = nomProcessus;
        this.codeRisque = codeRisque;
        this.libelleRisque = libelleRisque;
        this.auditPropose = auditPropose;
        this.typeRevue = typeRevue;
        this.objectifAudit = objectifAudit;
        this.effetAuditIndicatif = effetAuditIndicatif;
        this.recommandation = recommandation;
        this.statutSuivi = statutSuivi;
        this.decisionSuivi = decisionSuivi;
        this.dateDecisionSuivi = dateDecisionSuivi;
    }

    // ================= GETTERS =================

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public String getCodeUniteAdministrative() {
        return codeUniteAdministrative;
    }

    public String getNomUniteAdministrative() {
        return nomUniteAdministrative;
    }

    public String getCodeProcessus() {
        return codeProcessus;
    }

    public String getNomProcessus() {
        return nomProcessus;
    }

    public String getCodeRisque() {
        return codeRisque;
    }

    public String getLibelleRisque() {
        return libelleRisque;
    }

    public AuditPropose getAuditPropose() {
        return auditPropose;
    }

    public TypeRevue getTypeRevue() {
        return typeRevue;
    }

    public String getObjectifAudit() {
        return objectifAudit;
    }

    public String getEffetAuditIndicatif() {
        return effetAuditIndicatif;
    }

    public String getRecommandation() {
        return recommandation;
    }

    public StatutSuiviRecommandation getStatutSuivi() {
        return statutSuivi;
    }

    public String getDecisionSuivi() {
        return decisionSuivi;
    }

    public LocalDateTime getDateDecisionSuivi() {
        return dateDecisionSuivi;
    }
}

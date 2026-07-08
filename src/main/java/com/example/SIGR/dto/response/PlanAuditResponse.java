package com.example.SIGR.dto.response;

import com.example.SIGR.entity.AuditPropose;
import com.example.SIGR.entity.TypeRevue;

import java.time.LocalDate;

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
            String effetAuditIndicatif
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
}

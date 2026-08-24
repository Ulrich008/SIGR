package com.example.SIGR.dto.request;

import com.example.SIGR.entity.AuditPropose;
import com.example.SIGR.entity.TypeRevue;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class PlanAuditRequest {

    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 200, message = "Le libellé ne doit pas dépasser 200 caractères")
    private String libelle;

    @NotNull(message = "La date de création est obligatoire")
    private LocalDate dateCreation;

    @NotBlank(message = "L'unité administrative est obligatoire")
    private String codeUniteAdministrative;

    @NotBlank(message = "Le processus est obligatoire")
    private String codeProcessus;

    private String codeRisque;

    @NotNull(message = "L'audit proposé est obligatoire")
    private AuditPropose auditPropose;

    @NotNull(message = "Le type de revue est obligatoire")
    private TypeRevue typeRevue;

    @NotBlank(message = "L'objectif d'audit est obligatoire")
    @Size(max = 1000, message = "L'objectif d'audit ne doit pas dépasser 1000 caractères")
    private String objectifAudit;

    @NotBlank(message = "L'effort d'audit indicatif est obligatoire")
    @Size(max = 1000, message = "L'effort d'audit indicatif ne doit pas dépasser 1000 caractères")
    private String effetAuditIndicatif;

    @Size(max = 1000, message = "La recommandation ne doit pas dépasser 1000 caractères")
    private String recommandation;

    // ================= GETTERS / SETTERS =================

    public String getCode() {
        return code;
    }

    public PlanAuditRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public PlanAuditRequest setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public PlanAuditRequest setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public String getCodeUniteAdministrative() {
        return codeUniteAdministrative;
    }

    public PlanAuditRequest setCodeUniteAdministrative(String codeUniteAdministrative) {
        this.codeUniteAdministrative = codeUniteAdministrative;
        return this;
    }

    public String getCodeProcessus() {
        return codeProcessus;
    }

    public PlanAuditRequest setCodeProcessus(String codeProcessus) {
        this.codeProcessus = codeProcessus;
        return this;
    }

    public String getCodeRisque() {
        return codeRisque;
    }

    public PlanAuditRequest setCodeRisque(String codeRisque) {
        this.codeRisque = codeRisque;
        return this;
    }

    public AuditPropose getAuditPropose() {
        return auditPropose;
    }

    public PlanAuditRequest setAuditPropose(AuditPropose auditPropose) {
        this.auditPropose = auditPropose;
        return this;
    }

    public TypeRevue getTypeRevue() {
        return typeRevue;
    }

    public PlanAuditRequest setTypeRevue(TypeRevue typeRevue) {
        this.typeRevue = typeRevue;
        return this;
    }

    public String getObjectifAudit() {
        return objectifAudit;
    }

    public PlanAuditRequest setObjectifAudit(String objectifAudit) {
        this.objectifAudit = objectifAudit;
        return this;
    }

    public String getEffetAuditIndicatif() {
        return effetAuditIndicatif;
    }

    public PlanAuditRequest setEffetAuditIndicatif(String effetAuditIndicatif) {
        this.effetAuditIndicatif = effetAuditIndicatif;
        return this;
    }

    public String getRecommandation() {
        return recommandation;
    }

    public PlanAuditRequest setRecommandation(String recommandation) {
        this.recommandation = recommandation;
        return this;
    }
}

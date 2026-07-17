package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import java.time.LocalDate;

@Entity
@Table(name = "plan_audit")
@Audited
@FilterDef(name = "ministereFilter", parameters = @ParamDef(name = "codeMinistere", type = String.class))
@Filter(name = "ministereFilter", condition = "id_unite_administrative IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_ministere = :codeMinistere)")
public class PlanAudit extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_plan_audit", length = 50, updatable = false, nullable = false)
    private String id;

    @Column(name = "code", length = 50, unique = true, nullable = false)
    private String code;

    @Column(name = "libelle", length = 200, nullable = false)
    private String libelle;

    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unite_administrative", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private UniteAdministrative uniteAdministrative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_processus", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Processus processus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_risque")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Risque risque;

    @Enumerated(EnumType.STRING)
    @Column(name = "audit_propose", length = 50, nullable = false)
    private AuditPropose auditPropose;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_revue", length = 50, nullable = false)
    private TypeRevue typeRevue;

    @Column(name = "objectif_audit", length = 1000)
    private String objectifAudit;

    @Column(name = "effet_audit_indicatif", length = 1000)
    private String effetAuditIndicatif;

    public String getId() {
        return id;
    }

    public PlanAudit setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public PlanAudit setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public PlanAudit setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public PlanAudit setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public UniteAdministrative getUniteAdministrative() {
        return uniteAdministrative;
    }

    public PlanAudit setUniteAdministrative(UniteAdministrative uniteAdministrative) {
        this.uniteAdministrative = uniteAdministrative;
        return this;
    }

    public Processus getProcessus() {
        return processus;
    }

    public PlanAudit setProcessus(Processus processus) {
        this.processus = processus;
        return this;
    }

    public Risque getRisque() {
        return risque;
    }

    public PlanAudit setRisque(Risque risque) {
        this.risque = risque;
        return this;
    }

    public AuditPropose getAuditPropose() {
        return auditPropose;
    }

    public PlanAudit setAuditPropose(AuditPropose auditPropose) {
        this.auditPropose = auditPropose;
        return this;
    }

    public TypeRevue getTypeRevue() {
        return typeRevue;
    }

    public PlanAudit setTypeRevue(TypeRevue typeRevue) {
        this.typeRevue = typeRevue;
        return this;
    }

    public String getObjectifAudit() {
        return objectifAudit;
    }

    public PlanAudit setObjectifAudit(String objectifAudit) {
        this.objectifAudit = objectifAudit;
        return this;
    }

    public String getEffetAuditIndicatif() {
        return effetAuditIndicatif;
    }

    public PlanAudit setEffetAuditIndicatif(String effetAuditIndicatif) {
        this.effetAuditIndicatif = effetAuditIndicatif;
        return this;
    }
}

package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "action")
@Audited
@FilterDef(name = "ministereFilter", parameters = @ParamDef(name = "codeMinistere", type = String.class))
@Filter(name = "ministereFilter", condition = "id_plan IN (SELECT pmr.id_plan FROM plan_mitigation_risque pmr JOIN risque r ON r.id_risque = pmr.id_risque WHERE r.code_processus IN (SELECT p.code_processus FROM processus p WHERE p.id_unite IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_ministere = :codeMinistere)))")
@FilterDef(name = "uaFilter", parameters = @ParamDef(name = "codeUnite", type = String.class))
@Filter(name = "uaFilter", condition = "id_plan IN (SELECT pmr.id_plan FROM plan_mitigation_risque pmr JOIN risque r ON r.id_risque = pmr.id_risque WHERE r.code_processus IN (SELECT p.code_processus FROM processus p WHERE p.id_unite IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_unite = :codeUnite)))")
public class Action extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_action", length = 50)
    private String id;

    @Column(name = "code_action", length = 50, unique = true, nullable = false)
    private String code;

    @Column(name = "libelle", length = 2000)
    private String libelle;

    @Column(name = "code_risque", length = 50)
    private String codeRisque;

    @Column(name = "bonne_pratique", length = 500)
    private String bonnePratique;

    /**
     * Date de début de l'action
     */
    @Column(name = "date_debut")
    private LocalDate dateDebut;

    /**
     * Date de fin prévue de l'action
     */
    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", length = 50, nullable = false)
    private StatutAction statut;

    /**
     * RELATION :
     * Plusieurs actions peuvent appartenir à un même plan de mitigation
     * => Many Actions → One PlanMitigation
     */
    @ManyToOne
    @JoinColumn(name = "id_plan", nullable = false)
    private PlanMitigation planMitigation;

    /**
     * RELATION :
     * Plusieurs actions peuvent être assignées à un même agent responsable
     * => Many Actions → One Agent
     */
    @ManyToOne
    @JoinColumn(name = "matricule_responsable", referencedColumnName = "matricule_agent", nullable = false)
    private Agent responsable;

    // ===================== GETTERS / SETTERS =====================

    public String getId() {
        return id;
    }

    public Action setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Action setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public Action setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    // Helpers pour convertir entre String (stockage) et List<String> (API)
    public List<String> getLibelles() {
        if (libelle == null || libelle.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return java.util.Arrays.asList(libelle.split(";")).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toList());
    }

    public Action setLibelles(List<String> libelles) {
        if (libelles == null || libelles.isEmpty()) {
            this.libelle = null;
        } else {
            this.libelle = String.join("; ", libelles);
        }
        return this;
    }

    public String getCodeRisque() {
        return codeRisque;
    }

    public Action setCodeRisque(String codeRisque) {
        this.codeRisque = codeRisque;
        return this;
    }

    public String getBonnePratique() {
        return bonnePratique;
    }

    public Action setBonnePratique(String bonnePratique) {
        this.bonnePratique = bonnePratique;
        return this;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public Action setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public Action setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public StatutAction getStatut() {
        return statut;
    }

    public Action setStatut(StatutAction statut) {
        this.statut = statut;
        return this;
    }

    public PlanMitigation getPlanMitigation() {
        return planMitigation;
    }

    public Action setPlanMitigation(PlanMitigation planMitigation) {
        this.planMitigation = planMitigation;
        return this;
    }

    public Agent getResponsable() {
        return responsable;
    }

    public Action setResponsable(Agent responsable) {
        this.responsable = responsable;
        return this;
    }
}
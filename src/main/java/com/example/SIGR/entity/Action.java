package com.example.SIGR.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "action")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_action", length = 50)
    private String id;

    @Column(name = "code_action", length = 50, unique = true, nullable = false)
    private String code;

    @Column(name = "libelle", length = 200)
    private String libelle;

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
    @JoinColumn(name = "matricule_responsable", nullable = false)
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
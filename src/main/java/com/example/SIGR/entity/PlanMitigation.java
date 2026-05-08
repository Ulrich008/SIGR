package com.example.SIGR.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "plan_mitigation")
public class PlanMitigation {

    @Id
    @Column(name = "id_plan", length = 50)
    private String id;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", length = 50)
    private StatutPlanMitigation statut;

    // 🔗 Un plan de mitigation concerne un seul risque
    @ManyToOne
    @JoinColumn(name = "id_risque", nullable = false)
    private Risque risque;

    // 🔗 Un plan contient plusieurs actions
    @OneToMany(mappedBy = "planMitigation")
    private List<Action> actions;

    // ===================== GETTERS / SETTERS =====================

    public String getId() {
        return id;
    }

    public PlanMitigation setId(String id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PlanMitigation setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public PlanMitigation setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public StatutPlanMitigation getStatut() { return statut; }

    public PlanMitigation setStatut(StatutPlanMitigation statut) {
        this.statut = statut;
        return this;
    }


    public Risque getRisque() {
        return risque;
    }

    public PlanMitigation setRisque(Risque risque) {
        this.risque = risque;
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public PlanMitigation setActions(List<Action> actions) {
        this.actions = actions;
        return this;
    }
}
package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plan_mitigation")
@Audited
@FilterDef(name = "ministereFilter", parameters = @ParamDef(name = "codeMinistere", type = String.class))
@Filter(name = "ministereFilter", condition = "id_plan IN (SELECT pmr.id_plan FROM plan_mitigation_risque pmr JOIN risque r ON r.id_risque = pmr.id_risque WHERE r.code_processus IN (SELECT p.code_processus FROM processus p WHERE p.id_unite IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_ministere = :codeMinistere)))")
@FilterDef(name = "uaFilter", parameters = @ParamDef(name = "codeUnite", type = String.class))
@Filter(name = "uaFilter", condition = "id_plan IN (SELECT pmr.id_plan FROM plan_mitigation_risque pmr JOIN risque r ON r.id_risque = pmr.id_risque WHERE r.code_processus IN (SELECT p.code_processus FROM processus p WHERE p.id_unite IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_unite = :codeUnite)))")
public class PlanMitigation extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_plan", updatable = false, nullable = false)
    private String id;

    @Column(name = "code", length = 50, unique = true, nullable = false)
    private String code;

    @Column(name = "libelle_plan", length = 200)
    private String libelle;


    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", length = 50)
    private StatutPlanMitigation statut;

    /**
     * Un plan de mitigation peut désormais traiter plusieurs risques à la
     * fois (relation N-N, portée par la table de jointure dédiée
     * plan_mitigation_risque) — auparavant limité à un seul risque via une
     * simple colonne id_risque sur cette table.
     */
    @ManyToMany
    @JoinTable(
            name = "plan_mitigation_risque",
            joinColumns = @JoinColumn(name = "id_plan"),
            inverseJoinColumns = @JoinColumn(name = "id_risque")
    )
    private List<Risque> risques = new ArrayList<>();

    @OneToMany(mappedBy = "planMitigation")
    private List<Action> actions;

    // ===================== GETTERS / SETTERS =====================

    public String getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public PlanMitigation setCode(String code) {
        this.code = code;
        return this;
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

    public StatutPlanMitigation getStatut() {
        return statut;
    }

    public PlanMitigation setStatut(StatutPlanMitigation statut) {
        this.statut = statut;
        return this;
    }

    public List<Risque> getRisques() {
        return risques;
    }

    public PlanMitigation setRisques(List<Risque> risques) {
        this.risques = risques;
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
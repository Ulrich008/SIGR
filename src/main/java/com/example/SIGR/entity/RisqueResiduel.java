package com.example.SIGR.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "risque_residuel")
public class RisqueResiduel {

    @Id
    @Column(name = "id_risqueresiduel", length = 50)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    private Integer impactResiduel;

    private Integer probabiliteResiduelle;

    @ManyToOne
    @JoinColumn(name = "id_evaluation", nullable = false)
    private Evaluation evaluation;

    @ManyToOne
    @JoinColumn(name = "id_risque", nullable = false)
    private Risque risque;

    // Calcul clé métier
    @Transient
    public Integer getScoreResiduel() {
        if (impactResiduel == null || probabiliteResiduelle == null) return null;
        return impactResiduel * probabiliteResiduelle;
    }

    // Niveau de risque
    @Transient
    public String getNiveauRisque() {
        Integer score = getScoreResiduel();
        if (score == null) return "INDEFINI";

        if (score <= 5) return "FAIBLE";
        if (score <= 15) return "MOYEN";
        return "ELEVE";
    }

    // Getters / Setters

    public String getId() {
        return id;
    }

    public RisqueResiduel setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public RisqueResiduel setCode(String code) {
        this.code = code;
        return this;
    }

    public Integer getImpactResiduel() {
        return impactResiduel;
    }

    public RisqueResiduel setImpactResiduel(Integer impactResiduel) {
        this.impactResiduel = impactResiduel;
        return this;
    }

    public Integer getProbabiliteResiduelle() {
        return probabiliteResiduelle;
    }

    public RisqueResiduel setProbabiliteResiduelle(Integer probabiliteResiduelle) {
        this.probabiliteResiduelle = probabiliteResiduelle;
        return this;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public RisqueResiduel setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
        return this;
    }

    public Risque getRisque() {
        return risque;
    }

    public RisqueResiduel setRisque(Risque risque) {
        this.risque = risque;
        return this;
    }
}
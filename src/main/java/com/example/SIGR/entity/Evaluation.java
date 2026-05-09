package com.example.SIGR.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "evaluation")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_evaluation", length = 50)
    private String id;

    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    private Integer impact;

    private Integer probabilite;

    @Column(name = "date_evaluation")
    private LocalDate dateEvaluation;

    @Column(name = "bonnes_pratiques", length = 500)
    private String bonnesPratiques;

    @Column(name = "niveau_controle")
    private Integer niveauControle;

    @ManyToOne
    @JoinColumn(name = "id_risque", nullable = false)
    private Risque risque;

    @ManyToOne
    @JoinColumn(name = "evalue_par")
    private Agent evaluePar;

    // ================= BUSINESS METHOD =================
    @Transient
    public Integer getScoreInitial() {
        if (impact == null || probabilite == null) return null;
        return impact * probabilite;
    }

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public Evaluation setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Evaluation setCode(String code) {
        this.code = code;
        return this;
    }

    public Integer getImpact() {
        return impact;
    }

    public Evaluation setImpact(Integer impact) {
        this.impact = impact;
        return this;
    }

    public Integer getProbabilite() {
        return probabilite;
    }

    public Evaluation setProbabilite(Integer probabilite) {
        this.probabilite = probabilite;
        return this;
    }

    public LocalDate getDateEvaluation() {
        return dateEvaluation;
    }

    public Evaluation setDateEvaluation(LocalDate dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
        return this;
    }

    public String getBonnesPratiques() {
        return bonnesPratiques;
    }

    public Evaluation setBonnesPratiques(String bonnesPratiques) {
        this.bonnesPratiques = bonnesPratiques;
        return this;
    }

    public Integer getNiveauControle() {
        return niveauControle;
    }

    public Evaluation setNiveauControle(Integer niveauControle) {
        this.niveauControle = niveauControle;
        return this;
    }

    public Risque getRisque() {
        return risque;
    }

    public Evaluation setRisque(Risque risque) {
        this.risque = risque;
        return this;
    }

    public Agent getEvaluePar() {
        return evaluePar;
    }

    public Evaluation setEvaluePar(Agent evaluePar) {
        this.evaluePar = evaluePar;
        return this;
    }
}
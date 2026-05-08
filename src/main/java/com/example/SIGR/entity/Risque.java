package com.example.SIGR.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "risque")
public class Risque {

    @Id
    @Column(name = "id_risque", length = 50)
    private String id;

    @Column(name = "libelle_risque", length = 200)
    private String libelle;

    @Column(name = "categorie_risque", length = 100)
    private String categorie;

    @Column(name = "cause_probable", length = 500)
    private String causeProbable;

    @Column(name = "consequence_probable", length = 500)
    private String consequenceProbable;

    @Column(name = "statut_risque", length = 50)
    private String statut;

    @Column(name = "date_identification")
    private LocalDate dateIdentification;

    /**
     * Plusieurs risques peuvent appartenir
     * à un même processus
     */
    @ManyToOne
    @JoinColumn(name = "code_processus", nullable = false)
    private Processus processus;

    /**
     * Plusieurs risques peuvent appartenir
     * à une même cartographie
     */
    @ManyToOne
    @JoinColumn(name = "id_cartographie")
    private CartographieRisques cartographie;

    /**
     * Type du risque
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type_risque", length = 50)
    private TypeRisque typeRisque;

    /**
     * Un risque peut avoir plusieurs évaluations
     */
    @OneToMany(mappedBy = "risque")
    private List<Evaluation> evaluations;

    /**
     * Un risque peut avoir plusieurs plans de mitigation
     */
    @OneToMany(mappedBy = "risque")
    private List<PlanMitigation> plansMitigation;

    // ===================== GETTERS / SETTERS =====================

    public String getId() {
        return id;
    }

    public Risque setId(String id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public Risque setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getCategorie() {
        return categorie;
    }

    public Risque setCategorie(String categorie) {
        this.categorie = categorie;
        return this;
    }

    public String getCauseProbable() {
        return causeProbable;
    }

    public Risque setCauseProbable(String causeProbable) {
        this.causeProbable = causeProbable;
        return this;
    }

    public String getConsequenceProbable() {
        return consequenceProbable;
    }

    public Risque setConsequenceProbable(String consequenceProbable) {
        this.consequenceProbable = consequenceProbable;
        return this;
    }

    public String getStatut() {
        return statut;
    }

    public Risque setStatut(String statut) {
        this.statut = statut;
        return this;
    }

    public LocalDate getDateIdentification() {
        return dateIdentification;
    }

    public Risque setDateIdentification(LocalDate dateIdentification) {
        this.dateIdentification = dateIdentification;
        return this;
    }

    public Processus getProcessus() {
        return processus;
    }

    public Risque setProcessus(Processus processus) {
        this.processus = processus;
        return this;
    }

    public CartographieRisques getCartographie() {
        return cartographie;
    }

    public Risque setCartographie(CartographieRisques cartographie) {
        this.cartographie = cartographie;
        return this;
    }

    public TypeRisque getTypeRisque() {
        return typeRisque;
    }

    public Risque setTypeRisque(TypeRisque typeRisque) {
        this.typeRisque = typeRisque;
        return this;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public Risque setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
        return this;
    }

    public List<PlanMitigation> getPlansMitigation() {
        return plansMitigation;
    }

    public Risque setPlansMitigation(List<PlanMitigation> plansMitigation) {
        this.plansMitigation = plansMitigation;
        return this;
    }
}
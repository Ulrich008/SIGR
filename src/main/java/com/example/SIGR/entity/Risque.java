package com.example.SIGR.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "risque")
public class Risque {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_risque", length = 50, updatable = false, nullable = false)
    private String id;

    @Column(name = "code", length = 50, unique = true, nullable = false)
    private String code;

    @Column(name = "libelle_risque", length = 200)
    private String libelle;

    @Column(name = "cause_probable", length = 500)
    private String causeProbable;

    @Column(name = "consequence_probable", length = 500)
    private String consequenceProbable;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_risque", length = 50)
    private StatutRisque statut;

    @Column(name = "date_identification")
    private LocalDate dateIdentification;

    /**
     * RELATION :
     * Plusieurs risques peuvent appartenir à un même processus
     * => Many Risques → One Processus
     */
    @ManyToOne
    @JoinColumn(name = "code_processus", nullable = false)
    private Processus processus;

    /**
     * RELATION :
     * Plusieurs risques peuvent être regroupés dans une cartographie
     * => Many Risques → One CartographieRisques
     */
    @ManyToOne
    @JoinColumn(name = "id_cartographie")
    private CartographieRisques cartographie;

    /**
     * RELATION :
     * Un risque possède un type (classification métier)
     * => Enum simple (pas une relation objet)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type_risque", length = 50)
    private TypeRisque typeRisque;

    /**
     * Un risque peut avoir plusieurs évaluations
     */
    @OneToMany(mappedBy = "risque")
    private List<Evaluation> evaluations;

    /*
     * Un risque peut être traité par plusieurs plans de mitigation
     */
    @OneToMany(mappedBy = "risque")
    private List<PlanMitigation> plansMitigation;

    /*
     * Un risque peut générer plusieurs risques résiduels après traitement
     */
    @OneToMany(mappedBy = "risque", cascade = CascadeType.ALL)
    private List<RisqueResiduel> risquesResiduels;

    // ===================== GETTERS / SETTERS =====================

    public String getId() { return id; }
    public Risque setId(String id) { this.id = id; return this; }

    public String getCode() { return code; }
    public Risque setCode(String code) { this.code = code; return this; }

    public String getLibelle() { return libelle; }
    public Risque setLibelle(String libelle) { this.libelle = libelle; return this; }

    public String getCauseProbable() { return causeProbable; }
    public Risque setCauseProbable(String causeProbable) { this.causeProbable = causeProbable; return this; }

    public String getConsequenceProbable() { return consequenceProbable; }
    public Risque setConsequenceProbable(String consequenceProbable) { this.consequenceProbable = consequenceProbable; return this; }

    public StatutRisque getStatut() { return statut; }
    public Risque setStatut(StatutRisque statut) { this.statut = statut; return this; }

    public LocalDate getDateIdentification() { return dateIdentification; }
    public Risque setDateIdentification(LocalDate dateIdentification) { this.dateIdentification = dateIdentification; return this; }

    public Processus getProcessus() { return processus; }
    public Risque setProcessus(Processus processus) { this.processus = processus; return this; }

    public CartographieRisques getCartographie() { return cartographie; }
    public Risque setCartographie(CartographieRisques cartographie) { this.cartographie = cartographie; return this; }

    public TypeRisque getTypeRisque() { return typeRisque; }
    public Risque setTypeRisque(TypeRisque typeRisque) { this.typeRisque = typeRisque; return this; }

    public List<Evaluation> getEvaluations() { return evaluations; }
    public Risque setEvaluations(List<Evaluation> evaluations) { this.evaluations = evaluations; return this; }

    public List<PlanMitigation> getPlansMitigation() { return plansMitigation; }
    public Risque setPlansMitigation(List<PlanMitigation> plansMitigation) { this.plansMitigation = plansMitigation; return this; }

    public List<RisqueResiduel> getRisquesResiduels() { return risquesResiduels; }
    public Risque setRisquesResiduels(List<RisqueResiduel> risquesResiduels) { this.risquesResiduels = risquesResiduels; return this; }
}
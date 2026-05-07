package com.example.SIGR.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "affectation")
public class Affectation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_affectation")
    private String id;

    /**
     * Plusieurs affectations peuvent concerner un agent
     */
    @ManyToOne
    @JoinColumn(name = "matricule_agent")
    private Agent agent;

    /**
     * Plusieurs affectations peuvent concerner
     * une unité administrative
     */
    @ManyToOne
    @JoinColumn(name = "id_unite")
    private UniteAdministrative unite;

    /**
     * Poste occupé dans l’unité
     */
    @Column(name = "poste", length = 100)
    private String poste;

    /**
     * Date de début de l’affectation
     */
    @Column(name = "date_affectation")
    private LocalDate dateAffectation;

    /**
     * Date de fin de l’affectation
     */
    @Column(name = "date_fin_affectation")
    private LocalDate dateFinAffectation;

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public Affectation setId(String id) {
        this.id = id;
        return this;
    }

    public Agent getAgent() {
        return agent;
    }

    public Affectation setAgent(Agent agent) {
        this.agent = agent;
        return this;
    }

    public UniteAdministrative getUnite() {
        return unite;
    }

    public Affectation setUnite(UniteAdministrative unite) {
        this.unite = unite;
        return this;
    }

    public String getPoste() {
        return poste;
    }

    public Affectation setPoste(String poste) {
        this.poste = poste;
        return this;
    }

    public LocalDate getDateAffectation() {
        return dateAffectation;
    }

    public Affectation setDateAffectation(LocalDate dateAffectation) {
        this.dateAffectation = dateAffectation;
        return this;
    }

    public LocalDate getDateFinAffectation() {
        return dateFinAffectation;
    }

    public Affectation setDateFinAffectation(LocalDate dateFinAffectation) {
        this.dateFinAffectation = dateFinAffectation;
        return this;
    }
}
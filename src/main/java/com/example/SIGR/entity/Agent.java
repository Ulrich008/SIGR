package com.example.SIGR.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "agent")
public class Agent {

    @Id
    @Column(name = "matricule_agent", length = 20)
    private String matricule;

    @Column(name = "numeronpi_agent", length = 20)
    private String npi;

    @Column(name = "nom_agent", length = 50)
    private String nom;

    @Column(name = "prenoms_agent", length = 100)
    private String prenoms;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe_agent", length = 10)
    private Sexe sexe;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_agent", length = 20)
    private Role role;

    @Column(name = "date_naissance")
    private String dateNaissance;

    @Column(name = "date_prise_service")
    private String datePriseService;

    /**
     * RELATION :
     * Plusieurs agents appartiennent à une unité administrative
     * => Many Agents → One UniteAdministrative
     */
    @ManyToOne
    @JoinColumn(name = "id_unite")
    private UniteAdministrative unite;

    /**
     * RELATION :
     * Un agent peut être responsable de plusieurs actions
     * => One Agent → Many Actions
     */
    @OneToMany(mappedBy = "responsable")
    private List<Action> actions;

    // ===================== GETTERS / SETTERS =====================

    public String getMatricule() {
        return matricule;
    }

    public Agent setMatricule(String matricule) {
        this.matricule = matricule;
        return this;
    }

    public String getNpi() {
        return npi;
    }

    public Agent setNpi(String npi) {
        this.npi = npi;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public Agent setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getPrenoms() {
        return prenoms;
    }

    public Agent setPrenoms(String prenoms) {
        this.prenoms = prenoms;
        return this;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public Agent setSexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public Agent setRole(Role role) {
        this.role = role;
        return this;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public Agent setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
        return this;
    }

    public String getDatePriseService() {
        return datePriseService;
    }

    public Agent setDatePriseService(String datePriseService) {
        this.datePriseService = datePriseService;
        return this;
    }

    public UniteAdministrative getUnite() {
        return unite;
    }

    public Agent setUnite(UniteAdministrative unite) {
        this.unite = unite;
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public Agent setActions(List<Action> actions) {
        this.actions = actions;
        return this;
    }
}
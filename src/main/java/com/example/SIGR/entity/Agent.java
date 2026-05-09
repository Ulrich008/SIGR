package com.example.SIGR.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "agent")
public class Agent {

    /**
     * ID technique (UUID)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_agent")
    private String id;

    /**
     * Matricule métier (unique)
     */
    @Column(name = "matricule_agent", length = 20, unique = true, nullable = false)
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
    private LocalDate dateNaissance;

    @Column(name = "date_prise_service")
    private LocalDate datePriseService;

    /**
     * Plusieurs agents appartiennent à une unité administrative
     */
    @ManyToOne
    @JoinColumn(name = "id_unite")
    private UniteAdministrative unite;

    /**
     * Un agent peut être responsable de plusieurs actions
     */
    @JsonIgnore
    @OneToMany(mappedBy = "responsable")
    private List<Action> actions;

    /**
     * Un agent peut avoir plusieurs affectations
     */
    @JsonIgnore
    @OneToMany(mappedBy = "agent")
    private List<Affectation> affectations;

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public Agent setId(String id) {
        this.id = id;
        return this;
    }

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

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public Agent setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
        return this;
    }

    public LocalDate getDatePriseService() {
        return datePriseService;
    }

    public Agent setDatePriseService(LocalDate datePriseService) {
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

    public List<Affectation> getAffectations() {
        return affectations;
    }

    public Agent setAffectations(List<Affectation> affectations) {
        this.affectations = affectations;
        return this;
    }
}
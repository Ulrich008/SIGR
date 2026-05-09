package com.example.SIGR.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "unite_administrative")
public class UniteAdministrative {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_unite", updatable = false, nullable = false, length = 50)
    private String id;

    @Column(name = "code_unite", length = 50, unique = true, nullable = false)
    private String code;

    @Column(name = "libelle_unite", length = 200)
    private String libelle;

    /**
     * Plusieurs unités administratives peuvent partager le même type d’unité
     * (ex : direction, service, division)
     */
    @ManyToOne
    @JoinColumn(name = "id_type_unite")
    private TypeUnite typeUnite;

    /**
     * Plusieurs unités administratives appartiennent à un même ministère
     */
    @ManyToOne
    @JoinColumn(name = "code_ministere")
    private Ministere ministere;

    /**
     * Relation hiérarchique (auto-référence) :
     * une unité peut avoir une unité parent
     */
    @ManyToOne
    @JoinColumn(name = "id_unite_parent")
    private UniteAdministrative parent;

    /**
     * Une unité administrative peut avoir plusieurs sous-unités (enfants)
     */
    @OneToMany(mappedBy = "parent")
    private List<UniteAdministrative> enfants;

    /**
     * Une unité administrative peut contenir plusieurs agents
     */
    @OneToMany(mappedBy = "unite")
    private List<Agent> agents;

    /**
     * Une unité administrative peut avoir plusieurs affectations d’agents
     */
    @OneToMany(mappedBy = "unite")
    private List<Affectation> affectations;

    @Column(name = "niveau_hierarchique")
    private Integer niveauHierarchique;

    // ===================== GETTERS / SETTERS =====================

    public String getId() {
        return id;
    }

    public UniteAdministrative setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public UniteAdministrative setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public UniteAdministrative setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public TypeUnite getTypeUnite() {
        return typeUnite;
    }

    public UniteAdministrative setTypeUnite(TypeUnite typeUnite) {
        this.typeUnite = typeUnite;
        return this;
    }

    public Ministere getMinistere() {
        return ministere;
    }

    public UniteAdministrative setMinistere(Ministere ministere) {
        this.ministere = ministere;
        return this;
    }

    public UniteAdministrative getParent() {
        return parent;
    }

    public UniteAdministrative setParent(UniteAdministrative parent) {
        this.parent = parent;
        return this;
    }

    public List<UniteAdministrative> getEnfants() {
        return enfants;
    }

    public UniteAdministrative setEnfants(List<UniteAdministrative> enfants) {
        this.enfants = enfants;
        return this;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public UniteAdministrative setAgents(List<Agent> agents) {
        this.agents = agents;
        return this;
    }

    public List<Affectation> getAffectations() {
        return affectations;
    }

    public UniteAdministrative setAffectations(List<Affectation> affectations) {
        this.affectations = affectations;
        return this;
    }

    public Integer getNiveauHierarchique() {
        return niveauHierarchique;
    }

    public UniteAdministrative setNiveauHierarchique(Integer niveauHierarchique) {
        this.niveauHierarchique = niveauHierarchique;
        return this;
    }
}
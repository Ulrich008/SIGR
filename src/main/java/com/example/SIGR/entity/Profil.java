package com.example.SIGR.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "profil")
public class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_profil", length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;

    @Column(name = "libelle", length = 100, nullable = false, unique = true)
    private String libelle;

    @Column(name = "description", length = 500)
    private String description;

    /**
     * Relation avec les agents
     */
    @OneToMany(mappedBy = "profil")
    private List<Agent> agents;

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public Profil setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Profil setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public Profil setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public Profil setAgents(List<Agent> agents) {
        this.agents = agents;
        return this;
    }
}
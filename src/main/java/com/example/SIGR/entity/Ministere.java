package com.example.SIGR.entity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ministere")
public class Ministere {

    @Id
    @Column(name = "code_ministere", length = 50)
    private String code;

    @Column(name = "nom_ministere", length = 200)
    private String nom;

    @Column(name = "sigle_ministere", length = 20)
    private String sigle;

    @Column(length = 500)
    private String description;

    @Column(name = "cree_par", length = 50)
    private String creePar;

    @OneToMany(mappedBy = "ministere")
    private List<UniteAdministrative> unites;

    public String getCode() {
        return code;
    }

    public Ministere setCode(String code) {
        this.code = code;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public Ministere setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getSigle() {
        return sigle;
    }

    public Ministere setSigle(String sigle) {
        this.sigle = sigle;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Ministere setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCreePar() {
        return creePar;
    }

    public Ministere setCreePar(String creePar) {
        this.creePar = creePar;
        return this;
    }

    public List<UniteAdministrative> getUnites() {
        return unites;
    }

    public Ministere setUnites(List<UniteAdministrative> unites) {
        this.unites = unites;
        return this;
    }
}
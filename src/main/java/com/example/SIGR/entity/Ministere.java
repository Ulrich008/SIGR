package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ministere")
@Audited
public class Ministere extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_ministere", updatable = false, nullable = false)
    private String id;

    @Column(name = "code_ministere", length = 50, unique = true, nullable = false)
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


    public String getId() {
        return id;
    }

    public Ministere setId(String id) {
        this.id = id;
        return this;
    }

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
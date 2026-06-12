package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "type_unite")
@Audited
public class TypeUnite extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_type_unite", length = 36, updatable = false, nullable = false)
    private String id;

    @Column(name = "code", length = 30, unique = true, nullable = false)
    private String code;

    @Column(name = "libelle_type", length = 100)
    private String libelle;

    @Column(length = 300)
    private String description;

    @Column(name = "cree_par", length = 255)
    private String creePar;

    @OneToMany(mappedBy = "typeUnite")
    private List<UniteAdministrative> unites;

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public TypeUnite setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public TypeUnite setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public TypeUnite setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TypeUnite setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCreePar() {
        return creePar;
    }

    public TypeUnite setCreePar(String creePar) {
        this.creePar = creePar;
        return this;
    }

    public List<UniteAdministrative> getUnites() {
        return unites;
    }

    public TypeUnite setUnites(List<UniteAdministrative> unites) {
        this.unites = unites;
        return this;
    }
}
package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "unite_mesure")
@Audited
public class UniteMesure extends Auditable {

    public enum TypeUnite {
        NUMERIQUE,
        DATE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_unite_mesure", updatable = false, nullable = false)
    private String id;

    @Column(name = "code_unite_mesure", length = 20, unique = true, nullable = false)
    private String code;

    @Column(name = "libelle_unite_mesure", length = 200, nullable = false)
    private String libelle;

    @Column(name = "symbole", length = 10)
    private String symbole;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_unite", length = 20, nullable = false)
    private TypeUnite typeUnite = TypeUnite.NUMERIQUE;

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public UniteMesure setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public UniteMesure setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public UniteMesure setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getSymbole() {
        return symbole;
    }

    public UniteMesure setSymbole(String symbole) {
        this.symbole = symbole;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UniteMesure setDescription(String description) {
        this.description = description;
        return this;
    }

    public TypeUnite getTypeUnite() {
        return typeUnite;
    }

    public UniteMesure setTypeUnite(TypeUnite typeUnite) {
        this.typeUnite = typeUnite;
        return this;
    }
}

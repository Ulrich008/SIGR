package com.example.SIGR.entity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "processus")
public class Processus {

    @Id
    @Column(name = "code_processus", length = 20)
    private String code;

    @Column(name = "libelle_processus", length = 200)
    private String libelle;

    @Column(length = 500)
    private String finalite;

    @Column(name = "type_processus", length = 50)
    private String typeProcessus;

    @ManyToOne
    @JoinColumn(name = "id_unite", nullable = false)
    private UniteAdministrative unite;

    @ManyToOne
    @JoinColumn(name = "proprietaire_processus")
    private Agent proprietaire;

    @OneToMany(mappedBy = "processus")
    private List<IndicateurPerformance> indicateurs;

    public String getCode() {
        return code;
    }

    public Processus setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public Processus setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getFinalite() {
        return finalite;
    }

    public Processus setFinalite(String finalite) {
        this.finalite = finalite;
        return this;
    }

    public String getTypeProcessus() {
        return typeProcessus;
    }

    public Processus setTypeProcessus(String typeProcessus) {
        this.typeProcessus = typeProcessus;
        return this;
    }

    public UniteAdministrative getUnite() {
        return unite;
    }

    public Processus setUnite(UniteAdministrative unite) {
        this.unite = unite;
        return this;
    }

    public Agent getProprietaire() {
        return proprietaire;
    }

    public Processus setProprietaire(Agent proprietaire) {
        this.proprietaire = proprietaire;
        return this;
    }

    public List<IndicateurPerformance> getIndicateurs() {
        return indicateurs;
    }

    public Processus setIndicateurs(List<IndicateurPerformance> indicateurs) {
        this.indicateurs = indicateurs;
        return this;
    }
}

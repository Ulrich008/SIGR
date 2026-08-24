package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
import java.util.List;

@Entity
@Table(name = "processus")
@Audited
@FilterDef(name = "ministereFilter", parameters = @ParamDef(name = "codeMinistere", type = String.class))
@Filter(name = "ministereFilter", condition = "id_unite IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_ministere = :codeMinistere)")
// Cantonnement du Correspondant Risque à sa propre unité administrative
// (voir MinistereInterceptor) : filtre additionnel, actif seulement pour ce
// profil — sans effet pour les autres tant qu'il n'est pas explicitement
// activé sur la session.
@FilterDef(name = "uaFilter", parameters = @ParamDef(name = "codeUnite", type = String.class))
@Filter(name = "uaFilter", condition = "id_unite IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_unite = :codeUnite)")
public class Processus extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_processus", updatable = false, nullable = false)
    private String id;

    @Column(name = "code_processus", length = 20, unique = true, nullable = false)
    private String code;

    @Column(name = "libelle_processus", length = 200)
    private String libelle;

    @Column(length = 500)
    private String finalite;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_processus", length = 50)
    private TypeProcessus typeProcessus;

    /**
     * Plusieurs processus peuvent appartenir à une même unité.
     */
    @ManyToOne
    @JoinColumn(name = "id_unite", nullable = false)
    private UniteAdministrative unite;

    /*
     * Plusieurs processus peuvent être gérés par le même agent.
     */
    @ManyToOne
    @JoinColumn(name = "proprietaire_processus", referencedColumnName = "matricule_agent")
    private Agent proprietaire;

    /*
     * Un processus peut avoir plusieurs indicateurs associés.
     */
    @OneToMany(mappedBy = "processus")
    private List<IndicateurPerformance> indicateurs;

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public Processus setId(String id) {
        this.id = id;
        return this;
    }

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

    public TypeProcessus getTypeProcessus() {
        return typeProcessus;
    }

    public Processus setTypeProcessus(TypeProcessus typeProcessus) {
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
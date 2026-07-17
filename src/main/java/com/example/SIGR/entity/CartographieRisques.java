package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cartographie_risques")
@Audited
@FilterDef(name = "ministereFilter", parameters = @ParamDef(name = "codeMinistere", type = String.class))
@Filter(name = "ministereFilter", condition = "id_unite_administrative IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_ministere = :codeMinistere)")
public class CartographieRisques extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_cartographie", updatable = false, nullable = false)
    private String id;

    // NOUVEAU CHAMP CODE
    @Column(name = "code", length = 50, unique = true, nullable = false)
    private String code;

    /**
     * Unité administrative rattachée à ce projet de cartographie.
     * Sert aussi à générer le code (CR_&lt;sigle UA&gt;NNN) et à
     * filtrer par ministère : contrairement aux risques qu'elle
     * regroupe, une cartographie peut exister avant qu'aucun risque
     * n'y soit encore rattaché.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unite_administrative", nullable = false)
    private UniteAdministrative uniteAdministrative;

    @Column(name = "titre", length = 200)
    private String titre;

    @Column(name = "periode")
    private LocalDate periode;

    @Column(name = "seuil_faible")
    private Integer seuilFaible;

    @Column(name = "seuil_moyen")
    private Integer seuilMoyen;

    @Column(name = "seuil_eleve")
    private Integer seuilEleve;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", length = 30)
    private StatutCartographie statut;

    @OneToMany(mappedBy = "cartographie")
    private List<Risque> risques;

    // ===================== GETTERS / SETTERS =====================

    public String getId() {
        return id;
    }

    public CartographieRisques setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public CartographieRisques setCode(String code) {
        this.code = code;
        return this;
    }

    public UniteAdministrative getUniteAdministrative() {
        return uniteAdministrative;
    }

    public CartographieRisques setUniteAdministrative(UniteAdministrative uniteAdministrative) {
        this.uniteAdministrative = uniteAdministrative;
        return this;
    }

    public String getTitre() {
        return titre;
    }

    public CartographieRisques setTitre(String titre) {
        this.titre = titre;
        return this;
    }

    public LocalDate getPeriode() {
        return periode;
    }

    public CartographieRisques setPeriode(LocalDate periode) {
        this.periode = periode;
        return this;
    }

    public Integer getSeuilFaible() {
        return seuilFaible;
    }

    public CartographieRisques setSeuilFaible(Integer seuilFaible) {
        this.seuilFaible = seuilFaible;
        return this;
    }

    public Integer getSeuilMoyen() {
        return seuilMoyen;
    }

    public CartographieRisques setSeuilMoyen(Integer seuilMoyen) {
        this.seuilMoyen = seuilMoyen;
        return this;
    }

    public Integer getSeuilEleve() {
        return seuilEleve;
    }

    public CartographieRisques setSeuilEleve(Integer seuilEleve) {
        this.seuilEleve = seuilEleve;
        return this;
    }

    public StatutCartographie getStatut() {
        return statut;
    }

    public CartographieRisques setStatut(StatutCartographie statut) {
        this.statut = statut;
        return this;
    }

    public List<Risque> getRisques() {
        return risques;
    }

    public CartographieRisques setRisques(List<Risque> risques) {
        this.risques = risques;
        return this;
    }
}
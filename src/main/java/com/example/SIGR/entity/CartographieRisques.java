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
@Filter(name = "ministereFilter", condition = "id_cartographie IN (SELECT id_cartographie FROM risque WHERE code_processus IN (SELECT code FROM processus WHERE id_unite IN (SELECT id_unite FROM unite_administrative WHERE code_ministere = :codeMinistere)))")
public class CartographieRisques extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_cartographie", updatable = false, nullable = false)
    private String id;

    // NOUVEAU CHAMP CODE
    @Column(name = "code", length = 50, unique = true, nullable = false)
    private String code;

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
package com.example.SIGR.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "cartographie_risques")
public class CartographieRisques {

    @Id
    @Column(name = "id_cartographie", length = 50)
    private String id;

    @Column(name = "titre", length = 200)
    private String titre;

    @Column(name = "periode", length = 50)
    private String periode;

    @Column(name = "seuil_faible", length = 2)
    private String seuilFaible;

    @Column(name = "seuil_moyen", length = 2)
    private String seuilMoyen;

    @Column(name = "seuil_eleve", length = 2)
    private String seuilEleve;

    @Column(name = "statut", length = 30)
    private String statut;

    /**
     * RELATION :
     * Une cartographie peut contenir plusieurs risques
     * => One CartographieRisques → Many Risques
     */
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

    public String getTitre() {
        return titre;
    }

    public CartographieRisques setTitre(String titre) {
        this.titre = titre;
        return this;
    }

    public String getPeriode() {
        return periode;
    }

    public CartographieRisques setPeriode(String periode) {
        this.periode = periode;
        return this;
    }

    public String getSeuilFaible() {
        return seuilFaible;
    }

    public CartographieRisques setSeuilFaible(String seuilFaible) {
        this.seuilFaible = seuilFaible;
        return this;
    }

    public String getSeuilMoyen() {
        return seuilMoyen;
    }

    public CartographieRisques setSeuilMoyen(String seuilMoyen) {
        this.seuilMoyen = seuilMoyen;
        return this;
    }

    public String getSeuilEleve() {
        return seuilEleve;
    }

    public CartographieRisques setSeuilEleve(String seuilEleve) {
        this.seuilEleve = seuilEleve;
        return this;
    }

    public String getStatut() {
        return statut;
    }

    public CartographieRisques setStatut(String statut) {
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
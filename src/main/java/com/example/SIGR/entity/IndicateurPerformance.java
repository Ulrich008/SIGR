package com.example.SIGR.entity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "indicateur_performance")
public class IndicateurPerformance {

    @Id
    @Column(name = "code_indicateur", length = 10)
    private String code;

    @Column(length = 200)
    private String libelle;

    @Column(name = "unite_mesure", length = 50)
    private String uniteMesure;

    @Column(length = 30)
    private String frequence;

    @Column(name = "valeur_cible")
    private Double valeurCible;

    @Column(name = "valeur_obtenue")
    private Double valeurObtenue;

    @Column(name = "seuil_alerte")
    private Double seuilAlerte;

    @Column(name = "date_mesure")
    private LocalDate dateMesure;

    @ManyToOne
    @JoinColumn(name = "code_processus", nullable = false)
    private Processus processus;

    // ⚡ Champ calculé (non persisté)
    @Transient
    public Double getEcartCible() {
        if (valeurCible == null || valeurObtenue == null) return null;
        return valeurObtenue - valeurCible;
    }

    // ⚡ Statut simple pour mémoire
    @Transient
    public String getStatut() {
        if (valeurObtenue == null || seuilAlerte == null) return "INCONNU";

        return valeurObtenue >= seuilAlerte ? "ALERTE" : "OK";
    }

    public String getCode() {
        return code;
    }

    public IndicateurPerformance setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public IndicateurPerformance setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getUniteMesure() {
        return uniteMesure;
    }

    public IndicateurPerformance setUniteMesure(String uniteMesure) {
        this.uniteMesure = uniteMesure;
        return this;
    }

    public String getFrequence() {
        return frequence;
    }

    public IndicateurPerformance setFrequence(String frequence) {
        this.frequence = frequence;
        return this;
    }

    public Double getValeurCible() {
        return valeurCible;
    }

    public IndicateurPerformance setValeurCible(Double valeurCible) {
        this.valeurCible = valeurCible;
        return this;
    }

    public Double getValeurObtenue() {
        return valeurObtenue;
    }

    public IndicateurPerformance setValeurObtenue(Double valeurObtenue) {
        this.valeurObtenue = valeurObtenue;
        return this;
    }

    public Double getSeuilAlerte() {
        return seuilAlerte;
    }

    public IndicateurPerformance setSeuilAlerte(Double seuilAlerte) {
        this.seuilAlerte = seuilAlerte;
        return this;
    }

    public LocalDate getDateMesure() {
        return dateMesure;
    }

    public IndicateurPerformance setDateMesure(LocalDate dateMesure) {
        this.dateMesure = dateMesure;
        return this;
    }

    public Processus getProcessus() {
        return processus;
    }

    public IndicateurPerformance setProcessus(Processus processus) {
        this.processus = processus;
        return this;
    }
}

package com.example.SIGR.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "indicateur_performance")
public class IndicateurPerformance {

    // 🔥 ID technique auto-généré
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_indicateur", updatable = false, nullable = false)
    private String id;

    // 🔥 Code métier (unique)
    @Column(name = "code_indicateur", length = 10, unique = true, nullable = false)
    private String code;

    @Column(length = 200)
    private String libelle;

    @Column(name = "unite_mesure", length = 50)
    private String uniteMesure = "%";

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Frequence frequence;

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

    // ===================== CALCULS =====================

    @Transient
    public Double getEcartCible() {
        if (valeurCible == null || valeurObtenue == null) return null;
        return valeurObtenue - valeurCible;
    }

    @Transient
    public String getStatut() {
        if (valeurObtenue == null || seuilAlerte == null) return "INCONNU";
        return valeurObtenue >= seuilAlerte ? "ALERTE" : "OK";
    }

    // ===================== GETTERS / SETTERS =====================

    public String getId() {
        return id;
    }

    public IndicateurPerformance setId(String id) {
        this.id = id;
        return this;
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

    public Frequence getFrequence() {
        return frequence;
    }

    public IndicateurPerformance setFrequence(Frequence frequence) {
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
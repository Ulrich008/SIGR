package com.example.SIGR.dto.response;

import com.example.SIGR.entity.Frequence;

import java.time.LocalDate;

public class IndicateurPerformanceResponse {

    private String id;

    private String code;

    private String libelle;

    /**
     * Valeur automatiquement fixée à %
     */
    private String uniteMesure;

    /**
     * Fréquence de mesure
     */
    private Frequence frequence;

    private Double valeurCible;

    private Double valeurObtenue;

    private Double seuilAlerte;

    private LocalDate dateMesure;

    private String codeProcessus;

    private String nomProcessus;

    /**
     * Valeur calculée automatiquement
     */
    private Double ecartCible;

    /**
     * Statut calculé automatiquement
     * OK / ALERTE / INCONNU
     */
    private String statut;

    public IndicateurPerformanceResponse(
            String id,
            String code,
            String libelle,
            String uniteMesure,
            Frequence frequence,
            Double valeurCible,
            Double valeurObtenue,
            Double seuilAlerte,
            LocalDate dateMesure,
            String codeProcessus,
            String nomProcessus,
            Double ecartCible,
            String statut
    ) {

        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.uniteMesure = uniteMesure;
        this.frequence = frequence;
        this.valeurCible = valeurCible;
        this.valeurObtenue = valeurObtenue;
        this.seuilAlerte = seuilAlerte;
        this.dateMesure = dateMesure;
        this.codeProcessus = codeProcessus;
        this.nomProcessus = nomProcessus;
        this.ecartCible = ecartCible;
        this.statut = statut;
    }

    // ================= GETTERS =================

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getUniteMesure() {
        return uniteMesure;
    }

    public Frequence getFrequence() {
        return frequence;
    }

    public Double getValeurCible() {
        return valeurCible;
    }

    public Double getValeurObtenue() {
        return valeurObtenue;
    }

    public Double getSeuilAlerte() {
        return seuilAlerte;
    }

    public LocalDate getDateMesure() {
        return dateMesure;
    }

    public String getCodeProcessus() {
        return codeProcessus;
    }

    public String getNomProcessus() {
        return nomProcessus;
    }

    public Double getEcartCible() {
        return ecartCible;
    }

    public String getStatut() {
        return statut;
    }
}
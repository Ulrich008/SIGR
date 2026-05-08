package com.example.SIGR.dto.response;

import java.time.LocalDate;

public class IndicateurPerformanceResponse {

    private String code;
    private String libelle;
    private String uniteMesure;
    private String frequence;

    private Double valeurCible;
    private Double valeurObtenue;
    private Double seuilAlerte;

    private LocalDate dateMesure;

    private String codeProcessus;
    private String nomProcessus;

    private Double ecartCible;
    private String statut;

    public IndicateurPerformanceResponse(
            String code,
            String libelle,
            String uniteMesure,
            String frequence,
            Double valeurCible,
            Double valeurObtenue,
            Double seuilAlerte,
            LocalDate dateMesure,
            String codeProcessus,
            String nomProcessus,
            Double ecartCible,
            String statut
    ) {
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

    // GETTERS

    public String getCode() { return code; }

    public String getLibelle() { return libelle; }

    public String getUniteMesure() { return uniteMesure; }

    public String getFrequence() { return frequence; }

    public Double getValeurCible() { return valeurCible; }

    public Double getValeurObtenue() { return valeurObtenue; }

    public Double getSeuilAlerte() { return seuilAlerte; }

    public LocalDate getDateMesure() { return dateMesure; }

    public String getCodeProcessus() { return codeProcessus; }

    public String getNomProcessus() { return nomProcessus; }

    public Double getEcartCible() { return ecartCible; }

    public String getStatut() { return statut; }
}
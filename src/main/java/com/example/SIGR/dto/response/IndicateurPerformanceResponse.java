package com.example.SIGR.dto.response;

import com.example.SIGR.entity.Frequence;

import java.time.LocalDate;

public class IndicateurPerformanceResponse {

    private String id;

    private String code;

    private String libelle;

    /**
     * Code de l'unité de mesure
     */
    private String codeUniteMesure;

    /**
     * Libellé de l'unité de mesure
     */
    private String libelleUniteMesure;

    /**
     * Type de l'unité de mesure (NUMERIQUE ou DATE)
     */
    private String typeUniteMesure;

    /**
     * Fréquence de mesure
     */
    private Frequence frequence;

    private String valeurCible; // Peut être un nombre ou une date (string)

    private String valeurObtenue; // Peut être un nombre ou une date (string)

    private LocalDate seuilAlerte;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private String codeProcessus;

    private String nomProcessus;

    private String codeRisque;

    private String libelleRisque;

    private String codePlanMitigation;

    private String libellePlanMitigation;

    private String codeAction;

    private String libelleAction;

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
            String codeUniteMesure,
            String libelleUniteMesure,
            String typeUniteMesure,
            Frequence frequence,
            String valeurCible,
            String valeurObtenue,
            LocalDate seuilAlerte,
            LocalDate dateDebut,
            LocalDate dateFin,
            String codeProcessus,
            String nomProcessus,
            String codeRisque,
            String libelleRisque,
            String codePlanMitigation,
            String libellePlanMitigation,
            String codeAction,
            String libelleAction,
            Double ecartCible,
            String statut
    ) {

        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.codeUniteMesure = codeUniteMesure;
        this.libelleUniteMesure = libelleUniteMesure;
        this.typeUniteMesure = typeUniteMesure;
        this.frequence = frequence;
        this.valeurCible = valeurCible;
        this.valeurObtenue = valeurObtenue;
        this.seuilAlerte = seuilAlerte;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.codeProcessus = codeProcessus;
        this.nomProcessus = nomProcessus;
        this.codeRisque = codeRisque;
        this.libelleRisque = libelleRisque;
        this.codePlanMitigation = codePlanMitigation;
        this.libellePlanMitigation = libellePlanMitigation;
        this.codeAction = codeAction;
        this.libelleAction = libelleAction;
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

    public String getCodeUniteMesure() {
        return codeUniteMesure;
    }

    public String getLibelleUniteMesure() {
        return libelleUniteMesure;
    }

    public String getTypeUniteMesure() {
        return typeUniteMesure;
    }

    public Frequence getFrequence() {
        return frequence;
    }

    public String getValeurCible() {
        return valeurCible;
    }

    public String getValeurObtenue() {
        return valeurObtenue;
    }

    public LocalDate getSeuilAlerte() {
        return seuilAlerte;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public String getCodeProcessus() {
        return codeProcessus;
    }

    public String getNomProcessus() {
        return nomProcessus;
    }

    public String getCodeRisque() {
        return codeRisque;
    }

    public String getLibelleRisque() {
        return libelleRisque;
    }

    public String getCodePlanMitigation() {
        return codePlanMitigation;
    }

    public String getLibellePlanMitigation() {
        return libellePlanMitigation;
    }

    public String getCodeAction() {
        return codeAction;
    }

    public String getLibelleAction() {
        return libelleAction;
    }

    public Double getEcartCible() {
        return ecartCible;
    }

    public String getStatut() {
        return statut;
    }
}
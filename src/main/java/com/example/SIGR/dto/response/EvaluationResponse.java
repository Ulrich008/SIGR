package com.example.SIGR.dto.response;

import com.example.SIGR.entity.StrategieRisque;

import java.time.LocalDate;

public class EvaluationResponse {

    private String id;
    private String code;

    // ================= RISQUE INHERENT =================

    private Integer impactInherent;
    private Integer probabiliteInherente;
    private Integer scoreInherent;

    // ================= MAITRISE DU RISQUE =================

    private Integer protection;
    private Integer prevention;

    // ================= CONTROLES =================

    private String controleExistants;
    private String controleInexistants;
    private Boolean dejaSurvenu;

    // ================= RISQUE RESIDUEL =================

    private Integer impactResiduel;
    private Integer probabiliteResiduelle;
    private Integer scoreResiduel;

    // ================= PRIORISATION =================

    private Integer rangPriorite;
    private String libellePriorite;

    // ================= STRATEGIE RISQUE =================

    private StrategieRisque strategieRisque;
    private String strategieRisqueDescription;

    // ================= INFORMATIONS =================

    private LocalDate dateDebut;
    private LocalDate dateFin;

    private String recommandation;
    private String bonnesPratiques;

    // ================= RISQUE =================

    private String idRisque;
    private String libelleRisque;

    // ================= AGENT =================

    private String matriculeAgent;
    private String nomAgent;

    public EvaluationResponse(
            String id,
            String code,

            Integer impactInherent,
            Integer probabiliteInherente,
            Integer scoreInherent,

            Integer protection,
            Integer prevention,

            String controleExistants,
            String controleInexistants,
            Boolean dejaSurvenu,

            Integer impactResiduel,
            Integer probabiliteResiduelle,
            Integer scoreResiduel,

            Integer rangPriorite,
            String libellePriorite,

            StrategieRisque strategieRisque,
            String strategieRisqueDescription,

            LocalDate dateDebut,
            LocalDate dateFin,

            String recommandation,
            String bonnesPratiques,

            String idRisque,
            String libelleRisque,

            String matriculeAgent,
            String nomAgent
    ) {

        this.id = id;
        this.code = code;

        this.impactInherent = impactInherent;
        this.probabiliteInherente = probabiliteInherente;
        this.scoreInherent = scoreInherent;

        this.protection = protection;
        this.prevention = prevention;

        this.controleExistants = controleExistants;
        this.controleInexistants = controleInexistants;
        this.dejaSurvenu = dejaSurvenu;

        this.impactResiduel = impactResiduel;
        this.probabiliteResiduelle = probabiliteResiduelle;
        this.scoreResiduel = scoreResiduel;

        this.rangPriorite = rangPriorite;
        this.libellePriorite = libellePriorite;

        this.strategieRisque = strategieRisque;
        this.strategieRisqueDescription = strategieRisqueDescription;

        this.dateDebut = dateDebut;
        this.dateFin = dateFin;

        this.recommandation = recommandation;
        this.bonnesPratiques = bonnesPratiques;

        this.idRisque = idRisque;
        this.libelleRisque = libelleRisque;

        this.matriculeAgent = matriculeAgent;
        this.nomAgent = nomAgent;
    }

    // ================= GETTERS =================

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Integer getImpactInherent() {
        return impactInherent;
    }

    public Integer getProbabiliteInherente() {
        return probabiliteInherente;
    }

    public Integer getScoreInherent() {
        return scoreInherent;
    }

    public Integer getProtection() {
        return protection;
    }

    public Integer getPrevention() {
        return prevention;
    }

    public String getControleExistants() {
        return controleExistants;
    }

    public String getControleInexistants() {
        return controleInexistants;
    }

    public Boolean getDejaSurvenu() {
        return dejaSurvenu;
    }

    public Integer getImpactResiduel() {
        return impactResiduel;
    }

    public Integer getProbabiliteResiduelle() {
        return probabiliteResiduelle;
    }

    public Integer getScoreResiduel() {
        return scoreResiduel;
    }

    public Integer getRangPriorite() {
        return rangPriorite;
    }

    public String getLibellePriorite() {
        return libellePriorite;
    }

    public StrategieRisque getStrategieRisque() {
        return strategieRisque;
    }

    public String getStrategieRisqueDescription() {
        return strategieRisqueDescription;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public String getRecommandation() {
        return recommandation;
    }

    public String getBonnesPratiques() {
        return bonnesPratiques;
    }

    public String getIdRisque() {
        return idRisque;
    }

    public String getLibelleRisque() {
        return libelleRisque;
    }

    public String getMatriculeAgent() {
        return matriculeAgent;
    }

    public String getNomAgent() {
        return nomAgent;
    }
}
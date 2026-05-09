package com.example.SIGR.dto.response;

public class RisqueResiduelResponse {

    private String id;
    private String code;

    private Integer impactResiduel;
    private Integer probabiliteResiduelle;
    private Integer scoreResiduel;
    private String niveauRisque;

    private String idEvaluation;
    private String idRisque;
    private String libelleRisque;

    public RisqueResiduelResponse(
            String id,
            String code,
            Integer impactResiduel,
            Integer probabiliteResiduelle,
            Integer scoreResiduel,
            String niveauRisque,
            String idEvaluation,
            String idRisque,
            String libelleRisque
    ) {
        this.id = id;
        this.code = code;
        this.impactResiduel = impactResiduel;
        this.probabiliteResiduelle = probabiliteResiduelle;
        this.scoreResiduel = scoreResiduel;
        this.niveauRisque = niveauRisque;
        this.idEvaluation = idEvaluation;
        this.idRisque = idRisque;
        this.libelleRisque = libelleRisque;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
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

    public String getNiveauRisque() {
        return niveauRisque;
    }

    public String getIdEvaluation() {
        return idEvaluation;
    }

    public String getIdRisque() {
        return idRisque;
    }

    public String getLibelleRisque() {
        return libelleRisque;
    }
}
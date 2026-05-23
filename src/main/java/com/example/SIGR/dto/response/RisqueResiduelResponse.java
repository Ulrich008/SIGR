package com.example.SIGR.dto.response;

public class RisqueResiduelResponse {

    private String id;

    private String code;

    private Integer impactResiduel;

    private Integer probabiliteResiduelle;

    private Integer scoreResiduel;

    private String niveauRisque;

    /**
     * ================= EVALUATION =================
     */
    private String codeEvaluation;

    /**
     * ================= RISQUE =================
     */
    private String codeRisque;

    private String libelleRisque;

    public RisqueResiduelResponse(
            String id,
            String code,
            Integer impactResiduel,
            Integer probabiliteResiduelle,
            Integer scoreResiduel,
            String niveauRisque,
            String codeEvaluation,
            String codeRisque,
            String libelleRisque
    ) {

        this.id = id;

        this.code = code;

        this.impactResiduel = impactResiduel;

        this.probabiliteResiduelle = probabiliteResiduelle;

        this.scoreResiduel = scoreResiduel;

        this.niveauRisque = niveauRisque;

        this.codeEvaluation = codeEvaluation;

        this.codeRisque = codeRisque;

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

    public String getCodeEvaluation() {
        return codeEvaluation;
    }

    public String getCodeRisque() {
        return codeRisque;
    }

    public String getLibelleRisque() {
        return libelleRisque;
    }
}
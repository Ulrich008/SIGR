package com.example.SIGR.dto.response;

import java.time.LocalDate;

public class EvaluationResponse {

    private String id;
    private Integer impact;
    private Integer probabilite;
    private LocalDate dateEvaluation;
    private String bonnesPratiques;
    private Integer niveauControle;

    private Integer scoreInitial;

    private String idRisque;
    private String libelleRisque;

    private String idAgent;
    private String nomAgent;

    public EvaluationResponse(
            String id,
            Integer impact,
            Integer probabilite,
            LocalDate dateEvaluation,
            String bonnesPratiques,
            Integer niveauControle,
            Integer scoreInitial,
            String idRisque,
            String libelleRisque,
            String idAgent,
            String nomAgent
    ) {
        this.id = id;
        this.impact = impact;
        this.probabilite = probabilite;
        this.dateEvaluation = dateEvaluation;
        this.bonnesPratiques = bonnesPratiques;
        this.niveauControle = niveauControle;
        this.scoreInitial = scoreInitial;
        this.idRisque = idRisque;
        this.libelleRisque = libelleRisque;
        this.idAgent = idAgent;
        this.nomAgent = nomAgent;
    }

    public String getId() { return id; }

    public Integer getImpact() { return impact; }

    public Integer getProbabilite() { return probabilite; }

    public LocalDate getDateEvaluation() { return dateEvaluation; }

    public String getBonnesPratiques() { return bonnesPratiques; }

    public Integer getNiveauControle() { return niveauControle; }

    public Integer getScoreInitial() { return scoreInitial; }

    public String getIdRisque() { return idRisque; }

    public String getLibelleRisque() { return libelleRisque; }

    public String getIdAgent() { return idAgent; }

    public String getNomAgent() { return nomAgent; }
}
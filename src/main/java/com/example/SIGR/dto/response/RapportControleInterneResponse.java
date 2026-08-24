package com.example.SIGR.dto.response;

import com.example.SIGR.entity.StatutRapportCI;
import com.example.SIGR.entity.StatutSuiviRecommandation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RapportControleInterneResponse {

    private String id;
    private String code;

    private String codeUniteAdministrative;
    private String libelleUniteAdministrative;
    private String codeProcessus;
    private String libelleProcessus;

    private LocalDate dateEmission;

    private String preambule;
    private List<ActionCorrectiveResponse> actionsCorrectives;
    private String conclusion;

    private StatutRapportCI statut;
    private String motif;

    private boolean pdfDisponible;
    private LocalDateTime pdfGenereLe;

    private String creePar;

    private StatutSuiviRecommandation statutSuivi;
    private String decisionSuivi;
    private LocalDateTime dateDecisionSuivi;

    public RapportControleInterneResponse(
            String id,
            String code,
            String codeUniteAdministrative,
            String libelleUniteAdministrative,
            String codeProcessus,
            String libelleProcessus,
            LocalDate dateEmission,
            String preambule,
            List<ActionCorrectiveResponse> actionsCorrectives,
            String conclusion,
            StatutRapportCI statut,
            String motif,
            boolean pdfDisponible,
            LocalDateTime pdfGenereLe,
            String creePar,
            StatutSuiviRecommandation statutSuivi,
            String decisionSuivi,
            LocalDateTime dateDecisionSuivi
    ) {
        this.id = id;
        this.code = code;
        this.codeUniteAdministrative = codeUniteAdministrative;
        this.libelleUniteAdministrative = libelleUniteAdministrative;
        this.codeProcessus = codeProcessus;
        this.libelleProcessus = libelleProcessus;
        this.dateEmission = dateEmission;
        this.preambule = preambule;
        this.actionsCorrectives = actionsCorrectives;
        this.conclusion = conclusion;
        this.statut = statut;
        this.motif = motif;
        this.pdfDisponible = pdfDisponible;
        this.pdfGenereLe = pdfGenereLe;
        this.creePar = creePar;
        this.statutSuivi = statutSuivi;
        this.decisionSuivi = decisionSuivi;
        this.dateDecisionSuivi = dateDecisionSuivi;
    }

    public String getId() { return id; }
    public String getCode() { return code; }
    public String getCodeUniteAdministrative() { return codeUniteAdministrative; }
    public String getLibelleUniteAdministrative() { return libelleUniteAdministrative; }
    public String getCodeProcessus() { return codeProcessus; }
    public String getLibelleProcessus() { return libelleProcessus; }
    public LocalDate getDateEmission() { return dateEmission; }
    public String getPreambule() { return preambule; }
    public List<ActionCorrectiveResponse> getActionsCorrectives() { return actionsCorrectives; }
    public String getConclusion() { return conclusion; }
    public StatutRapportCI getStatut() { return statut; }
    public String getMotif() { return motif; }
    public boolean isPdfDisponible() { return pdfDisponible; }
    public LocalDateTime getPdfGenereLe() { return pdfGenereLe; }
    public String getCreePar() { return creePar; }
    public StatutSuiviRecommandation getStatutSuivi() { return statutSuivi; }
    public String getDecisionSuivi() { return decisionSuivi; }
    public LocalDateTime getDateDecisionSuivi() { return dateDecisionSuivi; }
}

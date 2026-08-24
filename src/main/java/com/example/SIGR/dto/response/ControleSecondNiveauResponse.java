package com.example.SIGR.dto.response;

import java.time.LocalDate;

public class ControleSecondNiveauResponse {

    private String id;
    private String code;

    private String codeUniteAdministrative;
    private String libelleUniteAdministrative;
    private String codeProcessus;
    private String libelleProcessus;

    private LocalDate dateControle;

    private String testsLibelle;
    private String testsConstats;
    private String testsAnalyse;
    private String testsRecommandation;

    private String revuesLibelle;
    private String revuesConstats;
    private String revuesAnalyse;
    private String revuesRecommandation;

    private String verificationLibelleDesPieces;
    private String verificationConstats;
    private String verificationAnalyse;
    private String verificationRecommandation;

    private String evolutionIntituleOperation;
    private String evolutionProceduresInternesRenforcements;
    private String evolutionResultatsConformite;
    private String evolutionAnalyse;
    private String evolutionRecommandation;

    private String anomalieConstat;
    private String anomalieAnalyse;
    private String anomalieRecommandation;

    private String faiblesseConstat;
    private String faiblesseAnalyse;
    private String faiblesseRecommandation;

    private String creePar;

    public ControleSecondNiveauResponse(
            String id,
            String code,
            String codeUniteAdministrative,
            String libelleUniteAdministrative,
            String codeProcessus,
            String libelleProcessus,
            LocalDate dateControle,
            String testsLibelle,
            String testsConstats,
            String testsAnalyse,
            String testsRecommandation,
            String revuesLibelle,
            String revuesConstats,
            String revuesAnalyse,
            String revuesRecommandation,
            String verificationLibelleDesPieces,
            String verificationConstats,
            String verificationAnalyse,
            String verificationRecommandation,
            String evolutionIntituleOperation,
            String evolutionProceduresInternesRenforcements,
            String evolutionResultatsConformite,
            String evolutionAnalyse,
            String evolutionRecommandation,
            String anomalieConstat,
            String anomalieAnalyse,
            String anomalieRecommandation,
            String faiblesseConstat,
            String faiblesseAnalyse,
            String faiblesseRecommandation,
            String creePar
    ) {
        this.id = id;
        this.code = code;
        this.codeUniteAdministrative = codeUniteAdministrative;
        this.libelleUniteAdministrative = libelleUniteAdministrative;
        this.codeProcessus = codeProcessus;
        this.libelleProcessus = libelleProcessus;
        this.dateControle = dateControle;
        this.testsLibelle = testsLibelle;
        this.testsConstats = testsConstats;
        this.testsAnalyse = testsAnalyse;
        this.testsRecommandation = testsRecommandation;
        this.revuesLibelle = revuesLibelle;
        this.revuesConstats = revuesConstats;
        this.revuesAnalyse = revuesAnalyse;
        this.revuesRecommandation = revuesRecommandation;
        this.verificationLibelleDesPieces = verificationLibelleDesPieces;
        this.verificationConstats = verificationConstats;
        this.verificationAnalyse = verificationAnalyse;
        this.verificationRecommandation = verificationRecommandation;
        this.evolutionIntituleOperation = evolutionIntituleOperation;
        this.evolutionProceduresInternesRenforcements = evolutionProceduresInternesRenforcements;
        this.evolutionResultatsConformite = evolutionResultatsConformite;
        this.evolutionAnalyse = evolutionAnalyse;
        this.evolutionRecommandation = evolutionRecommandation;
        this.anomalieConstat = anomalieConstat;
        this.anomalieAnalyse = anomalieAnalyse;
        this.anomalieRecommandation = anomalieRecommandation;
        this.faiblesseConstat = faiblesseConstat;
        this.faiblesseAnalyse = faiblesseAnalyse;
        this.faiblesseRecommandation = faiblesseRecommandation;
        this.creePar = creePar;
    }

    public String getId() { return id; }
    public String getCode() { return code; }
    public String getCodeUniteAdministrative() { return codeUniteAdministrative; }
    public String getLibelleUniteAdministrative() { return libelleUniteAdministrative; }
    public String getCodeProcessus() { return codeProcessus; }
    public String getLibelleProcessus() { return libelleProcessus; }
    public LocalDate getDateControle() { return dateControle; }
    public String getTestsLibelle() { return testsLibelle; }
    public String getTestsConstats() { return testsConstats; }
    public String getTestsAnalyse() { return testsAnalyse; }
    public String getTestsRecommandation() { return testsRecommandation; }
    public String getRevuesLibelle() { return revuesLibelle; }
    public String getRevuesConstats() { return revuesConstats; }
    public String getRevuesAnalyse() { return revuesAnalyse; }
    public String getRevuesRecommandation() { return revuesRecommandation; }
    public String getVerificationLibelleDesPieces() { return verificationLibelleDesPieces; }
    public String getVerificationConstats() { return verificationConstats; }
    public String getVerificationAnalyse() { return verificationAnalyse; }
    public String getVerificationRecommandation() { return verificationRecommandation; }
    public String getEvolutionIntituleOperation() { return evolutionIntituleOperation; }
    public String getEvolutionProceduresInternesRenforcements() { return evolutionProceduresInternesRenforcements; }
    public String getEvolutionResultatsConformite() { return evolutionResultatsConformite; }
    public String getEvolutionAnalyse() { return evolutionAnalyse; }
    public String getEvolutionRecommandation() { return evolutionRecommandation; }
    public String getAnomalieConstat() { return anomalieConstat; }
    public String getAnomalieAnalyse() { return anomalieAnalyse; }
    public String getAnomalieRecommandation() { return anomalieRecommandation; }
    public String getFaiblesseConstat() { return faiblesseConstat; }
    public String getFaiblesseAnalyse() { return faiblesseAnalyse; }
    public String getFaiblesseRecommandation() { return faiblesseRecommandation; }
    public String getCreePar() { return creePar; }
}

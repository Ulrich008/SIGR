package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ControleSecondNiveauRequest {

    @NotBlank(message = "L'unité administrative est obligatoire")
    private String codeUniteAdministrative;

    @NotBlank(message = "Le processus est obligatoire")
    private String codeProcessus;

    @NotNull(message = "La date du contrôle est obligatoire")
    private LocalDate dateControle;

    // ================= ÉTAPE 2 =================

    @Size(max = 500) private String testsLibelle;
    @Size(max = 2000) private String testsConstats;
    @Size(max = 2000) private String testsAnalyse;
    @Size(max = 2000) private String testsRecommandation;

    @Size(max = 500) private String revuesLibelle;
    @Size(max = 2000) private String revuesConstats;
    @Size(max = 2000) private String revuesAnalyse;
    @Size(max = 2000) private String revuesRecommandation;

    @Size(max = 500) private String verificationLibelleDesPieces;
    @Size(max = 2000) private String verificationConstats;
    @Size(max = 2000) private String verificationAnalyse;
    @Size(max = 2000) private String verificationRecommandation;

    // ================= ÉTAPE 3 =================

    @Size(max = 500) private String evolutionIntituleOperation;
    @Size(max = 2000) private String evolutionProceduresInternesRenforcements;
    @Size(max = 2000) private String evolutionResultatsConformite;
    @Size(max = 2000) private String evolutionAnalyse;
    @Size(max = 2000) private String evolutionRecommandation;

    // ================= ÉTAPE 4 =================

    @Size(max = 2000) private String anomalieConstat;
    @Size(max = 2000) private String anomalieAnalyse;
    @Size(max = 2000) private String anomalieRecommandation;

    @Size(max = 2000) private String faiblesseConstat;
    @Size(max = 2000) private String faiblesseAnalyse;
    @Size(max = 2000) private String faiblesseRecommandation;

    // ================= GETTERS / SETTERS =================

    public String getCodeUniteAdministrative() { return codeUniteAdministrative; }
    public ControleSecondNiveauRequest setCodeUniteAdministrative(String codeUniteAdministrative) { this.codeUniteAdministrative = codeUniteAdministrative; return this; }

    public String getCodeProcessus() { return codeProcessus; }
    public ControleSecondNiveauRequest setCodeProcessus(String codeProcessus) { this.codeProcessus = codeProcessus; return this; }

    public LocalDate getDateControle() { return dateControle; }
    public ControleSecondNiveauRequest setDateControle(LocalDate dateControle) { this.dateControle = dateControle; return this; }

    public String getTestsLibelle() { return testsLibelle; }
    public ControleSecondNiveauRequest setTestsLibelle(String testsLibelle) { this.testsLibelle = testsLibelle; return this; }

    public String getTestsConstats() { return testsConstats; }
    public ControleSecondNiveauRequest setTestsConstats(String testsConstats) { this.testsConstats = testsConstats; return this; }

    public String getTestsAnalyse() { return testsAnalyse; }
    public ControleSecondNiveauRequest setTestsAnalyse(String testsAnalyse) { this.testsAnalyse = testsAnalyse; return this; }

    public String getTestsRecommandation() { return testsRecommandation; }
    public ControleSecondNiveauRequest setTestsRecommandation(String testsRecommandation) { this.testsRecommandation = testsRecommandation; return this; }

    public String getRevuesLibelle() { return revuesLibelle; }
    public ControleSecondNiveauRequest setRevuesLibelle(String revuesLibelle) { this.revuesLibelle = revuesLibelle; return this; }

    public String getRevuesConstats() { return revuesConstats; }
    public ControleSecondNiveauRequest setRevuesConstats(String revuesConstats) { this.revuesConstats = revuesConstats; return this; }

    public String getRevuesAnalyse() { return revuesAnalyse; }
    public ControleSecondNiveauRequest setRevuesAnalyse(String revuesAnalyse) { this.revuesAnalyse = revuesAnalyse; return this; }

    public String getRevuesRecommandation() { return revuesRecommandation; }
    public ControleSecondNiveauRequest setRevuesRecommandation(String revuesRecommandation) { this.revuesRecommandation = revuesRecommandation; return this; }

    public String getVerificationLibelleDesPieces() { return verificationLibelleDesPieces; }
    public ControleSecondNiveauRequest setVerificationLibelleDesPieces(String verificationLibelleDesPieces) { this.verificationLibelleDesPieces = verificationLibelleDesPieces; return this; }

    public String getVerificationConstats() { return verificationConstats; }
    public ControleSecondNiveauRequest setVerificationConstats(String verificationConstats) { this.verificationConstats = verificationConstats; return this; }

    public String getVerificationAnalyse() { return verificationAnalyse; }
    public ControleSecondNiveauRequest setVerificationAnalyse(String verificationAnalyse) { this.verificationAnalyse = verificationAnalyse; return this; }

    public String getVerificationRecommandation() { return verificationRecommandation; }
    public ControleSecondNiveauRequest setVerificationRecommandation(String verificationRecommandation) { this.verificationRecommandation = verificationRecommandation; return this; }

    public String getEvolutionIntituleOperation() { return evolutionIntituleOperation; }
    public ControleSecondNiveauRequest setEvolutionIntituleOperation(String evolutionIntituleOperation) { this.evolutionIntituleOperation = evolutionIntituleOperation; return this; }

    public String getEvolutionProceduresInternesRenforcements() { return evolutionProceduresInternesRenforcements; }
    public ControleSecondNiveauRequest setEvolutionProceduresInternesRenforcements(String evolutionProceduresInternesRenforcements) { this.evolutionProceduresInternesRenforcements = evolutionProceduresInternesRenforcements; return this; }

    public String getEvolutionResultatsConformite() { return evolutionResultatsConformite; }
    public ControleSecondNiveauRequest setEvolutionResultatsConformite(String evolutionResultatsConformite) { this.evolutionResultatsConformite = evolutionResultatsConformite; return this; }

    public String getEvolutionAnalyse() { return evolutionAnalyse; }
    public ControleSecondNiveauRequest setEvolutionAnalyse(String evolutionAnalyse) { this.evolutionAnalyse = evolutionAnalyse; return this; }

    public String getEvolutionRecommandation() { return evolutionRecommandation; }
    public ControleSecondNiveauRequest setEvolutionRecommandation(String evolutionRecommandation) { this.evolutionRecommandation = evolutionRecommandation; return this; }

    public String getAnomalieConstat() { return anomalieConstat; }
    public ControleSecondNiveauRequest setAnomalieConstat(String anomalieConstat) { this.anomalieConstat = anomalieConstat; return this; }

    public String getAnomalieAnalyse() { return anomalieAnalyse; }
    public ControleSecondNiveauRequest setAnomalieAnalyse(String anomalieAnalyse) { this.anomalieAnalyse = anomalieAnalyse; return this; }

    public String getAnomalieRecommandation() { return anomalieRecommandation; }
    public ControleSecondNiveauRequest setAnomalieRecommandation(String anomalieRecommandation) { this.anomalieRecommandation = anomalieRecommandation; return this; }

    public String getFaiblesseConstat() { return faiblesseConstat; }
    public ControleSecondNiveauRequest setFaiblesseConstat(String faiblesseConstat) { this.faiblesseConstat = faiblesseConstat; return this; }

    public String getFaiblesseAnalyse() { return faiblesseAnalyse; }
    public ControleSecondNiveauRequest setFaiblesseAnalyse(String faiblesseAnalyse) { this.faiblesseAnalyse = faiblesseAnalyse; return this; }

    public String getFaiblesseRecommandation() { return faiblesseRecommandation; }
    public ControleSecondNiveauRequest setFaiblesseRecommandation(String faiblesseRecommandation) { this.faiblesseRecommandation = faiblesseRecommandation; return this; }
}

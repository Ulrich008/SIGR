package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;

/**
 * Contrôle de second niveau réalisé par un Contrôleur Interne sur un
 * processus d'une unité administrative : trois lignes fixes (Tests, Revues,
 * Vérification), un bloc "Évolution de conformité", et deux lignes fixes
 * "Anomalies et faiblesses de contrôles". Sert ensuite de matière première
 * aux Rapports de contrôle interne du même couple UA + Processus (constats
 * et recommandations agrégés à la volée, voir RapportControleInterneService).
 */
@Entity
@Table(name = "controle_second_niveau")
@Audited
@FilterDef(name = "ministereFilter", parameters = @ParamDef(name = "codeMinistere", type = String.class))
@Filter(name = "ministereFilter", condition = "id_unite_administrative IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_ministere = :codeMinistere)")
public class ControleSecondNiveau extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_controle_second_niveau", length = 50, updatable = false, nullable = false)
    private String id;

    @Column(name = "code", length = 50, unique = true, nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unite_administrative", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private UniteAdministrative uniteAdministrative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_processus", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Processus processus;

    @Column(name = "date_controle", nullable = false)
    private LocalDate dateControle;

    // ================= ÉTAPE 2 : CONTRÔLE DE SECOND NIVEAU =================

    @Column(name = "tests_libelle", length = 500)
    private String testsLibelle;
    @Column(name = "tests_constats", length = 2000)
    private String testsConstats;
    @Column(name = "tests_analyse", length = 2000)
    private String testsAnalyse;
    @Column(name = "tests_recommandation", length = 2000)
    private String testsRecommandation;

    @Column(name = "revues_libelle", length = 500)
    private String revuesLibelle;
    @Column(name = "revues_constats", length = 2000)
    private String revuesConstats;
    @Column(name = "revues_analyse", length = 2000)
    private String revuesAnalyse;
    @Column(name = "revues_recommandation", length = 2000)
    private String revuesRecommandation;

    @Column(name = "verification_libelle_des_pieces", length = 500)
    private String verificationLibelleDesPieces;
    @Column(name = "verification_constats", length = 2000)
    private String verificationConstats;
    @Column(name = "verification_analyse", length = 2000)
    private String verificationAnalyse;
    @Column(name = "verification_recommandation", length = 2000)
    private String verificationRecommandation;

    // ================= ÉTAPE 3 : ÉVOLUTION DE CONFORMITÉ =================

    @Column(name = "evolution_intitule_operation", length = 500)
    private String evolutionIntituleOperation;
    @Column(name = "evolution_procedures_internes_renforcements", length = 2000)
    private String evolutionProceduresInternesRenforcements;
    @Column(name = "evolution_resultats_conformite", length = 2000)
    private String evolutionResultatsConformite;
    @Column(name = "evolution_analyse", length = 2000)
    private String evolutionAnalyse;
    @Column(name = "evolution_recommandation", length = 2000)
    private String evolutionRecommandation;

    // ================= ÉTAPE 4 : ANOMALIES ET FAIBLESSES DE CONTRÔLES =================

    @Column(name = "anomalie_constat", length = 2000)
    private String anomalieConstat;
    @Column(name = "anomalie_analyse", length = 2000)
    private String anomalieAnalyse;
    @Column(name = "anomalie_recommandation", length = 2000)
    private String anomalieRecommandation;

    @Column(name = "faiblesse_constat", length = 2000)
    private String faiblesseConstat;
    @Column(name = "faiblesse_analyse", length = 2000)
    private String faiblesseAnalyse;
    @Column(name = "faiblesse_recommandation", length = 2000)
    private String faiblesseRecommandation;

    // ===================== GETTERS / SETTERS =====================

    public String getId() { return id; }
    public ControleSecondNiveau setId(String id) { this.id = id; return this; }

    public String getCode() { return code; }
    public ControleSecondNiveau setCode(String code) { this.code = code; return this; }

    public UniteAdministrative getUniteAdministrative() { return uniteAdministrative; }
    public ControleSecondNiveau setUniteAdministrative(UniteAdministrative uniteAdministrative) { this.uniteAdministrative = uniteAdministrative; return this; }

    public Processus getProcessus() { return processus; }
    public ControleSecondNiveau setProcessus(Processus processus) { this.processus = processus; return this; }

    public LocalDate getDateControle() { return dateControle; }
    public ControleSecondNiveau setDateControle(LocalDate dateControle) { this.dateControle = dateControle; return this; }

    public String getTestsLibelle() { return testsLibelle; }
    public ControleSecondNiveau setTestsLibelle(String testsLibelle) { this.testsLibelle = testsLibelle; return this; }

    public String getTestsConstats() { return testsConstats; }
    public ControleSecondNiveau setTestsConstats(String testsConstats) { this.testsConstats = testsConstats; return this; }

    public String getTestsAnalyse() { return testsAnalyse; }
    public ControleSecondNiveau setTestsAnalyse(String testsAnalyse) { this.testsAnalyse = testsAnalyse; return this; }

    public String getTestsRecommandation() { return testsRecommandation; }
    public ControleSecondNiveau setTestsRecommandation(String testsRecommandation) { this.testsRecommandation = testsRecommandation; return this; }

    public String getRevuesLibelle() { return revuesLibelle; }
    public ControleSecondNiveau setRevuesLibelle(String revuesLibelle) { this.revuesLibelle = revuesLibelle; return this; }

    public String getRevuesConstats() { return revuesConstats; }
    public ControleSecondNiveau setRevuesConstats(String revuesConstats) { this.revuesConstats = revuesConstats; return this; }

    public String getRevuesAnalyse() { return revuesAnalyse; }
    public ControleSecondNiveau setRevuesAnalyse(String revuesAnalyse) { this.revuesAnalyse = revuesAnalyse; return this; }

    public String getRevuesRecommandation() { return revuesRecommandation; }
    public ControleSecondNiveau setRevuesRecommandation(String revuesRecommandation) { this.revuesRecommandation = revuesRecommandation; return this; }

    public String getVerificationLibelleDesPieces() { return verificationLibelleDesPieces; }
    public ControleSecondNiveau setVerificationLibelleDesPieces(String verificationLibelleDesPieces) { this.verificationLibelleDesPieces = verificationLibelleDesPieces; return this; }

    public String getVerificationConstats() { return verificationConstats; }
    public ControleSecondNiveau setVerificationConstats(String verificationConstats) { this.verificationConstats = verificationConstats; return this; }

    public String getVerificationAnalyse() { return verificationAnalyse; }
    public ControleSecondNiveau setVerificationAnalyse(String verificationAnalyse) { this.verificationAnalyse = verificationAnalyse; return this; }

    public String getVerificationRecommandation() { return verificationRecommandation; }
    public ControleSecondNiveau setVerificationRecommandation(String verificationRecommandation) { this.verificationRecommandation = verificationRecommandation; return this; }

    public String getEvolutionIntituleOperation() { return evolutionIntituleOperation; }
    public ControleSecondNiveau setEvolutionIntituleOperation(String evolutionIntituleOperation) { this.evolutionIntituleOperation = evolutionIntituleOperation; return this; }

    public String getEvolutionProceduresInternesRenforcements() { return evolutionProceduresInternesRenforcements; }
    public ControleSecondNiveau setEvolutionProceduresInternesRenforcements(String evolutionProceduresInternesRenforcements) { this.evolutionProceduresInternesRenforcements = evolutionProceduresInternesRenforcements; return this; }

    public String getEvolutionResultatsConformite() { return evolutionResultatsConformite; }
    public ControleSecondNiveau setEvolutionResultatsConformite(String evolutionResultatsConformite) { this.evolutionResultatsConformite = evolutionResultatsConformite; return this; }

    public String getEvolutionAnalyse() { return evolutionAnalyse; }
    public ControleSecondNiveau setEvolutionAnalyse(String evolutionAnalyse) { this.evolutionAnalyse = evolutionAnalyse; return this; }

    public String getEvolutionRecommandation() { return evolutionRecommandation; }
    public ControleSecondNiveau setEvolutionRecommandation(String evolutionRecommandation) { this.evolutionRecommandation = evolutionRecommandation; return this; }

    public String getAnomalieConstat() { return anomalieConstat; }
    public ControleSecondNiveau setAnomalieConstat(String anomalieConstat) { this.anomalieConstat = anomalieConstat; return this; }

    public String getAnomalieAnalyse() { return anomalieAnalyse; }
    public ControleSecondNiveau setAnomalieAnalyse(String anomalieAnalyse) { this.anomalieAnalyse = anomalieAnalyse; return this; }

    public String getAnomalieRecommandation() { return anomalieRecommandation; }
    public ControleSecondNiveau setAnomalieRecommandation(String anomalieRecommandation) { this.anomalieRecommandation = anomalieRecommandation; return this; }

    public String getFaiblesseConstat() { return faiblesseConstat; }
    public ControleSecondNiveau setFaiblesseConstat(String faiblesseConstat) { this.faiblesseConstat = faiblesseConstat; return this; }

    public String getFaiblesseAnalyse() { return faiblesseAnalyse; }
    public ControleSecondNiveau setFaiblesseAnalyse(String faiblesseAnalyse) { this.faiblesseAnalyse = faiblesseAnalyse; return this; }

    public String getFaiblesseRecommandation() { return faiblesseRecommandation; }
    public ControleSecondNiveau setFaiblesseRecommandation(String faiblesseRecommandation) { this.faiblesseRecommandation = faiblesseRecommandation; return this; }
}

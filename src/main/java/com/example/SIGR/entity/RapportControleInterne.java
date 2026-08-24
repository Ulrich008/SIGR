package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Rapport de contrôle interne d'un couple Unité administrative + Processus.
 * Les constats et recommandations ne sont pas dupliqués ici : ils sont
 * recalculés à la volée depuis les ControleSecondNiveau du même couple
 * (voir RapportControleInterneServiceImpl.getConstatsEtRecommandations),
 * pour rester toujours à jour même si de nouveaux contrôles sont saisis
 * après la création du rapport.
 *
 * Circuit : {@code statut == null} tant que le PDF n'a pas été généré
 * (brouillon) ; EN_ATTENTE_DE_VALIDATION une fois généré ; TRANSMIS une fois
 * envoyé à la CCI ; VALIDE/DIFFERE/REJETE selon l'avis de la CCI (motif
 * obligatoire pour ces deux derniers, voir RapportControleInterneServiceImpl.validerAvis).
 */
@Entity
@Table(name = "rapport_controle_interne")
@Audited
@FilterDef(name = "ministereFilter", parameters = @ParamDef(name = "codeMinistere", type = String.class))
@Filter(name = "ministereFilter", condition = "id_unite_administrative IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_ministere = :codeMinistere)")
public class RapportControleInterne extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_rapport_ci", length = 50, updatable = false, nullable = false)
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

    @Column(name = "date_emission", nullable = false)
    private LocalDate dateEmission;

    @Column(name = "preambule", length = 2000)
    private String preambule;

    // Encode la liste d'actions correctrices (libellé + date de début + date
    // de fin) sous forme "libelle|dateDebut|dateFin" par ligne — voir
    // getActionsCorrectivesList()/setActionsCorrectivesList(). Pas de table
    // dédiée : ces actions n'ont pas de cycle de vie propre, elles n'existent
    // qu'à travers leur rapport.
    @Column(name = "actions_correctives", length = 4000)
    private String actionsCorrectives;

    @Column(name = "conclusion", length = 2000)
    private String conclusion;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", length = 30)
    private StatutRapportCI statut;

    @Column(name = "motif", length = 1000)
    private String motif;

    // Pas de @Lob : sur PostgreSQL, @Lob sur un byte[] mappe la colonne en
    // "oid" (Large Object), dont la lecture passe par un flux JDBC lié à la
    // transaction — d'où "Unable to access lob stream" dès que le byte[]
    // est lu après la fin de la transaction qui l'a chargé. Un byte[] simple
    // mappe nativement en "bytea", lu directement sans flux ni transaction.
    @Column(name = "pdf_content", columnDefinition = "bytea")
    private byte[] pdfContent;

    @Column(name = "pdf_genere_le")
    private LocalDateTime pdfGenereLe;

    // Suivi des recommandations du rapport (menu "Suivi des Recommandations
    // des CI"), distinct du statut de transmission ci-dessus : nullable tant
    // qu'aucun suivi n'a été renseigné.
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_suivi", length = 30)
    private StatutSuiviRecommandation statutSuivi;

    @Column(name = "decision_suivi", length = 1000)
    private String decisionSuivi;

    @Column(name = "date_decision_suivi")
    private LocalDateTime dateDecisionSuivi;

    // ===================== GETTERS / SETTERS =====================

    public String getId() { return id; }
    public RapportControleInterne setId(String id) { this.id = id; return this; }

    public String getCode() { return code; }
    public RapportControleInterne setCode(String code) { this.code = code; return this; }

    public UniteAdministrative getUniteAdministrative() { return uniteAdministrative; }
    public RapportControleInterne setUniteAdministrative(UniteAdministrative uniteAdministrative) { this.uniteAdministrative = uniteAdministrative; return this; }

    public Processus getProcessus() { return processus; }
    public RapportControleInterne setProcessus(Processus processus) { this.processus = processus; return this; }

    public LocalDate getDateEmission() { return dateEmission; }
    public RapportControleInterne setDateEmission(LocalDate dateEmission) { this.dateEmission = dateEmission; return this; }

    public String getPreambule() { return preambule; }
    public RapportControleInterne setPreambule(String preambule) { this.preambule = preambule; return this; }

    public String getActionsCorrectives() { return actionsCorrectives; }
    public RapportControleInterne setActionsCorrectives(String actionsCorrectives) { this.actionsCorrectives = actionsCorrectives; return this; }

    private static final String SEPARATEUR_CHAMP = "|";
    private static final String SEPARATEUR_LIGNE = "\n";

    public java.util.List<ActionCorrective> getActionsCorrectivesList() {
        if (actionsCorrectives == null || actionsCorrectives.isBlank()) {
            return new java.util.ArrayList<>();
        }
        java.util.List<ActionCorrective> resultat = new java.util.ArrayList<>();
        for (String ligne : actionsCorrectives.split(SEPARATEUR_LIGNE)) {
            if (ligne.isBlank()) continue;
            String[] parts = ligne.split(java.util.regex.Pattern.quote(SEPARATEUR_CHAMP), -1);
            String libelle = parts.length > 0 ? parts[0] : null;
            LocalDate dateDebut = parts.length > 1 && !parts[1].isBlank() ? LocalDate.parse(parts[1]) : null;
            LocalDate dateFin = parts.length > 2 && !parts[2].isBlank() ? LocalDate.parse(parts[2]) : null;
            resultat.add(new ActionCorrective(libelle, dateDebut, dateFin));
        }
        return resultat;
    }

    public RapportControleInterne setActionsCorrectivesList(java.util.List<ActionCorrective> actions) {
        if (actions == null || actions.isEmpty()) {
            this.actionsCorrectives = null;
            return this;
        }
        this.actionsCorrectives = actions.stream()
                .map(a -> String.join(
                        SEPARATEUR_CHAMP,
                        a.getLibelle() != null ? a.getLibelle().replace(SEPARATEUR_LIGNE, " ") : "",
                        a.getDateDebut() != null ? a.getDateDebut().toString() : "",
                        a.getDateFin() != null ? a.getDateFin().toString() : ""
                ))
                .collect(java.util.stream.Collectors.joining(SEPARATEUR_LIGNE));
        return this;
    }

    public String getConclusion() { return conclusion; }
    public RapportControleInterne setConclusion(String conclusion) { this.conclusion = conclusion; return this; }

    public StatutRapportCI getStatut() { return statut; }
    public RapportControleInterne setStatut(StatutRapportCI statut) { this.statut = statut; return this; }

    public String getMotif() { return motif; }
    public RapportControleInterne setMotif(String motif) { this.motif = motif; return this; }

    public byte[] getPdfContent() { return pdfContent; }
    public RapportControleInterne setPdfContent(byte[] pdfContent) { this.pdfContent = pdfContent; return this; }

    public LocalDateTime getPdfGenereLe() { return pdfGenereLe; }
    public RapportControleInterne setPdfGenereLe(LocalDateTime pdfGenereLe) { this.pdfGenereLe = pdfGenereLe; return this; }

    public StatutSuiviRecommandation getStatutSuivi() { return statutSuivi; }
    public RapportControleInterne setStatutSuivi(StatutSuiviRecommandation statutSuivi) { this.statutSuivi = statutSuivi; return this; }

    public String getDecisionSuivi() { return decisionSuivi; }
    public RapportControleInterne setDecisionSuivi(String decisionSuivi) { this.decisionSuivi = decisionSuivi; return this; }

    public LocalDateTime getDateDecisionSuivi() { return dateDecisionSuivi; }
    public RapportControleInterne setDateDecisionSuivi(LocalDateTime dateDecisionSuivi) { this.dateDecisionSuivi = dateDecisionSuivi; return this; }
}

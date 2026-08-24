package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "risque")
@Audited
@FilterDef(name = "ministereFilter", parameters = @ParamDef(name = "codeMinistere", type = String.class))
@Filter(name = "ministereFilter", condition = "code_processus IN (SELECT p.code_processus FROM processus p WHERE p.id_unite IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_ministere = :codeMinistere))")
@FilterDef(name = "uaFilter", parameters = @ParamDef(name = "codeUnite", type = String.class))
@Filter(name = "uaFilter", condition = "code_processus IN (SELECT p.code_processus FROM processus p WHERE p.id_unite IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_unite = :codeUnite))")
public class Risque extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_risque", length = 50, updatable = false, nullable = false)
    private String id;

    @Column(name = "code", length = 50, unique = true, nullable = false)
    private String code;

    @Column(name = "libelle_risque", length = 200)
    private String libelle;

    /**
     * Finalité du processus (parmi celles formalisées sur Processus.finalite)
     * que ce risque menace concrètement — permet de savoir, pour un
     * processus ayant plusieurs finalités, laquelle est exposée par ce
     * risque précis.
     */
    @Column(name = "finalite", length = 500)
    private String finalite;

    @Column(name = "cause_probable", length = 2000)
    private String causeProbable;

    @Column(name = "consequence_probable", length = 2000)
    private String consequenceProbable;

    @Column(name = "bonnes_pratiques", length = 2000)
    private String bonnesPratiques;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_risque", length = 50)
    private StatutRisque statut;

    @Enumerated(EnumType.STRING)
    @Column(name = "strategie_risque", length = 50)
    private StrategieRisque strategieRisque;

    @Enumerated(EnumType.STRING)
    @Column(name = "avis_risque", length = 50)
    private AvisRisque avis;

    @Column(name = "motif", length = 1000)
    private String motif;

    @Column(name = "transmis")
    private Boolean transmis = false;

    /**
     * Étape actuelle du circuit de validation
     * (Formalisation -> Pilote -> CCI -> CMMR -> Validée/Rejetée).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "etape_validation", length = 30)
    private EtapeValidation etapeValidation = EtapeValidation.FORMALISATION;

    /**
     * Agent ayant émis le dernier avis (Validé/Différé/Rejeté) sur ce
     * risque — permet d'afficher qui a rendu l'avis et son profil (Pilote,
     * CCI, CMMR) sans avoir à le déduire de l'étape courante du circuit.
     */
    @ManyToOne
    @JoinColumn(name = "matricule_emetteur_avis", referencedColumnName = "matricule_agent")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Agent emetteurAvis;

    @Column(name = "date_identification")
    private LocalDate dateIdentification;

    // Suivi des actions de mitigation du risque (menu "Suivi des Risques" >
    // "Suivi des actions de mitigations"), distinct du circuit de validation
    // (avis/motif/étapeValidation) ci-dessus.
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_suivi", length = 30)
    private StatutSuiviRecommandation statutSuivi;

    @Column(name = "decision_suivi", length = 1000)
    private String decisionSuivi;

    @Column(name = "date_decision_suivi")
    private java.time.LocalDateTime dateDecisionSuivi;

    /**
     * RELATION :
     * Plusieurs risques peuvent appartenir à un même processus
     * => Many Risques → One Processus
     */
    /**
     * referencedColumnName explicite : sans lui, JPA référence par défaut
     * la clé primaire de Processus (id_processus, un UUID) au lieu de son
     * code métier (code_processus), pourtant utilisé tel quel par le
     * filtre Hibernate par ministère ci-dessus et par les jointures
     * ailleurs dans l'application.
     */
    @ManyToOne
    @JoinColumn(name = "code_processus", referencedColumnName = "code_processus", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Processus processus;

    /**
     * RELATION :
     * Plusieurs risques peuvent être regroupés dans une cartographie
     * => Many Risques → One CartographieRisques
     */
    @ManyToOne
    @JoinColumn(name = "id_cartographie")
    private CartographieRisques cartographie;

    /**
     * RELATION :
     * Un risque possède un type (classification métier)
     * => Enum simple (pas une relation objet)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type_risque", length = 50)
    private TypeRisque typeRisque;

    /**
     * Un risque peut avoir plusieurs évaluations
     */
    @OneToMany(mappedBy = "risque")
    private List<Evaluation> evaluations;

    /*
     * Un risque peut être traité par plusieurs plans de mitigation, et
     * depuis peu un même plan peut lui-même couvrir plusieurs risques
     * (relation N-N, voir PlanMitigation.risques).
     */
    @ManyToMany(mappedBy = "risques")
    private List<PlanMitigation> plansMitigation;

    // ===================== GETTERS / SETTERS =====================

    public String getId() { return id; }
    public Risque setId(String id) { this.id = id; return this; }

    public String getCode() { return code; }
    public Risque setCode(String code) { this.code = code; return this; }

    public String getLibelle() { return libelle; }
    public Risque setLibelle(String libelle) { this.libelle = libelle; return this; }

    public String getFinalite() { return finalite; }
    public Risque setFinalite(String finalite) { this.finalite = finalite; return this; }

    public String getCauseProbable() { return causeProbable; }
    public Risque setCauseProbable(String causeProbable) { this.causeProbable = causeProbable; return this; }

    public String getConsequenceProbable() { return consequenceProbable; }
    public Risque setConsequenceProbable(String consequenceProbable) { this.consequenceProbable = consequenceProbable; return this; }

    public String getBonnesPratiques() { return bonnesPratiques; }
    public Risque setBonnesPratiques(String bonnesPratiques) { this.bonnesPratiques = bonnesPratiques; return this; }

    // Helpers pour convertir entre String (stockage) et List<String> (API)
    public List<String> getCauseProbableList() {
        if (causeProbable == null || causeProbable.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return java.util.Arrays.asList(causeProbable.split(";")).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toList());
    }

    public Risque setCauseProbableList(List<String> causeProbableList) {
        if (causeProbableList == null || causeProbableList.isEmpty()) {
            this.causeProbable = null;
        } else {
            this.causeProbable = String.join("; ", causeProbableList);
        }
        return this;
    }

    public List<String> getConsequenceProbableList() {
        if (consequenceProbable == null || consequenceProbable.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return java.util.Arrays.asList(consequenceProbable.split(";")).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toList());
    }

    public Risque setConsequenceProbableList(List<String> consequenceProbableList) {
        if (consequenceProbableList == null || consequenceProbableList.isEmpty()) {
            this.consequenceProbable = null;
        } else {
            this.consequenceProbable = String.join("; ", consequenceProbableList);
        }
        return this;
    }

    public List<String> getBonnesPratiquesList() {
        if (bonnesPratiques == null || bonnesPratiques.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return java.util.Arrays.asList(bonnesPratiques.split(";")).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toList());
    }

    public Risque setBonnesPratiquesList(List<String> bonnesPratiquesList) {
        if (bonnesPratiquesList == null || bonnesPratiquesList.isEmpty()) {
            this.bonnesPratiques = null;
        } else {
            this.bonnesPratiques = String.join("; ", bonnesPratiquesList);
        }
        return this;
    }

    public StatutRisque getStatut() { return statut; }
    public Risque setStatut(StatutRisque statut) { this.statut = statut; return this; }

    public StrategieRisque getStrategieRisque() { return strategieRisque; }
    public Risque setStrategieRisque(StrategieRisque strategieRisque) { this.strategieRisque = strategieRisque; return this; }

    public AvisRisque getAvis() { return avis; }
    public Risque setAvis(AvisRisque avis) { this.avis = avis; return this; }

    public String getMotif() { return motif; }
    public Risque setMotif(String motif) { this.motif = motif; return this; }

    public StatutSuiviRecommandation getStatutSuivi() { return statutSuivi; }
    public Risque setStatutSuivi(StatutSuiviRecommandation statutSuivi) { this.statutSuivi = statutSuivi; return this; }

    public String getDecisionSuivi() { return decisionSuivi; }
    public Risque setDecisionSuivi(String decisionSuivi) { this.decisionSuivi = decisionSuivi; return this; }

    public java.time.LocalDateTime getDateDecisionSuivi() { return dateDecisionSuivi; }
    public Risque setDateDecisionSuivi(java.time.LocalDateTime dateDecisionSuivi) { this.dateDecisionSuivi = dateDecisionSuivi; return this; }

    public Boolean getTransmis() { return transmis; }
    public Risque setTransmis(Boolean transmis) { this.transmis = transmis; return this; }

    public EtapeValidation getEtapeValidation() { return etapeValidation; }
    public Risque setEtapeValidation(EtapeValidation etapeValidation) { this.etapeValidation = etapeValidation; return this; }

    public Agent getEmetteurAvis() { return emetteurAvis; }
    public Risque setEmetteurAvis(Agent emetteurAvis) { this.emetteurAvis = emetteurAvis; return this; }

    public LocalDate getDateIdentification() { return dateIdentification; }
    public Risque setDateIdentification(LocalDate dateIdentification) { this.dateIdentification = dateIdentification; return this; }

    public Processus getProcessus() { return processus; }
    public Risque setProcessus(Processus processus) { this.processus = processus; return this; }

    public CartographieRisques getCartographie() { return cartographie; }
    public Risque setCartographie(CartographieRisques cartographie) { this.cartographie = cartographie; return this; }

    public TypeRisque getTypeRisque() { return typeRisque; }
    public Risque setTypeRisque(TypeRisque typeRisque) { this.typeRisque = typeRisque; return this; }

    public List<Evaluation> getEvaluations() { return evaluations; }
    public Risque setEvaluations(List<Evaluation> evaluations) { this.evaluations = evaluations; return this; }

    public List<PlanMitigation> getPlansMitigation() { return plansMitigation; }
    public Risque setPlansMitigation(List<PlanMitigation> plansMitigation) { this.plansMitigation = plansMitigation; return this; }
}
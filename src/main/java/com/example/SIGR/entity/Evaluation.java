package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;

@Entity
@Table(name = "evaluation")
@Audited
@FilterDef(name = "ministereFilter", parameters = @ParamDef(name = "codeMinistere", type = String.class))
@Filter(name = "ministereFilter", condition = "id_risque IN (SELECT r.id_risque FROM risque r WHERE r.code_processus IN (SELECT p.code_processus FROM processus p WHERE p.id_unite IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_ministere = :codeMinistere)))")
@FilterDef(name = "uaFilter", parameters = @ParamDef(name = "codeUnite", type = String.class))
@Filter(name = "uaFilter", condition = "id_risque IN (SELECT r.id_risque FROM risque r WHERE r.code_processus IN (SELECT p.code_processus FROM processus p WHERE p.id_unite IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_unite = :codeUnite)))")
public class Evaluation extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_evaluation", length = 50)
    private String id;

    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    // ================= RISQUE INHERENT =================

    @Column(name = "impact_inherent")
    private Integer impactInherent;

    @Column(name = "probabilite_inherente")
    private Integer probabiliteInherente;

    // ================= MAITRISE DU RISQUE =================

    @Column(name = "protection")
    private Integer protection;

    @Column(name = "prevention")
    private Integer prevention;

    // ================= CONTROLES =================

    @Column(name = "controle_existants", length = 1000)
    private String controleExistants;

    @Column(name = "controle_inexistants", length = 1000)
    private String controleInexistants;

    @Column(name = "deja_survenu")
    private Boolean dejaSurvenu = false;

    // ================= PRIORISATION =================

    @Column(name = "rang_priorite")
    private Integer rangPriorite;

    // ================= PLAN D'ACTION =================

    @Column(name = "recommandation", length = 1000)
    private String recommandation;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    // ================= INFORMATIONS GENERALES =================

    @Column(name = "bonnes_pratiques", length = 500)
    private String bonnesPratiques;

    @ManyToOne
    @JoinColumn(name = "id_risque", nullable = false)
    private Risque risque;

    @ManyToOne
    @JoinColumn(name = "evalue_par", referencedColumnName = "matricule_agent")
    private Agent evaluePar;

    // ================= CALCULS METIER =================

    /**
     * Score inhérent = Impact inhérent × Probabilité inhérente
     */
    @Transient
    public Integer getScoreInherent() {
        if (impactInherent == null || probabiliteInherente == null) {
            return null;
        }
        return impactInherent * probabiliteInherente;
    }

    /**
     * Impact résiduel = Impact inhérent - Protection
     */
    @Transient
    public Integer getImpactResiduel() {
        if (impactInherent == null || protection == null) {
            return null;
        }
        return Math.max(impactInherent - protection, 0);
    }

    /**
     * Probabilité résiduelle = Probabilité inhérente - Prévention
     */
    @Transient
    public Integer getProbabiliteResiduelle() {
        if (probabiliteInherente == null || prevention == null) {
            return null;
        }
        return Math.max(probabiliteInherente - prevention, 0);
    }

    /**
     * Score résiduel = Impact résiduel × Probabilité résiduelle
     */
    @Transient
    public Integer getScoreResiduel() {
        Integer impact = getImpactResiduel();
        Integer proba = getProbabiliteResiduelle();

        if (impact == null || proba == null) {
            return null;
        }
        return impact * proba;
    }

    /**
     * Stratégie de risque calculée automatiquement en fonction du score résiduel
     * - Score 1-7 (Faible) → TOLERER
     * - Score 8-14 (Moyen) → TRAITER
     * - Score 15-25 (Élevé) → TRANSFERER
     * - Score >25 (Très élevé) → TERMINER
     */
    @Transient
    public StrategieRisque getStrategieRisqueCalculee() {
        Integer score = getScoreResiduel();
        
        if (score == null) {
            return null;
        }
        
        if (score <= 7) {
            return StrategieRisque.TOLERER;
        } else if (score <= 14) {
            return StrategieRisque.TRAITER;
        } else if (score <= 25) {
            return StrategieRisque.TRANSFERER;
        } else {
            return StrategieRisque.TERMINER;
        }
    }

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public Evaluation setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Evaluation setCode(String code) {
        this.code = code;
        return this;
    }

    public Integer getImpactInherent() {
        return impactInherent;
    }

    public Evaluation setImpactInherent(Integer impactInherent) {
        this.impactInherent = impactInherent;
        return this;
    }

    public Integer getProbabiliteInherente() {
        return probabiliteInherente;
    }

    public Evaluation setProbabiliteInherente(Integer probabiliteInherente) {
        this.probabiliteInherente = probabiliteInherente;
        return this;
    }

    public Integer getProtection() {
        return protection;
    }

    public Evaluation setProtection(Integer protection) {
        this.protection = protection;
        return this;
    }

    public Integer getPrevention() {
        return prevention;
    }

    public Evaluation setPrevention(Integer prevention) {
        this.prevention = prevention;
        return this;
    }

    public String getControleExistants() {
        return controleExistants;
    }

    public Evaluation setControleExistants(String controleExistants) {
        this.controleExistants = controleExistants;
        return this;
    }

    public String getControleInexistants() {
        return controleInexistants;
    }

    public Evaluation setControleInexistants(String controleInexistants) {
        this.controleInexistants = controleInexistants;
        return this;
    }

    public Boolean getDejaSurvenu() {
        return dejaSurvenu;
    }

    public Evaluation setDejaSurvenu(Boolean dejaSurvenu) {
        this.dejaSurvenu = dejaSurvenu;
        return this;
    }

    public Integer getRangPriorite() {
        return rangPriorite;
    }

    public Evaluation setRangPriorite(Integer rangPriorite) {
        this.rangPriorite = rangPriorite;
        return this;
    }

    public String getRecommandation() {
        return recommandation;
    }

    public Evaluation setRecommandation(String recommandation) {
        this.recommandation = recommandation;
        return this;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public Evaluation setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public Evaluation setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public String getBonnesPratiques() {
        return bonnesPratiques;
    }

    public Evaluation setBonnesPratiques(String bonnesPratiques) {
        this.bonnesPratiques = bonnesPratiques;
        return this;
    }

    public Risque getRisque() {
        return risque;
    }

    public Evaluation setRisque(Risque risque) {
        this.risque = risque;
        return this;
    }

    public Agent getEvaluePar() {
        return evaluePar;
    }

    public Evaluation setEvaluePar(Agent evaluePar) {
        this.evaluePar = evaluePar;
        return this;
    }
}
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
@Table(name = "indicateur_performance")
@Audited
@FilterDef(name = "ministereFilter", parameters = @ParamDef(name = "codeMinistere", type = String.class))
@Filter(name = "ministereFilter", condition = "code_processus IN (SELECT p.code_processus FROM processus p WHERE p.id_unite IN (SELECT ua.id_unite FROM unite_administrative ua WHERE ua.code_ministere = :codeMinistere))")
public class IndicateurPerformance extends Auditable {

    // 🔥 ID technique auto-généré
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_indicateur", updatable = false, nullable = false)
    private String id;

    // 🔥 Code métier (unique)
    @Column(name = "code_indicateur", length = 10, unique = true, nullable = false)
    private String code;

    @Column(length = 200)
    private String libelle;

    @ManyToOne
    @JoinColumn(name = "code_unite_mesure", referencedColumnName = "code_unite_mesure")
    private UniteMesure uniteMesure;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Frequence frequence;

    @Column(name = "valeur_cible")
    private String valeurCible;

    @Column(name = "valeur_obtenue")
    private String valeurObtenue;

    @Column(name = "seuil_alerte")
    private LocalDate seuilAlerte;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @ManyToOne
    @JoinColumn(name = "code_processus", referencedColumnName = "code_processus", nullable = false)
    private Processus processus;

    /**
     * Relation optionnelle avec un risque
     */
    @ManyToOne
    @JoinColumn(name = "code_risque", referencedColumnName = "code")
    private Risque risque;

    /**
     * Relation optionnelle avec un plan de mitigation
     */
    @ManyToOne
    @JoinColumn(name = "code_plan_mitigation", referencedColumnName = "code")
    private PlanMitigation planMitigation;

    /**
     * Relation optionnelle avec une action
     */
    @ManyToOne
    @JoinColumn(name = "code_action", referencedColumnName = "code_action")
    private Action action;

    // ===================== CALCULS =====================

    @Transient
    public Double getEcartCible() {
        if (valeurCible == null || valeurObtenue == null) return null;
        
        // Si l'unité de mesure est de type DATE, calculer l'écart en nombre de jours
        if (uniteMesure != null && uniteMesure.getTypeUnite() == UniteMesure.TypeUnite.DATE) {
            try {
                LocalDate dateCible = LocalDate.parse(valeurCible);
                LocalDate dateObtenue = LocalDate.parse(valeurObtenue);
                long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(dateCible, dateObtenue);
                return (double) daysBetween;
            } catch (Exception e) {
                return null;
            }
        }
        
        // Sinon, calculer l'écart numérique
        try {
            double cible = Double.parseDouble(valeurCible);
            double obtenue = Double.parseDouble(valeurObtenue);
            return obtenue - cible;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Transient
    public String getStatut() {

        if (dateFin == null || seuilAlerte == null) {
            return "Informations de suivi non disponibles";
        }

        LocalDate currentDate = LocalDate.now();

        if (currentDate.isAfter(dateFin)) {
            return "Échéance dépassée - Action de mitigation en retard";
        }

        if (currentDate.isAfter(seuilAlerte)) {
            return "Attention : échéance proche, suivi renforcé requis";
        }

        return "Plan de mitigation en cours conformément au calendrier";
    }

    // ===================== GETTERS / SETTERS =====================

    public String getId() {
        return id;
    }

    public IndicateurPerformance setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public IndicateurPerformance setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public IndicateurPerformance setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public UniteMesure getUniteMesure() {
        return uniteMesure;
    }

    public IndicateurPerformance setUniteMesure(UniteMesure uniteMesure) {
        this.uniteMesure = uniteMesure;
        return this;
    }

    public Frequence getFrequence() {
        return frequence;
    }

    public IndicateurPerformance setFrequence(Frequence frequence) {
        this.frequence = frequence;
        return this;
    }


    public String getValeurCible() {
        return valeurCible;
    }

    public IndicateurPerformance setValeurCible(String valeurCible) {
        this.valeurCible = valeurCible;
        return this;
    }

    public String getValeurObtenue() {
        return valeurObtenue;
    }

    public IndicateurPerformance setValeurObtenue(String valeurObtenue) {
        this.valeurObtenue = valeurObtenue;
        return this;
    }

    public LocalDate getSeuilAlerte() {
        return seuilAlerte;
    }

    public IndicateurPerformance setSeuilAlerte(LocalDate seuilAlerte) {
        this.seuilAlerte = seuilAlerte;
        return this;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public IndicateurPerformance setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public IndicateurPerformance setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public Processus getProcessus() {
        return processus;
    }

    public IndicateurPerformance setProcessus(Processus processus) {
        this.processus = processus;
        return this;
    }

    public Risque getRisque() {
        return risque;
    }

    public IndicateurPerformance setRisque(Risque risque) {
        this.risque = risque;
        return this;
    }

    public PlanMitigation getPlanMitigation() {
        return planMitigation;
    }

    public IndicateurPerformance setPlanMitigation(PlanMitigation planMitigation) {
        this.planMitigation = planMitigation;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public IndicateurPerformance setAction(Action action) {
        this.action = action;
        return this;
    }
}
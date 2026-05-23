package com.example.SIGR.dto.response;

import com.example.SIGR.entity.StatutRisque;
import com.example.SIGR.entity.TypeRisque;

import java.time.LocalDate;

public class CartographieRisqueDetailResponse {

    // ================= RISQUE =================
    private String codeRisque;
    private String libelleRisque;
    private TypeRisque typeRisque;
    private StatutRisque statutRisque;
    private LocalDate dateIdentification;

    // ================= PROCESSUS / UNITE =================
    private String codeProcessus;
    private String libelleProcessus;
    private String uniteAdministrative;

    // ================= CAUSES / CONSEQUENCES =================
    private String causeProbable;
    private String consequenceProbable;

    // ================= SCORE =================
    private Integer impactInherent;
    private Integer probabiliteInherente;
    private Integer scoreInherent;

    private Integer protection;
    private Integer prevention;
    private Integer impactResiduel;
    private Integer probabiliteResiduelle;
    private Integer scoreResiduel;

    private Integer rangPriorite;

    // ================= CONTROLES =================
    private String controleExistants;
    private String controleInexistants;
    private Boolean dejaSurvenu;

    // ================= PLAN ACTION =================
    private String recommandation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String bonnesPratiques;

    // ================= AUDIT =================
    private String evaluePar;

    // ================= CONSTRUCTEUR JPQL =================
    public CartographieRisqueDetailResponse(
            String codeRisque,
            String libelleRisque,
            TypeRisque typeRisque,
            StatutRisque statutRisque,
            LocalDate dateIdentification,

            String codeProcessus,
            String libelleProcessus,
            String uniteAdministrative,

            String causeProbable,
            String consequenceProbable,

            Integer impactInherent,
            Integer probabiliteInherente,
            Integer scoreInherent,

            Integer protection,
            Integer prevention,
            Integer impactResiduel,
            Integer probabiliteResiduelle,
            Integer scoreResiduel,

            Integer rangPriorite,

            String controleExistants,
            String controleInexistants,
            Boolean dejaSurvenu,

            String recommandation,
            LocalDate dateDebut,
            LocalDate dateFin,
            String bonnesPratiques,

            String evaluePar
    ) {
        this.codeRisque = codeRisque;
        this.libelleRisque = libelleRisque;
        this.typeRisque = typeRisque;
        this.statutRisque = statutRisque;
        this.dateIdentification = dateIdentification;

        this.codeProcessus = codeProcessus;
        this.libelleProcessus = libelleProcessus;
        this.uniteAdministrative = uniteAdministrative;

        this.causeProbable = causeProbable;
        this.consequenceProbable = consequenceProbable;

        this.impactInherent = impactInherent;
        this.probabiliteInherente = probabiliteInherente;
        this.scoreInherent = scoreInherent;

        this.protection = protection;
        this.prevention = prevention;
        this.impactResiduel = impactResiduel;
        this.probabiliteResiduelle = probabiliteResiduelle;
        this.scoreResiduel = scoreResiduel;

        this.rangPriorite = rangPriorite;

        this.controleExistants = controleExistants;
        this.controleInexistants = controleInexistants;
        this.dejaSurvenu = dejaSurvenu;

        this.recommandation = recommandation;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.bonnesPratiques = bonnesPratiques;

        this.evaluePar = evaluePar;
    }

    // ================= GETTERS =================

    public String getCodeRisque() { return codeRisque; }

    public String getLibelleRisque() { return libelleRisque; }

    public TypeRisque getTypeRisque() { return typeRisque; }

    public StatutRisque getStatutRisque() { return statutRisque; }

    public LocalDate getDateIdentification() { return dateIdentification; }

    public String getCodeProcessus() { return codeProcessus; }

    public String getLibelleProcessus() { return libelleProcessus; }

    public String getUniteAdministrative() { return uniteAdministrative; }

    public String getCauseProbable() { return causeProbable; }

    public String getConsequenceProbable() { return consequenceProbable; }

    public Integer getImpactInherent() { return impactInherent; }

    public Integer getProbabiliteInherente() { return probabiliteInherente; }

    public Integer getScoreInherent() { return scoreInherent; }

    public Integer getProtection() { return protection; }

    public Integer getPrevention() { return prevention; }

    public Integer getImpactResiduel() { return impactResiduel; }

    public Integer getProbabiliteResiduelle() { return probabiliteResiduelle; }

    public Integer getScoreResiduel() { return scoreResiduel; }

    public Integer getRangPriorite() { return rangPriorite; }

    public String getControleExistants() { return controleExistants; }

    public String getControleInexistants() { return controleInexistants; }

    public Boolean getDejaSurvenu() { return dejaSurvenu; }

    public String getRecommandation() { return recommandation; }

    public LocalDate getDateDebut() { return dateDebut; }

    public LocalDate getDateFin() { return dateFin; }

    public String getBonnesPratiques() { return bonnesPratiques; }

    public String getEvaluePar() { return evaluePar; }
}
package com.example.SIGR.dto.response;

import com.example.SIGR.entity.AvisRisque;
import com.example.SIGR.entity.EtapeValidation;
import com.example.SIGR.entity.StatutRisque;
import com.example.SIGR.entity.StatutSuiviRecommandation;
import com.example.SIGR.entity.StrategieRisque;
import com.example.SIGR.entity.TypeRisque;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RisqueResponse {

    private String id;
    private String code;
    private String libelle;
    private String finalite;
    private List<String> causeProbable;
    private List<String> consequenceProbable;
    private List<String> bonnesPratiques;

    private StatutRisque statut;

    private StrategieRisque strategieRisque;

    private LocalDate dateIdentification;

    private String codeProcessus;
    private String nomProcessus;

    private String idCartographie;

    private TypeRisque typeRisque;

    private AvisRisque avis;

    private String motif;

    private Boolean transmis;

    private EtapeValidation etapeValidation;

    private String emetteurAvisMatricule;
    private String emetteurAvisNomComplet;
    private String emetteurAvisCodeProfil;
    private String emetteurAvisLibelleProfil;

    /**
     * Un risque doit avoir été évalué (au moins une Evaluation) avant de
     * pouvoir être transmis par le Responsable des risques — voir
     * RisqueServiceImpl.transmettre().
     */
    private boolean evalue;

    private StatutSuiviRecommandation statutSuivi;
    private String decisionSuivi;
    private LocalDateTime dateDecisionSuivi;

    public RisqueResponse(
            String id,
            String code,
            String libelle,
            String finalite,
            List<String> causeProbable,
            List<String> consequenceProbable,
            List<String> bonnesPratiques,
            StatutRisque statut,
            StrategieRisque strategieRisque,
            LocalDate dateIdentification,
            String codeProcessus,
            String nomProcessus,
            String idCartographie,
            TypeRisque typeRisque,
            AvisRisque avis,
            String motif,
            Boolean transmis,
            EtapeValidation etapeValidation,
            String emetteurAvisMatricule,
            String emetteurAvisNomComplet,
            String emetteurAvisCodeProfil,
            String emetteurAvisLibelleProfil,
            boolean evalue,
            StatutSuiviRecommandation statutSuivi,
            String decisionSuivi,
            LocalDateTime dateDecisionSuivi
    ) {
        this.id = id;
        this.code = code ;
        this.libelle = libelle;
        this.finalite = finalite;
        this.causeProbable = causeProbable;
        this.consequenceProbable = consequenceProbable;
        this.bonnesPratiques = bonnesPratiques;
        this.statut = statut;
        this.strategieRisque = strategieRisque;
        this.dateIdentification = dateIdentification;
        this.codeProcessus = codeProcessus;
        this.nomProcessus = nomProcessus;
        this.idCartographie = idCartographie;
        this.typeRisque = typeRisque;
        this.avis = avis;
        this.motif = motif;
        this.transmis = transmis;
        this.etapeValidation = etapeValidation;
        this.emetteurAvisMatricule = emetteurAvisMatricule;
        this.emetteurAvisNomComplet = emetteurAvisNomComplet;
        this.emetteurAvisCodeProfil = emetteurAvisCodeProfil;
        this.emetteurAvisLibelleProfil = emetteurAvisLibelleProfil;
        this.evalue = evalue;
        this.statutSuivi = statutSuivi;
        this.decisionSuivi = decisionSuivi;
        this.dateDecisionSuivi = dateDecisionSuivi;
    }

    public String getCode() {
        return code;
    }
    public String getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getFinalite() {
        return finalite;
    }

    public List<String> getCauseProbable() {
        return causeProbable;
    }

    public List<String> getConsequenceProbable() {
        return consequenceProbable;
    }

    public List<String> getBonnesPratiques() {
        return bonnesPratiques;
    }

    public StatutRisque getStatut() {
        return statut;
    }

    public StrategieRisque getStrategieRisque() {
        return strategieRisque;
    }

    public LocalDate getDateIdentification() {
        return dateIdentification;
    }

    public String getCodeProcessus() {
        return codeProcessus;
    }

    public String getNomProcessus() {
        return nomProcessus;
    }

    public String getIdCartographie() {
        return idCartographie;
    }

    public TypeRisque getTypeRisque() {
        return typeRisque;
    }

    public AvisRisque getAvis() {
        return avis;
    }

    public String getMotif() {
        return motif;
    }

    public Boolean getTransmis() {
        return transmis;
    }

    public EtapeValidation getEtapeValidation() {
        return etapeValidation;
    }

    public String getEmetteurAvisMatricule() {
        return emetteurAvisMatricule;
    }

    public String getEmetteurAvisNomComplet() {
        return emetteurAvisNomComplet;
    }

    public String getEmetteurAvisCodeProfil() {
        return emetteurAvisCodeProfil;
    }

    public String getEmetteurAvisLibelleProfil() {
        return emetteurAvisLibelleProfil;
    }

    public boolean isEvalue() {
        return evalue;
    }

    public StatutSuiviRecommandation getStatutSuivi() {
        return statutSuivi;
    }

    public String getDecisionSuivi() {
        return decisionSuivi;
    }

    public LocalDateTime getDateDecisionSuivi() {
        return dateDecisionSuivi;
    }
}
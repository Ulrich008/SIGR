package com.example.SIGR.dto.response;

import com.example.SIGR.entity.StatutRisque;
import com.example.SIGR.entity.TypeRisque;

import java.time.LocalDate;
import java.util.List;

public class RisqueResponse {

    private String id;
    private String code;
    private String libelle;
    private List<String> causeProbable;
    private List<String> consequenceProbable;
    private List<String> bonnesPratiques;

    private StatutRisque statut;

    private LocalDate dateIdentification;

    private String codeProcessus;
    private String nomProcessus;

    private String idCartographie;

    private TypeRisque typeRisque;

    /**
     * Risques résiduels liés (après mitigation)
     */
    private List<String> risquesResiduelsIds;

    public RisqueResponse(
            String id,
            String code,
            String libelle,
            List<String> causeProbable,
            List<String> consequenceProbable,
            List<String> bonnesPratiques,
            StatutRisque statut,
            LocalDate dateIdentification,
            String codeProcessus,
            String nomProcessus,
            String idCartographie,
            TypeRisque typeRisque,
            List<String> risquesResiduelsIds
    ) {
        this.id = id;
        this.code = code ;
        this.libelle = libelle;
        this.causeProbable = causeProbable;
        this.consequenceProbable = consequenceProbable;
        this.bonnesPratiques = bonnesPratiques;
        this.statut = statut;
        this.dateIdentification = dateIdentification;
        this.codeProcessus = codeProcessus;
        this.nomProcessus = nomProcessus;
        this.idCartographie = idCartographie;
        this.typeRisque = typeRisque;
        this.risquesResiduelsIds = risquesResiduelsIds;
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

    public List<String> getRisquesResiduelsIds() {
        return risquesResiduelsIds;
    }
}
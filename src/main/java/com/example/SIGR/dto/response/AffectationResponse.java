package com.example.SIGR.dto.response;

import java.time.LocalDate;

public class AffectationResponse {

    private String id;

    private String code;

    private String matriculeAgent;

    private String nomCompletAgent;

    private String codeUnite;

    private String libelleUnite;

    private String poste;

    private LocalDate dateAffectation;

    private LocalDate dateFinAffectation;

    public AffectationResponse(String id,
                               String code,
                               String matriculeAgent,
                               String nomCompletAgent,
                               String codeUnite,
                               String libelleUnite,
                               String poste,
                               LocalDate dateAffectation,
                               LocalDate dateFinAffectation) {

        this.id = id;
        this.code = code;
        this.matriculeAgent = matriculeAgent;
        this.nomCompletAgent = nomCompletAgent;
        this.codeUnite = codeUnite;
        this.libelleUnite = libelleUnite;
        this.poste = poste;
        this.dateAffectation = dateAffectation;
        this.dateFinAffectation = dateFinAffectation;
    }

    // ================= GETTERS =================

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getMatriculeAgent() {
        return matriculeAgent;
    }

    public String getNomCompletAgent() {
        return nomCompletAgent;
    }

    public String getCodeUnite() {
        return codeUnite;
    }

    public String getLibelleUnite() {
        return libelleUnite;
    }

    public String getPoste() {
        return poste;
    }

    public LocalDate getDateAffectation() {
        return dateAffectation;
    }

    public LocalDate getDateFinAffectation() {
        return dateFinAffectation;
    }
}
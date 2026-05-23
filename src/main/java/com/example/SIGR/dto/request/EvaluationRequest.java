package com.example.SIGR.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class EvaluationRequest {

    // ================= RISQUE INHERENT =================

    @NotNull(message = "L'impact inhérent est obligatoire")
    @Min(value = 1, message = "L'impact inhérent doit être >= 1")
    @Max(value = 5, message = "L'impact inhérent doit être <= 5")
    private Integer impactInherent;

    @NotNull(message = "La probabilité inhérente est obligatoire")
    @Min(value = 1, message = "La probabilité inhérente doit être >= 1")
    @Max(value = 5, message = "La probabilité inhérente doit être <= 5")
    private Integer probabiliteInherente;

    // ================= MAITRISE DU RISQUE =================

    @NotNull(message = "Le niveau de protection est obligatoire")
    @Min(value = 0, message = "La protection doit être >= 0")
    @Max(value = 3, message = "La protection doit être <= 3")
    private Integer protection;

    @NotNull(message = "Le niveau de prévention est obligatoire")
    @Min(value = 0, message = "La prévention doit être >= 0")
    @Max(value = 3, message = "La prévention doit être <= 3")
    private Integer prevention;

    // ================= CONTROLES =================

    @Size(
            max = 1000,
            message = "Les contrôles existants ne doivent pas dépasser 1000 caractères"
    )
    private String controleExistants;

    @Size(
            max = 1000,
            message = "Les contrôles inexistants ne doivent pas dépasser 1000 caractères"
    )
    private String controleInexistants;

    private Boolean dejaSurvenu = false;

    // ================= PLAN D'ACTION =================

    @Size(
            max = 1000,
            message = "La recommandation ne doit pas dépasser 1000 caractères"
    )
    private String recommandation;

    // ================= BONNES PRATIQUES =================

    @Size(
            max = 1000,
            message = "Les bonnes pratiques ne doivent pas dépasser 1000 caractères"
    )
    private String bonnesPratiques;

    // ================= CONTEXTE =================

    @NotBlank(message = "Le code du risque est obligatoire")
    private String codeRisque;

    private String matriculeAgent;

    // ================= DATES =================

    private LocalDate dateDebut;

    private LocalDate dateFin;

    // ================= GETTERS / SETTERS =================

    public Integer getImpactInherent() {
        return impactInherent;
    }

    public EvaluationRequest setImpactInherent(Integer impactInherent) {
        this.impactInherent = impactInherent;
        return this;
    }

    public Integer getProbabiliteInherente() {
        return probabiliteInherente;
    }

    public EvaluationRequest setProbabiliteInherente(Integer probabiliteInherente) {
        this.probabiliteInherente = probabiliteInherente;
        return this;
    }

    public Integer getProtection() {
        return protection;
    }

    public EvaluationRequest setProtection(Integer protection) {
        this.protection = protection;
        return this;
    }

    public Integer getPrevention() {
        return prevention;
    }

    public EvaluationRequest setPrevention(Integer prevention) {
        this.prevention = prevention;
        return this;
    }

    public String getControleExistants() {
        return controleExistants;
    }

    public EvaluationRequest setControleExistants(String controleExistants) {
        this.controleExistants = controleExistants;
        return this;
    }

    public String getControleInexistants() {
        return controleInexistants;
    }

    public EvaluationRequest setControleInexistants(String controleInexistants) {
        this.controleInexistants = controleInexistants;
        return this;
    }

    public Boolean getDejaSurvenu() {
        return dejaSurvenu;
    }

    public EvaluationRequest setDejaSurvenu(Boolean dejaSurvenu) {
        this.dejaSurvenu = dejaSurvenu;
        return this;
    }

    public String getRecommandation() {
        return recommandation;
    }

    public EvaluationRequest setRecommandation(String recommandation) {
        this.recommandation = recommandation;
        return this;
    }

    public String getBonnesPratiques() {
        return bonnesPratiques;
    }

    public EvaluationRequest setBonnesPratiques(String bonnesPratiques) {
        this.bonnesPratiques = bonnesPratiques;
        return this;
    }

    public String getCodeRisque() {
        return codeRisque;
    }

    public EvaluationRequest setCodeRisque(String codeRisque) {
        this.codeRisque = codeRisque;
        return this;
    }

    public String getMatriculeAgent() {
        return matriculeAgent;
    }

    public EvaluationRequest setMatriculeAgent(String matriculeAgent) {
        this.matriculeAgent = matriculeAgent;
        return this;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public EvaluationRequest setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public EvaluationRequest setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }
}
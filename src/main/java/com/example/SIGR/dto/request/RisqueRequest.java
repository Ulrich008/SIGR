package com.example.SIGR.dto.request;

import com.example.SIGR.entity.StatutRisque;
import com.example.SIGR.entity.TypeRisque;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public class RisqueRequest {

    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 200, message = "Le libellé ne doit pas dépasser 200 caractères")
    private String libelle;

    private List<String> causeProbable;

    private List<String> consequenceProbable;

    private List<String> bonnesPratiques;

    @NotNull(message = "Le statut est obligatoire")
    private StatutRisque statut;

    @NotNull(message = "La date d'identification est obligatoire")
    private LocalDate dateIdentification;

    @NotBlank(message = "Le code processus est obligatoire")
    private String codeProcessus;

    // 🔥 RENOMMÉ ICI
    private String codeCartographie;

    @NotNull(message = "Le type de risque est obligatoire")
    private TypeRisque typeRisque;

    // ================= GETTERS / SETTERS =================

    public String getCode() {
        return code;
    }

    public RisqueRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public RisqueRequest setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }


    public List<String> getCauseProbable() {
        return causeProbable;
    }

    public RisqueRequest setCauseProbable(List<String> causeProbable) {
        this.causeProbable = causeProbable;
        return this;
    }

    public List<String> getConsequenceProbable() {
        return consequenceProbable;
    }

    public RisqueRequest setConsequenceProbable(List<String> consequenceProbable) {
        this.consequenceProbable = consequenceProbable;
        return this;
    }

    public List<String> getBonnesPratiques() {
        return bonnesPratiques;
    }

    public RisqueRequest setBonnesPratiques(List<String> bonnesPratiques) {
        this.bonnesPratiques = bonnesPratiques;
        return this;
    }

    public StatutRisque getStatut() {
        return statut;
    }

    public RisqueRequest setStatut(StatutRisque statut) {
        this.statut = statut;
        return this;
    }

    public LocalDate getDateIdentification() {
        return dateIdentification;
    }

    public RisqueRequest setDateIdentification(LocalDate dateIdentification) {
        this.dateIdentification = dateIdentification;
        return this;
    }

    public String getCodeProcessus() {
        return codeProcessus;
    }

    public RisqueRequest setCodeProcessus(String codeProcessus) {
        this.codeProcessus = codeProcessus;
        return this;
    }

    // 🔥 GETTER CORRIGÉ
    public String getCodeCartographie() {
        return codeCartographie;
    }

    public RisqueRequest setCodeCartographie(String codeCartographie) {
        this.codeCartographie = codeCartographie;
        return this;
    }

    public TypeRisque getTypeRisque() {
        return typeRisque;
    }

    public RisqueRequest setTypeRisque(TypeRisque typeRisque) {
        this.typeRisque = typeRisque;
        return this;
    }
}
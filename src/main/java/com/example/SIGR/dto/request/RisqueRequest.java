package com.example.SIGR.dto.request;

import com.example.SIGR.entity.TypeRisque;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class RisqueRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String libelle;

    @NotBlank
    private String categorie;

    private String causeProbable;

    private String consequenceProbable;

    @NotBlank
    private String statut;

    @NotNull
    private LocalDate dateIdentification;

    /**
     * Code du processus lié au risque
     */
    @NotBlank
    private String codeProcessus;

    /**
     * Cartographie associée
     */
    private String idCartographie;

    /**
     * Type du risque
     */
    @NotNull
    private TypeRisque typeRisque;

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public RisqueRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public RisqueRequest setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getCategorie() {
        return categorie;
    }

    public RisqueRequest setCategorie(String categorie) {
        this.categorie = categorie;
        return this;
    }

    public String getCauseProbable() {
        return causeProbable;
    }

    public RisqueRequest setCauseProbable(String causeProbable) {
        this.causeProbable = causeProbable;
        return this;
    }

    public String getConsequenceProbable() {
        return consequenceProbable;
    }

    public RisqueRequest setConsequenceProbable(String consequenceProbable) {
        this.consequenceProbable = consequenceProbable;
        return this;
    }

    public String getStatut() {
        return statut;
    }

    public RisqueRequest setStatut(String statut) {
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

    public String getIdCartographie() {
        return idCartographie;
    }

    public RisqueRequest setIdCartographie(String idCartographie) {
        this.idCartographie = idCartographie;
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
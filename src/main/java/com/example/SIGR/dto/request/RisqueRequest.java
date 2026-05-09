package com.example.SIGR.dto.request;

import com.example.SIGR.entity.StatutRisque;
import com.example.SIGR.entity.TypeRisque;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class RisqueRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 200, message = "Le libellé ne doit pas dépasser 200 caractères")
    private String libelle;

    @NotBlank(message = "La catégorie est obligatoire")
    @Size(max = 100, message = "La catégorie ne doit pas dépasser 100 caractères")
    private String categorie;

    @Size(max = 1000, message = "La cause probable est trop longue")
    private String causeProbable;

    @Size(max = 1000, message = "La conséquence probable est trop longue")
    private String consequenceProbable;

    @NotNull(message = "Le statut est obligatoire")
    private StatutRisque statut;

    @NotNull(message = "La date d'identification est obligatoire")
    private LocalDate dateIdentification;

    @NotBlank(message = "Le code processus est obligatoire")
    private String codeProcessus;

    private String idCartographie;

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
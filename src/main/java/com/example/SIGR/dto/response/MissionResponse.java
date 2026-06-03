package com.example.SIGR.dto.response;

import java.time.LocalDate;

public class MissionResponse {

    private String id;
    private String code;
    private String libelle;
    private String description;
    private String codeProcessus;
    private String nomProcessus;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statut;
    private String codeResponsable;
    private String nomResponsable;

    public MissionResponse() {
    }

    public MissionResponse(String id, String code, String libelle, String description,
                           String codeProcessus, String nomProcessus,
                           LocalDate dateDebut, LocalDate dateFin,
                           String statut, String codeResponsable, String nomResponsable) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.description = description;
        this.codeProcessus = codeProcessus;
        this.nomProcessus = nomProcessus;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.codeResponsable = codeResponsable;
        this.nomResponsable = nomResponsable;
    }

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public MissionResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public MissionResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public MissionResponse setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MissionResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCodeProcessus() {
        return codeProcessus;
    }

    public MissionResponse setCodeProcessus(String codeProcessus) {
        this.codeProcessus = codeProcessus;
        return this;
    }

    public String getNomProcessus() {
        return nomProcessus;
    }

    public MissionResponse setNomProcessus(String nomProcessus) {
        this.nomProcessus = nomProcessus;
        return this;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public MissionResponse setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public MissionResponse setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public String getStatut() {
        return statut;
    }

    public MissionResponse setStatut(String statut) {
        this.statut = statut;
        return this;
    }

    public String getCodeResponsable() {
        return codeResponsable;
    }

    public MissionResponse setCodeResponsable(String codeResponsable) {
        this.codeResponsable = codeResponsable;
        return this;
    }

    public String getNomResponsable() {
        return nomResponsable;
    }

    public MissionResponse setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
        return this;
    }
}

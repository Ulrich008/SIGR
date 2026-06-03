package com.example.SIGR.dto.request;

import java.time.LocalDate;

public class MissionRequest {

    private String code;
    private String libelle;
    private String description;
    private String codeProcessus;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statut;
    private String codeResponsable;

    // ================= GETTERS / SETTERS =================

    public String getCode() {
        return code;
    }

    public MissionRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public MissionRequest setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MissionRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCodeProcessus() {
        return codeProcessus;
    }

    public MissionRequest setCodeProcessus(String codeProcessus) {
        this.codeProcessus = codeProcessus;
        return this;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public MissionRequest setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public MissionRequest setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public String getStatut() {
        return statut;
    }

    public MissionRequest setStatut(String statut) {
        this.statut = statut;
        return this;
    }

    public String getCodeResponsable() {
        return codeResponsable;
    }

    public MissionRequest setCodeResponsable(String codeResponsable) {
        this.codeResponsable = codeResponsable;
        return this;
    }
}

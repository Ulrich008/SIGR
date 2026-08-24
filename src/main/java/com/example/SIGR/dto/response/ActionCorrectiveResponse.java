package com.example.SIGR.dto.response;

import java.time.LocalDate;

public class ActionCorrectiveResponse {

    private String libelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    public ActionCorrectiveResponse(String libelle, LocalDate dateDebut, LocalDate dateFin) {
        this.libelle = libelle;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public String getLibelle() { return libelle; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
}

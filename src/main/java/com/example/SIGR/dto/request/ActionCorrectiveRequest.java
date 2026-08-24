package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ActionCorrectiveRequest {

    @NotBlank(message = "Le libellé de l'action correctrice est obligatoire")
    @Size(max = 500, message = "Le libellé ne doit pas dépasser 500 caractères")
    private String libelle;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    public String getLibelle() { return libelle; }
    public ActionCorrectiveRequest setLibelle(String libelle) { this.libelle = libelle; return this; }

    public LocalDate getDateDebut() { return dateDebut; }
    public ActionCorrectiveRequest setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; return this; }

    public LocalDate getDateFin() { return dateFin; }
    public ActionCorrectiveRequest setDateFin(LocalDate dateFin) { this.dateFin = dateFin; return this; }
}

package com.example.SIGR.entity;

import java.time.LocalDate;

/**
 * Élément d'une liste d'actions correctrices (libellé + date de début + date
 * de fin) — pas une entité JPA à part entière : {@link RapportControleInterne}
 * encode la liste dans son propre champ texte (voir
 * getActionsCorrectivesList()/setActionsCorrectivesList()), cette classe ne
 * sert qu'à porter la valeur d'un élément entre l'entité et les DTO.
 */
public class ActionCorrective {

    private String libelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    public ActionCorrective() {
    }

    public ActionCorrective(String libelle, LocalDate dateDebut, LocalDate dateFin) {
        this.libelle = libelle;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public String getLibelle() { return libelle; }
    public ActionCorrective setLibelle(String libelle) { this.libelle = libelle; return this; }

    public LocalDate getDateDebut() { return dateDebut; }
    public ActionCorrective setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; return this; }

    public LocalDate getDateFin() { return dateFin; }
    public ActionCorrective setDateFin(LocalDate dateFin) { this.dateFin = dateFin; return this; }
}

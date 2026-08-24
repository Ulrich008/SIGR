package com.example.SIGR.dto.response;

import com.example.SIGR.entity.StatutRapportCI;

import java.time.LocalDateTime;

/**
 * Une entrée de l'historique des avis de validation d'un rapport de contrôle
 * interne (Transmis, Validé, Différé, Rejeté...), reconstituée à partir des
 * révisions Hibernate Envers de l'entité RapportControleInterne — voir
 * RapportControleInterneServiceImpl.getHistoriqueAvis().
 *
 * Nécessaire pour la même raison que AvisHistoriqueResponse (côté Risque) :
 * statut/motif sont de simples colonnes, écrasées à chaque nouvelle décision
 * de la CCI. C'est même la SEULE trace qui subsiste une fois qu'un rapport
 * différé/rejeté est supprimé par le Contrôleur Interne (voir
 * RapportControleInterneServiceImpl.delete) — l'enregistrement disparaît de
 * la table, mais ses révisions Envers, elles, restent consultables.
 */
public class AvisHistoriqueRapportCIResponse {

    private LocalDateTime date;
    private StatutRapportCI statut;
    private String motif;
    private String matriculeAuteur;
    private String nomAuteur;

    public AvisHistoriqueRapportCIResponse(
            LocalDateTime date,
            StatutRapportCI statut,
            String motif,
            String matriculeAuteur,
            String nomAuteur
    ) {
        this.date = date;
        this.statut = statut;
        this.motif = motif;
        this.matriculeAuteur = matriculeAuteur;
        this.nomAuteur = nomAuteur;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public StatutRapportCI getStatut() {
        return statut;
    }

    public String getMotif() {
        return motif;
    }

    public String getMatriculeAuteur() {
        return matriculeAuteur;
    }

    public String getNomAuteur() {
        return nomAuteur;
    }
}

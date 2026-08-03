package com.example.SIGR.dto.response;

import com.example.SIGR.entity.AvisRisque;
import com.example.SIGR.entity.EtapeValidation;

import java.time.LocalDateTime;

/**
 * Une entrée de l'historique des avis de validation d'un risque (Transmis,
 * Validé, Différé, Rejeté...), reconstituée à partir des révisions Hibernate
 * Envers de l'entité Risque — voir RisqueServiceImpl.getHistoriqueAvis().
 *
 * Nécessaire car avis/motif/etapeValidation sont de simples colonnes sur
 * Risque, écrasées à chaque nouvelle décision : sans cet historique, seul le
 * dernier avis reste consultable si un dossier fait plusieurs allers-retours
 * dans le circuit de validation.
 */
public class AvisHistoriqueResponse {

    private LocalDateTime date;
    private AvisRisque avis;
    private String motif;
    private EtapeValidation etapeValidation;
    private String matriculeAuteur;
    private String nomAuteur;

    public AvisHistoriqueResponse(
            LocalDateTime date,
            AvisRisque avis,
            String motif,
            EtapeValidation etapeValidation,
            String matriculeAuteur,
            String nomAuteur
    ) {
        this.date = date;
        this.avis = avis;
        this.motif = motif;
        this.etapeValidation = etapeValidation;
        this.matriculeAuteur = matriculeAuteur;
        this.nomAuteur = nomAuteur;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public AvisRisque getAvis() {
        return avis;
    }

    public String getMotif() {
        return motif;
    }

    public EtapeValidation getEtapeValidation() {
        return etapeValidation;
    }

    public String getMatriculeAuteur() {
        return matriculeAuteur;
    }

    public String getNomAuteur() {
        return nomAuteur;
    }
}

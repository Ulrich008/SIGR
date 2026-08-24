package com.example.SIGR.dto.request;

import com.example.SIGR.entity.StatutRapportCI;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Requête dédiée à l'avis de la CCI sur un rapport de contrôle interne
 * transmis (Valider / Différer / Rejeter). Seules les valeurs VALIDE,
 * DIFFERE et REJETE sont acceptées — validé en service, cf.
 * RapportControleInterneServiceImpl.validerAvis.
 */
public class AvisRapportCIRequest {

    @NotNull(message = "L'avis est obligatoire")
    private StatutRapportCI avis;

    @Size(max = 1000, message = "Le motif ne doit pas dépasser 1000 caractères")
    private String motif;

    public StatutRapportCI getAvis() { return avis; }
    public AvisRapportCIRequest setAvis(StatutRapportCI avis) { this.avis = avis; return this; }

    public String getMotif() { return motif; }
    public AvisRapportCIRequest setMotif(String motif) { this.motif = motif; return this; }
}

package com.example.SIGR.dto.request;

import com.example.SIGR.entity.AvisRisque;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Requête dédiée à l'action Valider / Différer / Rejeter d'un risque
 * (CMMR, CCI, Pilote de processus). Volontairement plus restreinte que
 * RisqueRequest : ces profils ne peuvent modifier ni le contenu du
 * risque, ni ses autres champs — uniquement se prononcer dessus.
 */
public class AvisRisqueRequest {

    @NotNull(message = "L'avis est obligatoire")
    private AvisRisque avis;

    @Size(max = 1000, message = "Le motif ne doit pas dépasser 1000 caractères")
    private String motif;

    public AvisRisque getAvis() {
        return avis;
    }

    public AvisRisqueRequest setAvis(AvisRisque avis) {
        this.avis = avis;
        return this;
    }

    public String getMotif() {
        return motif;
    }

    public AvisRisqueRequest setMotif(String motif) {
        this.motif = motif;
        return this;
    }
}

package com.example.SIGR.dto.request;

import jakarta.validation.constraints.Size;

/**
 * Décision de la CCI sur le suivi des recommandations d'un rapport de
 * contrôle interne — réservée à la CCI, distincte du statut d'avancement
 * renseigné par le Contrôleur Interne (voir {@link StatutSuiviRequest}).
 */
public class DecisionSuiviRequest {

    @Size(max = 1000, message = "La décision ne doit pas dépasser 1000 caractères")
    private String decision;

    public String getDecision() {
        return decision;
    }

    public DecisionSuiviRequest setDecision(String decision) {
        this.decision = decision;
        return this;
    }
}

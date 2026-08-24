package com.example.SIGR.dto.request;

import com.example.SIGR.entity.StatutSuiviRecommandation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Enregistrement du suivi d'une recommandation (audit ou contrôle interne) :
 * statut d'avancement + décision libre, utilisé par les deux sous-menus
 * "Suivi des recommandations d'audit" et "Suivi des Recommandations des CI".
 */
public class SuiviRecommandationRequest {

    @NotNull(message = "Le statut de suivi est obligatoire")
    private StatutSuiviRecommandation statutSuivi;

    @Size(max = 1000, message = "La décision ne doit pas dépasser 1000 caractères")
    private String decision;

    public StatutSuiviRecommandation getStatutSuivi() {
        return statutSuivi;
    }

    public SuiviRecommandationRequest setStatutSuivi(StatutSuiviRecommandation statutSuivi) {
        this.statutSuivi = statutSuivi;
        return this;
    }

    public String getDecision() {
        return decision;
    }

    public SuiviRecommandationRequest setDecision(String decision) {
        this.decision = decision;
        return this;
    }
}

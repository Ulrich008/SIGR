package com.example.SIGR.dto.request;

import com.example.SIGR.entity.StatutSuiviRecommandation;
import jakarta.validation.constraints.NotNull;

/**
 * Mise à jour du statut d'avancement du suivi d'un rapport de contrôle
 * interne — réservée au Contrôleur Interne, distincte de la décision de la
 * CCI (voir {@link DecisionSuiviRequest}).
 */
public class StatutSuiviRequest {

    @NotNull(message = "Le statut de suivi est obligatoire")
    private StatutSuiviRecommandation statutSuivi;

    public StatutSuiviRecommandation getStatutSuivi() {
        return statutSuivi;
    }

    public StatutSuiviRequest setStatutSuivi(StatutSuiviRecommandation statutSuivi) {
        this.statutSuivi = statutSuivi;
        return this;
    }
}

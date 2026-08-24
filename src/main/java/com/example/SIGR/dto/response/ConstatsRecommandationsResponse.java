package com.example.SIGR.dto.response;

import java.util.List;

/**
 * Agrégation, pour un couple Unité administrative + Processus, de tous les
 * constats et recommandations saisis dans les ControleSecondNiveau
 * correspondants — alimente l'étape 2 (lecture seule) du formulaire Rapport
 * de contrôle interne. Voir RapportControleInterneServiceImpl / ControleSecondNiveauServiceImpl.
 */
public class ConstatsRecommandationsResponse {

    private List<String> constats;
    private List<String> recommandations;

    public ConstatsRecommandationsResponse(List<String> constats, List<String> recommandations) {
        this.constats = constats;
        this.recommandations = recommandations;
    }

    public List<String> getConstats() { return constats; }
    public List<String> getRecommandations() { return recommandations; }
}

package com.example.SIGR.entity;

/**
 * Statut d'avancement du suivi d'une recommandation (contrôle interne ou
 * audit). {@code null} tant qu'aucun suivi n'a encore été renseigné.
 */
public enum StatutSuiviRecommandation {
    NON_ENTAME,
    EN_COURS,
    REALISEE,
    NON_REALISEE
}

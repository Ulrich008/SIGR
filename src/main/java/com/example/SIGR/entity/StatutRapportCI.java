package com.example.SIGR.entity;

/**
 * Statut d'un rapport de contrôle interne, à partir de la génération de son
 * PDF (avant ça, {@code statut == null} : le rapport est encore un
 * brouillon en cours de saisie, pas encore visible en Transmission).
 *
 * Circuit : EN_ATTENTE_DE_VALIDATION -[transmettre]-> TRANSMIS -[avis CCI]->
 * VALIDE (diffusé au Responsable des risques et au CMMR) ou DIFFERE/REJETE
 * (motif obligatoire, revient au Contrôleur Interne qui supprime le rapport
 * et en recrée un nouveau).
 */
public enum StatutRapportCI {
    EN_ATTENTE_DE_VALIDATION,
    TRANSMIS,
    VALIDE,
    DIFFERE,
    REJETE
}

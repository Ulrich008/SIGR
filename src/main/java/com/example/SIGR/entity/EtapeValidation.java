package com.example.SIGR.entity;

/**
 * Étape actuelle du circuit de validation d'un risque :
 * Responsable des risques (formalisation) -> Pilote -> CCI -> CMMR.
 *
 * - Sur "Valider" : passage à l'étape suivante (CMMR validé -> VALIDEE).
 * - Sur "Différer" : retour à l'étape précédente.
 * - Sur "Rejeter" : clôture définitive (REJETEE), pas de retour automatique.
 */
public enum EtapeValidation {
    FORMALISATION,
    PILOTE,
    CCI,
    CMMR,
    VALIDEE,
    REJETEE
}

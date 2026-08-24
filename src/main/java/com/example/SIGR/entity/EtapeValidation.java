package com.example.SIGR.entity;

/**
 * Étape actuelle du circuit de validation d'un risque :
 * Correspondant Risque (formalisation) -[transmet]-> Manager Risque
 * -[transmet]-> CCI -[transmet]-> Responsable Risque (avis : visa) -[si visa
 * validé, transmet]-> CCI -[transmet]-> CMMR (avis : validation finale).
 *
 * Manager Risque et CCI ne font que relayer le dossier (action "Transmettre",
 * sans motif) ; CCI apparaît deux fois dans le circuit car il relaie d'abord
 * vers le Responsable puis, une fois le visa obtenu, vers le CMMR — d'où les
 * deux valeurs distinctes CCI_VERS_RESPONSABLE et CCI_VERS_CMMR. Seuls le
 * Responsable Risque et le CMMR rendent un véritable avis (Valider/Différer/
 * Rejeter, motif obligatoire pour ces deux derniers) :
 * - "Valider" (Responsable) : passage à CCI_VERS_CMMR.
 * - "Valider" (CMMR) : passage à VALIDEE.
 * - "Différer" (Responsable ou CMMR) : retour à MANAGER_RISQUE, qui relaie
 *   ensuite explicitement le dossier au Correspondant pour correction
 *   (retour à FORMALISATION).
 * - "Rejeter" (Responsable ou CMMR) : clôture définitive (REJETEE), pas de
 *   retour automatique.
 */
public enum EtapeValidation {
    FORMALISATION,
    MANAGER_RISQUE,
    CCI_VERS_RESPONSABLE,
    RESPONSABLE,
    CCI_VERS_CMMR,
    CMMR,
    VALIDEE,
    REJETEE
}

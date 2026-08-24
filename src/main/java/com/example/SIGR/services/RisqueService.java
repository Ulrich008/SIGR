package com.example.SIGR.services;

import com.example.SIGR.dto.request.AvisRisqueRequest;
import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.request.SuiviRecommandationRequest;
import com.example.SIGR.dto.response.AvisHistoriqueResponse;
import com.example.SIGR.dto.response.RisqueResponse;

import java.util.List;

public interface RisqueService {

    RisqueResponse create(RisqueRequest request);

    RisqueResponse getByCode(String code);

    List<RisqueResponse> getAll();

    RisqueResponse updateByCode(String code, RisqueRequest request);

    /**
     * Action Valider / Différer / Rejeter (CMMR, CCI, Pilote de
     * processus) : ne touche qu'à l'avis et au motif, jamais au
     * contenu du risque.
     */
    RisqueResponse validerAvis(String code, AvisRisqueRequest request);

    /**
     * Transmission du dossier par le Responsable des risques : fait
     * entrer le risque dans le circuit de validation (étape Pilote).
     */
    RisqueResponse transmettre(String code);

    void deleteByCode(String code);

    /**
     * Clôture manuelle du risque par le CCI, indépendante du circuit de
     * validation de la cartographie et possible à n'importe quelle étape.
     */
    RisqueResponse cloturer(String code);

    /**
     * Historique complet des avis de validation (Transmis, Validé, Différé,
     * Rejeté) de ce risque, reconstitué depuis les révisions Envers —
     * contrairement à RisqueResponse.motif/avis (uniquement le dernier en
     * date), couvre tous les allers-retours passés dans le circuit.
     */
    List<AvisHistoriqueResponse> getHistoriqueAvis(String code);

    /**
     * Suivi des actions de mitigation du risque (menu "Suivi des Risques" >
     * "Suivi des actions de mitigations") : statut d'avancement + décision,
     * distinct du circuit de validation (avis/motif).
     */
    RisqueResponse enregistrerSuivi(String code, SuiviRecommandationRequest request);
}
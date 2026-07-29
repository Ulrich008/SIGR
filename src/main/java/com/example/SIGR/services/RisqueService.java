package com.example.SIGR.services;

import com.example.SIGR.dto.request.AvisRisqueRequest;
import com.example.SIGR.dto.request.RisqueRequest;
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
}
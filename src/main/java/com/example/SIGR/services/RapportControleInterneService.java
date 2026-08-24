package com.example.SIGR.services;

import com.example.SIGR.dto.request.AvisRapportCIRequest;
import com.example.SIGR.dto.request.DecisionSuiviRequest;
import com.example.SIGR.dto.request.RapportControleInterneRequest;
import com.example.SIGR.dto.request.StatutSuiviRequest;
import com.example.SIGR.dto.response.AvisHistoriqueRapportCIResponse;
import com.example.SIGR.dto.response.RapportControleInterneResponse;

import java.util.List;

public interface RapportControleInterneService {

    RapportControleInterneResponse create(RapportControleInterneRequest request);

    RapportControleInterneResponse getByCode(String code);

    List<RapportControleInterneResponse> getAll();

    void delete(String code);

    /** Construit le PDF, le stocke, et fait passer le statut à EN_ATTENTE_DE_VALIDATION. */
    RapportControleInterneResponse genererPdf(String code);

    byte[] getPdf(String code);

    /** EN_ATTENTE_DE_VALIDATION -> TRANSMIS (réservé au Contrôleur Interne créateur / SUPER_ADMIN). */
    RapportControleInterneResponse transmettre(String code);

    /** Avis de la CCI sur un rapport TRANSMIS : VALIDE, DIFFERE ou REJETE (motif obligatoire pour ces deux derniers). */
    RapportControleInterneResponse validerAvis(String code, AvisRapportCIRequest request);

    /** Statut d'avancement du suivi des recommandations, réservé au Contrôleur Interne. */
    RapportControleInterneResponse enregistrerStatutSuivi(String code, StatutSuiviRequest request);

    /** Décision de la CCI sur le suivi des recommandations, réservée à la CCI. */
    RapportControleInterneResponse enregistrerDecisionSuivi(String code, DecisionSuiviRequest request);

    /**
     * Historique complet des avis de validation (Transmis, Validé, Différé,
     * Rejeté), y compris pour un rapport déjà supprimé depuis (voir
     * RapportControleInterneServiceImpl.delete) : reconstitué à partir des
     * révisions Envers, recherchées par code plutôt que par id, puisque
     * l'enregistrement lui-même peut ne plus exister.
     */
    List<AvisHistoriqueRapportCIResponse> getHistoriqueAvis(String code);
}

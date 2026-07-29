package com.example.SIGR.services;

import com.example.SIGR.dto.request.PlanMitigationRequest;
import com.example.SIGR.dto.response.PlanMitigationResponse;

import java.util.List;

public interface PlanMitigationService {

    PlanMitigationResponse create(PlanMitigationRequest request);

    PlanMitigationResponse getByCode(String code);

    List<PlanMitigationResponse> getAll();



   PlanMitigationResponse updateByCode(String code, PlanMitigationRequest request);



    void deleteByCode(String code);

    /**
     * Clôture un plan de mitigation, réservé au CCI. Une fois clôturé, le
     * statut n'est plus recalculé automatiquement (voir recalculerStatut).
     */
    PlanMitigationResponse cloturer(String code);

    /**
     * Recalcule le statut d'un plan à partir de ses actions liées :
     * aucune action → PLANIFIE, au moins une action non terminée → EN_COURS,
     * toutes les actions terminées → TERMINE. Un plan déjà CLOTURE n'est
     * jamais touché par ce recalcul (clôture manuelle définitive du CCI).
     * Appelé par ActionServiceImpl à chaque création/modification/suppression
     * d'une action.
     */
    void recalculerStatut(String codePlanMitigation);
}
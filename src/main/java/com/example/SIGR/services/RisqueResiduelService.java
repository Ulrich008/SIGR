package com.example.SIGR.services;

import com.example.SIGR.dto.request.RisqueResiduelRequest;
import com.example.SIGR.dto.response.RisqueResiduelResponse;

import java.util.List;

public interface RisqueResiduelService {

    /**
     * ================= CREATE =================
     */
    RisqueResiduelResponse create(
            RisqueResiduelRequest request
    );

    /**
     * ================= GET ALL =================
     */
    List<RisqueResiduelResponse> getAll();

    /**
     * ================= GET BY CODE =================
     */
    RisqueResiduelResponse getByCode(
            String code
    );

    /**
     * ================= UPDATE =================
     */
    RisqueResiduelResponse updateByCode(
            String code,
            RisqueResiduelRequest request
    );

    /**
     * ================= DELETE =================
     */
    void deleteByCode(
            String code
    );

    /**
     * ================= BY EVALUATION =================
     */
    List<RisqueResiduelResponse> getByEvaluation(
            String codeEvaluation
    );

    /**
     * ================= BY RISQUE =================
     */
    List<RisqueResiduelResponse> getByRisque(
            String codeRisque
    );

    /**
     * ================= RISQUES ELEVES =================
     */
    List<RisqueResiduelResponse> getRisquesEleves();
}
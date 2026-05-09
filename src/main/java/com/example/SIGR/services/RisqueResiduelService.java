package com.example.SIGR.services;

import com.example.SIGR.dto.request.RisqueResiduelRequest;
import com.example.SIGR.dto.response.RisqueResiduelResponse;

import java.util.List;

public interface RisqueResiduelService {

    // ================= CRUD =================
    RisqueResiduelResponse create(RisqueResiduelRequest request);

    RisqueResiduelResponse getById(String id);

    List<RisqueResiduelResponse> getAll();

    RisqueResiduelResponse updateBycode(String code, RisqueResiduelRequest request);


    void deleteBycode(String code);

    RisqueResiduelResponse getByCode(String code);

    List<RisqueResiduelResponse> getByEvaluation(String idEvaluation);

    List<RisqueResiduelResponse> getByRisque(String idRisque);

    List<RisqueResiduelResponse> getRisquesElevés();
}
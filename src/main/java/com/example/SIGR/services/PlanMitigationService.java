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
}
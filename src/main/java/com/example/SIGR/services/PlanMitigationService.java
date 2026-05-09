package com.example.SIGR.services;

import com.example.SIGR.dto.request.PlanMitigationRequest;
import com.example.SIGR.dto.response.PlanMitigationResponse;

import java.util.List;

public interface PlanMitigationService {

    PlanMitigationResponse create(PlanMitigationRequest request);

    PlanMitigationResponse getById(String id);

    PlanMitigationResponse getByCode(String code);

    List<PlanMitigationResponse> getAll();

    PlanMitigationResponse updateById(String id, PlanMitigationRequest request);

   /*PlanMitigationResponse updateByCode(String code, PlanMitigationRequest request);*/

      void deleteById(String id);

    /*void deleteByCode(String code);*/
}
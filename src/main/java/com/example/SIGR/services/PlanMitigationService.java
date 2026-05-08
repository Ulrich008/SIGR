package com.example.SIGR.services;

import com.example.SIGR.dto.request.PlanMitigationRequest;
import com.example.SIGR.dto.response.PlanMitigationResponse;

import java.util.List;

public interface PlanMitigationService {

    PlanMitigationResponse create(PlanMitigationRequest request);

    PlanMitigationResponse getById(String id);

    List<PlanMitigationResponse> getAll();

    PlanMitigationResponse update(String id, PlanMitigationRequest request);

    void delete(String id);
}
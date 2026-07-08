package com.example.SIGR.services;

import com.example.SIGR.dto.request.PlanAuditRequest;
import com.example.SIGR.dto.response.PlanAuditResponse;

import java.util.List;

public interface PlanAuditService {

    PlanAuditResponse create(PlanAuditRequest request);

    PlanAuditResponse getByCode(String code);

    List<PlanAuditResponse> getAll();

    PlanAuditResponse update(String code, PlanAuditRequest request);

    void delete(String code);
}

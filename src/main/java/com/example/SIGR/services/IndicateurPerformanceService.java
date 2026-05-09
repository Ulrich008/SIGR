package com.example.SIGR.services;

import com.example.SIGR.dto.request.IndicateurPerformanceRequest;
import com.example.SIGR.dto.response.IndicateurPerformanceResponse;

import java.util.List;

public interface IndicateurPerformanceService {

    IndicateurPerformanceResponse create(IndicateurPerformanceRequest request);

    IndicateurPerformanceResponse getById(String id);

    /*IndicateurPerformanceResponse getByCode(String code);*/

    List<IndicateurPerformanceResponse> getAll();

    IndicateurPerformanceResponse update(String id, IndicateurPerformanceRequest request);

    void delete(String id);
}
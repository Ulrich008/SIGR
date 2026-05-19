
package com.example.SIGR.services;

import com.example.SIGR.dto.request.IndicateurPerformanceRequest;
import com.example.SIGR.dto.response.IndicateurPerformanceResponse;

import java.util.List;

public interface IndicateurPerformanceService {

    /**
     * ================= CREATE =================
     */
    IndicateurPerformanceResponse create(
            IndicateurPerformanceRequest request
    );

    /**
     * ================= GET BY CODE =================
     */
    IndicateurPerformanceResponse getByCode(
            String code
    );

    /**
     * ================= GET ALL =================
     */
    List<IndicateurPerformanceResponse> getAll();

    /**
     * ================= UPDATE =================
     */
    IndicateurPerformanceResponse update(
            String code,
            IndicateurPerformanceRequest request
    );

    /**
     * ================= DELETE =================
     */
    void delete(
            String code
    );
}

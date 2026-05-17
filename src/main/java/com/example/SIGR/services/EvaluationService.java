package com.example.SIGR.services;

import com.example.SIGR.dto.request.EvaluationRequest;
import com.example.SIGR.dto.response.EvaluationResponse;

import java.util.List;

public interface EvaluationService {

    // Création
    EvaluationResponse create(EvaluationRequest request);

    EvaluationResponse getByCode(String code)
            ;
    List<EvaluationResponse> getAll();

    EvaluationResponse update(String id, EvaluationRequest request);


    void delete(String id);
}
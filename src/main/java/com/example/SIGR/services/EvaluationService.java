package com.example.SIGR.services;

import com.example.SIGR.dto.request.EvaluationRequest;
import com.example.SIGR.dto.response.EvaluationResponse;

import java.util.List;

public interface EvaluationService {

    EvaluationResponse create(EvaluationRequest request);

    EvaluationResponse getById(String id);

    List<EvaluationResponse> getAll();

    EvaluationResponse update(String id, EvaluationRequest request);

    void delete(String id);
}
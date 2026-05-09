package com.example.SIGR.services;

import com.example.SIGR.dto.request.AffectationRequest;
import com.example.SIGR.dto.response.AffectationResponse;

import java.util.List;

public interface AffectationService {

    AffectationResponse create(AffectationRequest request);

    AffectationResponse getByCode(String code);

    List<AffectationResponse> getAll();

    AffectationResponse update(String code, AffectationRequest request);

    void delete(String code);
}
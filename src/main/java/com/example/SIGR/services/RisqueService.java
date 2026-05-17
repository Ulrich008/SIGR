package com.example.SIGR.services;

import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.response.RisqueResponse;

import java.util.List;

public interface RisqueService {

    RisqueResponse create(RisqueRequest request);

    RisqueResponse getByCode(String code);

    List<RisqueResponse> getAll();

    RisqueResponse updateByCode(String code, RisqueRequest request);

    void deleteByCode(String code);
}
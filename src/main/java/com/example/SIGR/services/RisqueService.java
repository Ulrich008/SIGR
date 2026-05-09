package com.example.SIGR.services;

import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.response.RisqueResponse;

import java.util.List;

public interface RisqueService {

    RisqueResponse create(RisqueRequest request);

    RisqueResponse getById(String id);

    RisqueResponse getByCode(String code);

    List<RisqueResponse> getAll();

    RisqueResponse updateById(String id, RisqueRequest request);

    RisqueResponse updateByCode(String code, RisqueRequest request);

    void delete(String id);

    void deleteByCode(String code);
}
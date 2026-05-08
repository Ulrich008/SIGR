package com.example.SIGR.services;

import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.response.RisqueResponse;

import java.util.List;

public interface RisqueService {

    RisqueResponse create(RisqueRequest request);

    RisqueResponse getById(String id);

    List<RisqueResponse> getAll();

    RisqueResponse update(String id, RisqueRequest request);

    void delete(String id);
}
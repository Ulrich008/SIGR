package com.example.SIGR.services;

import com.example.SIGR.dto.request.MinistereRequest;
import com.example.SIGR.dto.response.MinistereResponse;

import java.util.List;

public interface MinistereService {

    MinistereResponse create(MinistereRequest request);
    MinistereResponse getById(String code);
    List<MinistereResponse> getAll();
    MinistereResponse update(String code, MinistereRequest request);
    void delete(String code);
}
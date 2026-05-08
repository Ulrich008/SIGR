package com.example.SIGR.services;

import com.example.SIGR.dto.request.CartographieRisquesRequest;
import com.example.SIGR.dto.response.CartographieRisquesResponse;

import java.util.List;

public interface CartographieRisquesService {

    CartographieRisquesResponse create(CartographieRisquesRequest request);

    CartographieRisquesResponse getById(String id);

    List<CartographieRisquesResponse> getAll();

    CartographieRisquesResponse update(String id, CartographieRisquesRequest request);

    void delete(String id);
}
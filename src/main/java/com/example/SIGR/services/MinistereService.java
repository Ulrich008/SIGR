package com.example.SIGR.services;

import com.example.SIGR.dto.request.MinistereRequest;
import com.example.SIGR.dto.response.MinistereResponse;

import java.util.List;

public interface MinistereService {

    MinistereResponse create(MinistereRequest request);

    MinistereResponse getById(String id);

    List<MinistereResponse> getAll();

    MinistereResponse update(String id, MinistereRequest request);

    void delete(String id);

    // optionnel (très utile)
    MinistereResponse getByCode (String code);

    // Récupérer le code du ministère de l'utilisateur connecté
    String getCodeMinistereOfCurrentUser();
}
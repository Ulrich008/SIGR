package com.example.SIGR.services;

import com.example.SIGR.dto.request.UniteAdministrativeRequest;
import com.example.SIGR.dto.response.UniteAdministrativeResponse;
import java.util.List;

public interface UniteAdministrativeService {

    UniteAdministrativeResponse create(UniteAdministrativeRequest request);
    UniteAdministrativeResponse getById(String id);
    List<UniteAdministrativeResponse> getAll();
    UniteAdministrativeResponse update(String id, UniteAdministrativeRequest request);
    void delete(String id);
}
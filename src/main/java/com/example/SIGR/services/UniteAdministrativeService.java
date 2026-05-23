package com.example.SIGR.services;

import com.example.SIGR.dto.request.UniteAdministrativeRequest;
import com.example.SIGR.dto.response.UniteAdministrativeResponse;

import java.util.List;

public interface UniteAdministrativeService {

    UniteAdministrativeResponse create(UniteAdministrativeRequest request);

    UniteAdministrativeResponse getByCode(String code);

    List<UniteAdministrativeResponse> getAll();

    UniteAdministrativeResponse update(String code, UniteAdministrativeRequest request);

    void delete(String code);
}
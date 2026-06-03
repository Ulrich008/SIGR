package com.example.SIGR.services;

import com.example.SIGR.dto.request.UniteMesureRequest;
import com.example.SIGR.dto.response.UniteMesureResponse;

import java.util.List;

public interface UniteMesureService {

    UniteMesureResponse create(UniteMesureRequest request);

    UniteMesureResponse getByCode(String code);

    List<UniteMesureResponse> getAll();

    UniteMesureResponse update(String code, UniteMesureRequest request);

    void delete(String code);
}

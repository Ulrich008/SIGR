package com.example.SIGR.services;

import com.example.SIGR.dto.request.TypeUniteRequest;
import com.example.SIGR.dto.response.TypeUniteResponse;

import java.util.List;

public interface TypeUniteService {

    TypeUniteResponse create(TypeUniteRequest request);

    TypeUniteResponse getByCode(String code);

    List<TypeUniteResponse> getAll();

    TypeUniteResponse update(String id, TypeUniteRequest request);

    void delete(String id);
}
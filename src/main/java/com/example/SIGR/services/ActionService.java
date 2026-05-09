package com.example.SIGR.services;

import com.example.SIGR.dto.request.ActionRequest;
import com.example.SIGR.dto.response.ActionResponse;

import java.util.List;

public interface ActionService {

    ActionResponse create(ActionRequest request);

    ActionResponse getById(String id);

    ActionResponse getByCode(String code);

    List<ActionResponse> getAll();

    ActionResponse update(String id, ActionRequest request);

    void delete(String id);
}
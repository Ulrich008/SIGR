package com.example.SIGR.services;

import com.example.SIGR.dto.request.MissionRequest;
import com.example.SIGR.dto.response.MissionResponse;

import java.util.List;

public interface MissionService {

    MissionResponse create(MissionRequest request);

    MissionResponse getByCode(String code);

    List<MissionResponse> getAll();

    MissionResponse update(String code, MissionRequest request);

    void delete(String code);

    List<MissionResponse> getByProcessusId(String processusId);
}

package com.example.SIGR.services;

import com.example.SIGR.dto.request.ProcessusRequest;
import com.example.SIGR.dto.response.ProcessusResponse;

import java.util.List;

public interface ProcessusService {

    ProcessusResponse create(ProcessusRequest request);

    ProcessusResponse getByCode(String code);

    List<ProcessusResponse> getAll();

    ProcessusResponse updateByCode(String code, ProcessusRequest request);

    void deleteByCode(String code);
}
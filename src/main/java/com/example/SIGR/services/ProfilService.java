package com.example.SIGR.services;

import com.example.SIGR.dto.request.ProfilRequest;
import com.example.SIGR.dto.response.ProfilResponse;

import java.util.List;

public interface ProfilService {

    // ================= CREATE =================
    ProfilResponse create(ProfilRequest request);

    // ================= GET ALL =================
    List<ProfilResponse> getAll();

    // ================= GET BY CODE =================
    ProfilResponse getByCode(String code);

    // ================= UPDATE =================
    ProfilResponse update(String code, ProfilRequest request);

    // ================= DELETE =================
    void delete(String code);
}
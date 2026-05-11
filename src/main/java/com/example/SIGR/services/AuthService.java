package com.example.SIGR.services;

import com.example.SIGR.dto.request.LoginRequest;
import com.example.SIGR.dto.response.LoginResponse;

public interface AuthService {

    /**
     * Authentification d'un agent
     */
    LoginResponse login(LoginRequest request);
}
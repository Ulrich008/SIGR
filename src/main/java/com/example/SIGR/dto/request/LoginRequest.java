package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    // ================= GETTERS / SETTERS =================

    public String getMatricule() {
        return matricule;
    }

    public LoginRequest setMatricule(String matricule) {
        this.matricule = matricule;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
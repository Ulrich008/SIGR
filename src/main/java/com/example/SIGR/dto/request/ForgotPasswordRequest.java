package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {

    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;

    public String getMatricule() {
        return matricule;
    }

    public ForgotPasswordRequest setMatricule(String matricule) {
        this.matricule = matricule;
        return this;
    }
}

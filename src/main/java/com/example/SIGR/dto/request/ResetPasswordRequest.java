package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {

    @NotBlank(message = "Le jeton de réinitialisation est obligatoire")
    private String token;

    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String nouveauMotDePasse;

    public String getToken() {
        return token;
    }

    public ResetPasswordRequest setToken(String token) {
        this.token = token;
        return this;
    }

    public String getNouveauMotDePasse() {
        return nouveauMotDePasse;
    }

    public ResetPasswordRequest setNouveauMotDePasse(String nouveauMotDePasse) {
        this.nouveauMotDePasse = nouveauMotDePasse;
        return this;
    }
}

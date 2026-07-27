package com.example.SIGR.dto.request;

import jakarta.validation.constraints.Email;

/**
 * Modification de l'email par l'agent lui-même (self-service), depuis
 * la page "Mon profil". L'email reste optionnel (sert uniquement aux
 * notifications) : une valeur vide/nulle l'efface.
 */
public class ModifierEmailRequest {

    @Email(message = "Adresse email invalide")
    private String email;

    public String getEmail() {
        return email;
    }

    public ModifierEmailRequest setEmail(String email) {
        this.email = email;
        return this;
    }
}

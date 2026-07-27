package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Changement du mot de passe par l'agent lui-même (self-service) :
 * contrairement à AgentRequest utilisé par un ADMIN pour réinitialiser
 * le mot de passe d'un tiers, celui-ci exige la vérification de
 * l'ancien mot de passe.
 */
public class ChangerMotDePasseRequest {

    @NotBlank(message = "Le mot de passe actuel est obligatoire")
    private String ancienMotDePasse;

    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(min = 6, message = "Le nouveau mot de passe doit contenir au moins 6 caractères")
    private String nouveauMotDePasse;

    public String getAncienMotDePasse() {
        return ancienMotDePasse;
    }

    public ChangerMotDePasseRequest setAncienMotDePasse(String ancienMotDePasse) {
        this.ancienMotDePasse = ancienMotDePasse;
        return this;
    }

    public String getNouveauMotDePasse() {
        return nouveauMotDePasse;
    }

    public ChangerMotDePasseRequest setNouveauMotDePasse(String nouveauMotDePasse) {
        this.nouveauMotDePasse = nouveauMotDePasse;
        return this;
    }
}

package com.example.SIGR.services;

import com.example.SIGR.dto.request.ForgotPasswordRequest;
import com.example.SIGR.dto.request.LoginRequest;
import com.example.SIGR.dto.request.ResetPasswordRequest;
import com.example.SIGR.dto.response.LoginResponse;

public interface AuthService {

    /**
     * Authentification d'un agent
     */
    LoginResponse login(LoginRequest request);

    /**
     * Déclenche l'envoi d'un lien de réinitialisation de mot de passe à
     * l'email de l'agent, si le matricule existe et dispose d'un email.
     * Ne lève jamais d'exception et ne révèle jamais si le matricule
     * existe : la réponse HTTP est identique dans tous les cas (voir
     * AuthController), pour ne pas permettre l'énumération de comptes.
     */
    void motDePasseOublie(ForgotPasswordRequest request);

    /**
     * Réinitialise le mot de passe d'un agent à partir d'un jeton reçu par
     * email. Échoue si le jeton est introuvable, déjà utilisé ou expiré.
     */
    void reinitialiserMotDePasse(ResetPasswordRequest request);
}
package com.example.SIGR.controller;

import com.example.SIGR.dto.request.ForgotPasswordRequest;
import com.example.SIGR.dto.request.LoginRequest;
import com.example.SIGR.dto.request.ResetPasswordRequest;
import com.example.SIGR.dto.response.LoginResponse;
import com.example.SIGR.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "Authentification",
        description = "API d'authentification des agents"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * ================= LOGIN =================
     */
    @PostMapping("/login")
    @Operation(
            summary = "Connexion d'un agent",
            description = """
                    Permet à un agent de se connecter au système SIGR.
                    
                    Après authentification, un token JWT est retourné.
                    
                    Ce token devra être envoyé dans les requêtes sécurisées :
                    
                    Authorization: Bearer votre_token
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple login",
                                    value = """
                                            {
                                              "matricule": "AGT001",
                                              "password": "password123"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    /**
     * ================= MOT DE PASSE OUBLIÉ =================
     *
     * Réponse volontairement identique que le matricule existe ou non, et
     * qu'un email soit renseigné ou non — pour ne jamais permettre de
     * découvrir par ce biais si un matricule donné existe dans le système.
     */
    @PostMapping("/mot-de-passe-oublie")
    @Operation(
            summary = "Demander la réinitialisation de son mot de passe",
            description = """
                    Envoie un lien de réinitialisation à l'email de l'agent
                    correspondant au matricule fourni, s'il existe et dispose
                    d'un email. Répond toujours de la même façon, que le
                    matricule existe ou non.
                    """
    )
    public ResponseEntity<Map<String, String>> motDePasseOublie(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {

        authService.motDePasseOublie(request);

        return ResponseEntity.ok(Map.of(
                "message",
                "Si ce matricule existe et dispose d'un email, un lien de réinitialisation vient de lui être envoyé."
        ));
    }

    /**
     * ================= RÉINITIALISATION DU MOT DE PASSE =================
     */
    @PostMapping("/reinitialiser-mot-de-passe")
    @Operation(
            summary = "Réinitialiser son mot de passe via un jeton reçu par email",
            description = "Le jeton doit être valide, non expiré (1 heure) et non déjà utilisé."
    )
    public ResponseEntity<Map<String, String>> reinitialiserMotDePasse(
            @Valid @RequestBody ResetPasswordRequest request
    ) {

        authService.reinitialiserMotDePasse(request);

        return ResponseEntity.ok(Map.of(
                "message",
                "Votre mot de passe a été réinitialisé avec succès."
        ));
    }
}
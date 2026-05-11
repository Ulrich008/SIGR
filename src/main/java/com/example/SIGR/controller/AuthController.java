package com.example.SIGR.controller;

import com.example.SIGR.dto.request.LoginRequest;
import com.example.SIGR.dto.response.LoginResponse;
import com.example.SIGR.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
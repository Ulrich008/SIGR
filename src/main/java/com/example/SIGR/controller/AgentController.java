package com.example.SIGR.controller;

import com.example.SIGR.dto.request.AgentRequest;
import com.example.SIGR.dto.response.AgentResponse;
import com.example.SIGR.services.AgentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@Tag(
        name = "Agent",
        description = "API de gestion des agents"
)
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    /**
     * ================= CRÉATION =================
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Créer un agent",
            description = "Permet de créer un nouvel agent dans le système",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création agent",
                                    value = """
                                            {
                                              "matricule": "AGT001",
                                              "password": "password123",
                                              "npi": "1234567890",
                                              "nom": "ASSOGBA",
                                              "prenoms": "Ulrich",
                                              "sexe": "MASCULIN",
                                              "role": "AGENT",
                                              "dateNaissance": "1998-05-10",
                                              "datePriseService": "2024-01-15",
                                              "codeUnite": "DGB"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<AgentResponse> create(
            @Valid @RequestBody AgentRequest request
    ) {

        AgentResponse response = agentService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * ================= MON PROFIL =================
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    @Operation(
            summary = "Mon profil",
            description = "Retourne les informations de l'utilisateur connecté"
    )
    public ResponseEntity<AgentResponse> me(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                agentService.getByMatricule(
                        authentication.getName()
                )
        );
    }

    /**
     * ================= LISTE =================
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping
    @Operation(
            summary = "Lister tous les agents",
            description = "Retourne la liste complète des agents"
    )
    public ResponseEntity<List<AgentResponse>> getAll() {

        return ResponseEntity.ok(agentService.getAll());
    }

    /**
     * ================= RECHERCHE PAR ID =================
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping("/id/{id}")
    @Operation(
            summary = "Rechercher un agent par ID",
            description = "Retourne les informations d'un agent à partir de son ID"
    )
    public ResponseEntity<AgentResponse> getById(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                agentService.getById(id)
        );
    }

    /**
     * ================= RECHERCHE PAR MATRICULE =================
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping("/{matricule}")
    @Operation(
            summary = "Rechercher un agent par matricule",
            description = "Retourne les informations d'un agent à partir de son matricule"
    )
    public ResponseEntity<AgentResponse> getByMatricule(
            @PathVariable String matricule
    ) {

        return ResponseEntity.ok(
                agentService.getByMatricule(matricule)
        );
    }

    /**
     * ================= MODIFICATION =================
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{matricule}")
    @Operation(
            summary = "Modifier un agent",
            description = "Permet de modifier les informations d'un agent",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification agent",
                                    value = """
                                            {
                                              "password": "newPassword123",
                                              "npi": "9876543210",
                                              "nom": "ASSOGBA",
                                              "prenoms": "Ulrich Junior",
                                              "sexe": "MASCULIN",
                                              "role": "MANAGER",
                                              "dateNaissance": "1998-05-10",
                                              "datePriseService": "2024-01-15",
                                              "codeUnite": "DGB"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<AgentResponse> update(
            @PathVariable String matricule,
            @Valid @RequestBody AgentRequest request,
            Authentication authentication
    ) {

        String currentUser = authentication.getName();

        // Empêche un admin de modifier son propre rôle
        if (currentUser.equals(matricule)
                && request.getRole() != null) {

            throw new RuntimeException(
                    "Vous ne pouvez pas modifier votre propre rôle"
            );
        }

        return ResponseEntity.ok(
                agentService.update(matricule, request)
        );
    }

    /**
     * ================= ACTIVATION / DÉSACTIVATION =================
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{matricule}/status")
    @Operation(
            summary = "Activer ou désactiver un agent",
            description = "Permet d'activer ou désactiver le compte d'un agent"
    )
    public ResponseEntity<AgentResponse> changeStatus(
            @PathVariable String matricule,
            @RequestParam Boolean enabled,
            Authentication authentication
    ) {

        String currentUser = authentication.getName();

        // Empêche auto désactivation
        if (currentUser.equals(matricule) && !enabled) {

            throw new RuntimeException(
                    "Vous ne pouvez pas désactiver votre propre compte"
            );
        }

        return ResponseEntity.ok(
                agentService.changeStatus(
                        matricule,
                        enabled
                )
        );
    }

    /**
     * ================= SUPPRESSION =================
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{matricule}")
    @Operation(
            summary = "Supprimer un agent",
            description = "Permet de supprimer un agent via son matricule"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String matricule,
            Authentication authentication
    ) {

        String currentUser = authentication.getName();

        // Empêche auto suppression
        if (currentUser.equals(matricule)) {

            throw new RuntimeException(
                    "Vous ne pouvez pas supprimer votre propre compte"
            );
        }

        agentService.delete(matricule);

        return ResponseEntity.noContent().build();
    }
}
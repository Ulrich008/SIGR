package com.example.SIGR.controller;

import com.example.SIGR.dto.request.AgentRequest;
import com.example.SIGR.dto.response.AgentResponse;
import com.example.SIGR.services.AgentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
        description = """
                API de gestion des agents.

                Profils supportés :
                - ADMINISTRATEUR_GENERAL
                - CMMR
                - CORRESPONDANT_RISQUE
                - RESPONSABLE_MITIGATION
                - LECTEUR
                """
)
public class AgentController {

    private final AgentService agentService;

    public AgentController(
            AgentService agentService
    ) {
        this.agentService = agentService;
    }

    /**
     * ================= CREATION =================
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Créer un agent",
            description = """
                    Crée un nouvel agent.

                    Le matricule est généré automatiquement.
                    Le profil permet d'attribuer les permissions métier.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Informations du nouvel agent",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AgentRequest.class
                            ),
                            examples = @ExampleObject(
                                    name = "Exemple création agent",
                                    value = """
                                            {
                                              "password": "password123",
                                              "npi": "1234567890",
                                              "nom": "ASSOGBA",
                                              "prenoms": "Ulrich",
                                              "sexe": "MASCULIN",
                                              "role": "AGENT",
                                              "codeProfil": "RESP_MITIGATION",
                                              "dateNaissance": "1998-05-10",
                                              "datePriseService": "2024-01-15",
                                              "codeUnite": "DGB"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Agent créé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflit de données"
            )
    })
    public ResponseEntity<AgentResponse> create(
            @Valid
            @RequestBody
            AgentRequest request
    ) {

        AgentResponse response =
                agentService.create(request);

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
            description = """
                    Retourne les informations
                    de l'utilisateur connecté.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profil récupéré avec succès"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Utilisateur non authentifié"
            )
    })
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
            description = """
                    Retourne la liste complète des agents.
                    Accessible uniquement aux administrateurs
                    et managers.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des agents récupérée"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            )
    })
    public ResponseEntity<List<AgentResponse>> getAll() {

        return ResponseEntity.ok(
                agentService.getAll()
        );
    }

    /**
     * ================= GET BY MATRICULE =================
     */
    @PreAuthorize("""
            hasAnyAuthority('ADMIN', 'MANAGER')
            or #matricule == authentication.name
            """)
    @GetMapping("/{matricule}")
    @Operation(
            summary = "Rechercher un agent par matricule",
            description = """
                    Retourne les informations d'un agent
                    à partir de son matricule métier.

                    Un utilisateur simple peut uniquement
                    consulter son propre profil.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agent trouvé"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès interdit"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agent introuvable"
            )
    })
    public ResponseEntity<AgentResponse> getByMatricule(

            @Parameter(
                    description = "Matricule métier de l'agent",
                    example = "AGT-001"
            )
            @PathVariable String matricule
    ) {

        return ResponseEntity.ok(
                agentService.getByMatricule(matricule)
        );
    }

    /**
     * ================= UPDATE =================
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{matricule}")
    @Operation(
            summary = "Modifier un agent",
            description = """
                    Met à jour les informations d'un agent.

                    Champs NON modifiables :
                    - matricule
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Nouvelles informations de l'agent",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AgentRequest.class
                            ),
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
                                              "codeProfil": "CMMR",
                                              "dateNaissance": "1998-05-10",
                                              "datePriseService": "2024-01-15",
                                              "codeUnite": "DGB"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agent modifié avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agent introuvable"
            )
    })
    public ResponseEntity<AgentResponse> update(

            @Parameter(
                    description = "Matricule métier de l'agent",
                    example = "AGT-001"
            )
            @PathVariable String matricule,

            @Valid
            @RequestBody
            AgentRequest request,

            Authentication authentication
    ) {

        String currentUser =
                authentication.getName();

        // Empêche un admin de modifier son propre rôle
        if (currentUser.equals(matricule)
                && request.getRole() != null) {

            throw new RuntimeException(
                    "Vous ne pouvez pas modifier votre propre rôle"
            );
        }

        return ResponseEntity.ok(
                agentService.update(
                        matricule,
                        request
                )
        );
    }

    /**
     * ================= CHANGE STATUS =================
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{matricule}/status")
    @Operation(
            summary = "Activer ou désactiver un agent",
            description = """
                    Active ou désactive le compte d'un agent.

                    Paramètre attendu :
                    - enabled=true
                    - enabled=false
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Statut modifié avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agent introuvable"
            )
    })
    public ResponseEntity<AgentResponse> changeStatus(

            @Parameter(
                    description = "Matricule métier de l'agent",
                    example = "AGT-001"
            )
            @PathVariable String matricule,

            @Parameter(
                    description = "Statut du compte",
                    example = "true"
            )
            @RequestParam Boolean enabled,

            Authentication authentication
    ) {

        String currentUser =
                authentication.getName();

        // Empêche auto désactivation
        if (currentUser.equals(matricule)
                && !enabled) {

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
     * ================= DELETE =================
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{matricule}")
    @Operation(
            summary = "Supprimer un agent",
            description = """
                    Supprime un agent
                    via son matricule métier.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Agent supprimé avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agent introuvable"
            )
    })
    public ResponseEntity<Void> delete(

            @Parameter(
                    description = "Matricule métier de l'agent",
                    example = "AGT-001"
            )
            @PathVariable String matricule,

            Authentication authentication
    ) {

        String currentUser =
                authentication.getName();

        // Empêche auto suppression
        if (currentUser.equals(matricule)) {

            throw new RuntimeException(
                    "Vous ne pouvez pas supprimer votre propre compte"
            );
        }

        agentService.delete(matricule);

        return ResponseEntity
                .noContent()
                .build();
    }
}
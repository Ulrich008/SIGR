package com.example.SIGR.controller;

import com.example.SIGR.dto.request.AgentRequest;
import com.example.SIGR.dto.request.ChangerMotDePasseRequest;
import com.example.SIGR.dto.request.ModifierEmailRequest;
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

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
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
                agentService.getMe(
                        authentication.getName()
                )
        );
    }

    /**
     * ================= CHANGER MON MOT DE PASSE =================
     */
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me/password")
    @Operation(
            summary = "Changer mon mot de passe",
            description = "Self-service : nécessite l'ancien mot de passe pour appliquer le nouveau."
    )
    public ResponseEntity<Void> changerMonMotDePasse(
            @Valid @RequestBody ChangerMotDePasseRequest request,
            Authentication authentication
    ) {

        agentService.changerMotDePasse(
                authentication.getName(),
                request.getAncienMotDePasse(),
                request.getNouveauMotDePasse()
        );

        return ResponseEntity.noContent().build();
    }

    /**
     * ================= MODIFIER MON EMAIL =================
     */
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me/email")
    @Operation(
            summary = "Modifier mon email",
            description = "Self-service, depuis la page \"Mon profil\"."
    )
    public ResponseEntity<AgentResponse> modifierMonEmail(
            @Valid @RequestBody ModifierEmailRequest request,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                agentService.modifierMonEmail(
                        authentication.getName(),
                        request.getEmail()
                )
        );
    }

    /**
     * ================= LISTE =================
     * ADMIN/SUPER_ADMIN : tous les agents de leur périmètre (ministère,
     * filtré par Hibernate). AGENT : uniquement les agents de sa propre
     * unité administrative (voir AgentServiceImpl.getAll) — nécessaire
     * pour les formulaires qui doivent proposer une liste d'agents
     * (ex: responsable d'une action) sans exposer tout le ministère.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'AGENT')")
    @GetMapping
    @Operation(
            summary = "Lister tous les agents",
            description = """
                    Retourne la liste des agents visibles pour l'utilisateur connecté :
                    tout le ministère pour un ADMIN/SUPER_ADMIN, uniquement sa propre
                    unité administrative pour un AGENT.
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
     * ================= EXPORT PDF =================
     *
     * ADMIN :
     * - Génère le PDF des agents de son propre ministère
     *   (codeMinistere est ignoré, forcé côté service)
     *
     * SUPER_ADMIN :
     * - Génère le PDF des agents du ministère de son choix
     *   (codeMinistere obligatoire)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/export/pdf")
    @Operation(
            summary = "Générer le PDF de la liste des agents d'un ministère",
            description = """
                    Un ADMIN reçoit toujours la liste de son propre ministère.
                    Un SUPER_ADMIN doit préciser le code du ministère souhaité.
                    """
    )
    public ResponseEntity<byte[]> exportAgentsPdf(
            @Parameter(description = "Code du ministère (obligatoire pour un SUPER_ADMIN)")
            @RequestParam(required = false) String codeMinistere
    ) {

        byte[] pdf = agentService.generateAgentsPdf(codeMinistere);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("agents.pdf")
                                .build()
                                .toString()
                )
                .body(pdf);
    }

    /**
     * ================= IMPORT EXCEL =================
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Importer des agents depuis un fichier Excel",
            description = """
                    Chaque ligne est traitée indépendamment : une ligne en
                    erreur n'empêche pas l'import des autres. Le fichier doit
                    respecter le modèle téléchargeable via GET /import/modele.
                    """
    )
    public ResponseEntity<com.example.SIGR.dto.response.ImportResultResponse> importAgents(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file
    ) {
        return ResponseEntity.ok(agentService.importFromExcel(file));
    }

    /**
     * ================= MODÈLE D'IMPORT =================
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @GetMapping("/import/modele")
    @Operation(
            summary = "Télécharger le modèle Excel pour l'import des agents"
    )
    public ResponseEntity<byte[]> telechargerModeleImport() {
        byte[] modele = agentService.generateImportTemplate();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .header(
                        org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("modele_import_agents.xlsx")
                                .build()
                                .toString()
                )
                .body(modele);
    }

    /**
     * ================= GET BY MATRICULE =================
     */
    @PreAuthorize("""
            hasAuthority('ADMIN')
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
                                              "role": "AGENT",
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
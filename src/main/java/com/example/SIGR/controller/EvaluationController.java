package com.example.SIGR.controller;

import com.example.SIGR.dto.request.EvaluationRequest;
import com.example.SIGR.dto.response.EvaluationResponse;
import com.example.SIGR.services.EvaluationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@Tag(
        name = "Evaluation",
        description = "API de gestion des évaluations des risques"
)
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(
            EvaluationService evaluationService
    ) {
        this.evaluationService = evaluationService;
    }

    /**
     * ================= CREATION =================
     *
     * ADMIN :
     * - Création des évaluations
     *
     * MANAGER :
     * - Création des évaluations
     *
     * AGENT :
     * - Peut effectuer des évaluations
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'AGENT')")
    @PostMapping
    @Operation(
            summary = "Créer une évaluation",
            description = """
                    Permet de créer une nouvelle évaluation.
                    
                    Champ généré automatiquement :
                    - code
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Evaluation créée avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            )
    })
    public ResponseEntity<EvaluationResponse> create(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Données de l'évaluation à créer",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création évaluation",
                                    value = """
                                            {
                                              "impact": 4,
                                              "probabilite": 3,
                                              "dateEvaluation": "2026-05-16",
                                              "bonnesPratiques": "Contrôle interne renforcé",
                                              "niveauControle": 2,
                                              "codeRisque": "RIS-001",
                                              "matriculeAgent": "AGT-001"
                                            }
                                            """
                            )
                    )
            )
            EvaluationRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        evaluationService.create(request)
                );
    }

    /**
     * ================= LISTE =================
     *
     * ADMIN :
     * - Consultation complète
     *
     * MANAGER :
     * - Consultation complète
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping
    @Operation(
            summary = "Lister toutes les évaluations",
            description = "Retourne la liste complète des évaluations"
    )
    public ResponseEntity<List<EvaluationResponse>> getAll() {

        return ResponseEntity.ok(
                evaluationService.getAll()
        );
    }

    /**
     * ================= RECHERCHE PAR CODE =================
     *
     * ADMIN :
     * - Consultation complète
     *
     * MANAGER :
     * - Consultation complète
     *
     * AGENT :
     * - Peut consulter ses propres évaluations
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'AGENT')")
    @GetMapping("/{code}")
    @Operation(
            summary = "Récupérer une évaluation par code",
            description = "Retourne une évaluation via son code métier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Evaluation trouvée"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Evaluation introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            )
    })
    public ResponseEntity<EvaluationResponse> getByCode(
            @Parameter(
                    description = "Code métier de l'évaluation",
                    example = "EVAL-001"
            )
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                evaluationService.getByCode(code)
        );
    }

    /**
     * ================= MODIFICATION =================
     *
     * ADMIN :
     * - Modification des évaluations
     *
     * MANAGER :
     * - Modification des évaluations
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier une évaluation",
            description = """
                    Permet de modifier une évaluation existante via son code métier.
                    
                    Champ NON modifiable :
                    - code
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Evaluation modifiée avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Evaluation introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            )
    })
    public ResponseEntity<EvaluationResponse> update(
            @Parameter(
                    description = "Code métier de l'évaluation",
                    example = "EVAL-001"
            )
            @PathVariable String code,

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Données de l'évaluation à modifier",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification évaluation",
                                    value = """
                                            {
                                              "impact": 5,
                                              "probabilite": 4,
                                              "dateEvaluation": "2026-06-01",
                                              "bonnesPratiques": "Audit renforcé et contrôle permanent",
                                              "niveauControle": 4,
                                              "codeRisque": "RIS-001",
                                              "matriculeAgent": "AGT-002"
                                            }
                                            """
                            )
                    )
            )
            EvaluationRequest request
    ) {

        return ResponseEntity.ok(
                evaluationService.update(
                        code,
                        request
                )
        );
    }

    /**
     * ================= SUPPRESSION =================
     *
     * ADMIN :
     * - Suppression des évaluations
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer une évaluation",
            description = "Supprime une évaluation via son code métier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Evaluation supprimée avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Evaluation introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(
                    description = "Code métier de l'évaluation",
                    example = "EVAL-001"
            )
            @PathVariable String code
    ) {

        evaluationService.delete(code);

        return ResponseEntity
                .noContent()
                .build();
    }
}
package com.example.SIGR.controller;

import com.example.SIGR.dto.request.IndicateurPerformanceRequest;
import com.example.SIGR.dto.response.IndicateurPerformanceResponse;

import com.example.SIGR.services.IndicateurPerformanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indicateurs-performance")
@Tag(
        name = "Indicateur de Performance",
        description = "API de gestion des KPI basée uniquement sur le code métier"
)
public class IndicateurPerformanceController {

    private final IndicateurPerformanceService service;

    public IndicateurPerformanceController(
              IndicateurPerformanceService service
    ) {
        this.service = service;
    }

    /**
     * ================= CREATE =================
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES', 'RESPONSABLE_ACTION')")
    @PostMapping
    @Operation(
            summary = "Créer un indicateur de performance",
            description = """
                    Crée un nouveau KPI.

                    Le code métier est généré automatiquement.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "KPI créé avec succès"
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
    public ResponseEntity<IndicateurPerformanceResponse> create(

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données du KPI à créer",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création KPI",
                                    value = """
                                            {
                                              "libelle": "Taux de satisfaction client",
                                              "frequence": "MENSUEL",
                                              "valeurCible": 90,
                                              "valeurObtenue": 85,
                                              "seuilAlerte": 75,
                                              "dateMesure": "2026-05-07",
                                              "codeProcessus": "PROC-001"
                                            }
                                            """
                            )
                    )
            )
            IndicateurPerformanceRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        service.create(request)
                );
    }

    /**
     * ================= GET ALL =================
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(
            summary = "Lister tous les indicateurs de performance",
            description = "Retourne la liste complète des KPI."
    )
    public ResponseEntity<List<IndicateurPerformanceResponse>> getAll() {

        return ResponseEntity.ok(
                service.getAll()
        );
    }

    /**
     * ================= GET BY CODE =================
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    @Operation(
            summary = "Récupérer un KPI par code",
            description = "Retourne un KPI à partir de son code métier."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "KPI trouvé"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "KPI introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            )
    })
    public ResponseEntity<IndicateurPerformanceResponse> getByCode(

            @Parameter(
                    description = "Code métier du KPI",
                    example = "KPI-001"
            )
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                service.getByCode(code)
        );
    }

    /**
     * ================= UPDATE =================
     *
     * AUDITEUR :
     * - Peut valider un indicateur (seule exception à son accès
     *   normalement en lecture seule sur ce module)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES', 'RESPONSABLE_ACTION', 'AUDITEUR')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier un indicateur de performance",
            description = """
                    Met à jour un KPI existant.

                    Champs NON modifiables :
                    - code
                    - uniteMesure
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Modification réussie"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "KPI introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            )
    })
    public ResponseEntity<IndicateurPerformanceResponse> update(

            @Parameter(
                    description = "Code métier du KPI",
                    example = "KPI-001"
            )
            @PathVariable String code,

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nouvelles données du KPI",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification KPI",
                                    value = """
                                            {
                                              "libelle": "Taux de satisfaction client mis à jour",
                                              "frequence": "MENSUEL",
                                              "valeurCible": 95,
                                              "valeurObtenue": 88,
                                              "seuilAlerte": 80,
                                              "dateMesure": "2026-05-07",
                                              "codeProcessus": "PROC-001"
                                            }
                                            """
                            )
                    )
            )
            IndicateurPerformanceRequest request
    ) {

        return ResponseEntity.ok(
                service.update(
                        code,
                        request
                )
        );
    }

    /**
     * ================= DELETE =================
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES', 'RESPONSABLE_ACTION')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer un indicateur de performance",
            description = "Supprime un KPI via son code métier."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Suppression réussie"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "KPI introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            )
    })
    public ResponseEntity<Void> delete(

            @Parameter(
                    description = "Code métier du KPI",
                    example = "KPI-001"
            )
            @PathVariable String code
    ) {

        service.delete(code);

        return ResponseEntity
                .noContent()
                .build();
    }
}
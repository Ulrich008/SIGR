package com.example.SIGR.controller;

import com.example.SIGR.dto.request.RisqueResiduelRequest;
import com.example.SIGR.dto.response.RisqueResiduelResponse;
import com.example.SIGR.services.RisqueResiduelService;

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
@RequestMapping("/api/risques-residuels")
@Tag(
        name = "Risque Résiduel",
        description = "API de gestion des risques résiduels basée uniquement sur le code métier"
)
public class RisqueResiduelController {

    private final RisqueResiduelService service;

    public RisqueResiduelController(
            RisqueResiduelService service
    ) {
        this.service = service;
    }

    /**
     * ================= CREATE =================
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping
    @Operation(
            summary = "Créer un risque résiduel",
            description = """
                    Crée un nouveau risque résiduel.

                    Le code métier est généré automatiquement.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Création réussie"
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
    public ResponseEntity<RisqueResiduelResponse> create(

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données du risque résiduel à créer",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Création risque résiduel",
                                    value = """
                                            {
                                              "impactResiduel": 3,
                                              "probabiliteResiduelle": 2,
                                              "codeEvaluation": "EVAL-001",
                                              "codeRisque": "RIS-001"
                                            }
                                            """
                            )
                    )
            )
            RisqueResiduelRequest request
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'AGENT')")
    @GetMapping
    @Operation(
            summary = "Lister tous les risques résiduels",
            description = "Retourne la liste complète des risques résiduels."
    )
    public ResponseEntity<List<RisqueResiduelResponse>> getAll() {

        return ResponseEntity.ok(
                service.getAll()
        );
    }

    /**
     * ================= GET BY CODE =================
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'AGENT')")
    @GetMapping("/{code}")
    @Operation(
            summary = "Rechercher un risque résiduel par code",
            description = "Retourne un risque résiduel à partir de son code métier."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Risque résiduel trouvé"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Risque résiduel introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            )
    })
    public ResponseEntity<RisqueResiduelResponse> getByCode(

            @Parameter(
                    description = "Code métier du risque résiduel",
                    example = "RR-001"
            )
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                service.getByCode(code)
        );
    }

    /**
     * ================= UPDATE =================
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier un risque résiduel",
            description = """
                    Met à jour un risque résiduel existant.

                    Champ NON modifiable :
                    - code
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Modification réussie"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Risque résiduel introuvable"
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
    public ResponseEntity<RisqueResiduelResponse> updateByCode(

            @Parameter(
                    description = "Code métier du risque résiduel",
                    example = "RR-001"
            )
            @PathVariable String code,

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nouvelles données du risque résiduel",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Modification risque résiduel",
                                    value = """
                                            {
                                              "impactResiduel": 4,
                                              "probabiliteResiduelle": 1,
                                              "codeEvaluation": "EVAL-001",
                                              "codeRisque": "RIS-001"
                                            }
                                            """
                            )
                    )
            )
            RisqueResiduelRequest request
    ) {

        return ResponseEntity.ok(
                service.updateByCode(
                        code,
                        request
                )
        );
    }

    /**
     * ================= DELETE =================
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer un risque résiduel",
            description = "Supprime un risque résiduel via son code métier."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Suppression réussie"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Risque résiduel introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            )
    })
    public ResponseEntity<Void> delete(

            @Parameter(
                    description = "Code métier du risque résiduel",
                    example = "RR-001"
            )
            @PathVariable String code
    ) {

        service.deleteByCode(code);

        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * ================= BY EVALUATION =================
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'AGENT')")
    @GetMapping("/evaluation/{codeEvaluation}")
    @Operation(
            summary = "Lister par évaluation",
            description = "Retourne les risques résiduels liés à une évaluation."
    )
    public ResponseEntity<List<RisqueResiduelResponse>> getByEvaluation(

            @Parameter(
                    description = "Code métier de l'évaluation",
                    example = "EVAL-001"
            )
            @PathVariable String codeEvaluation
    ) {

        return ResponseEntity.ok(
                service.getByEvaluation(codeEvaluation)
        );
    }

    /**
     * ================= BY RISQUE =================
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'AGENT')")
    @GetMapping("/risque/{codeRisque}")
    @Operation(
            summary = "Lister par risque",
            description = "Retourne les risques résiduels liés à un risque."
    )
    public ResponseEntity<List<RisqueResiduelResponse>> getByRisque(

            @Parameter(
                    description = "Code métier du risque",
                    example = "RIS-001"
            )
            @PathVariable String codeRisque
    ) {

        return ResponseEntity.ok(
                service.getByRisque(codeRisque)
        );
    }

    /**
     * ================= RISQUES ELEVES =================
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping("/eleves")
    @Operation(
            summary = "Lister les risques élevés",
            description = """
                    Retourne les risques résiduels
                    ayant un score résiduel supérieur à 15.
                    """
    )
    public ResponseEntity<List<RisqueResiduelResponse>> getRisquesEleves() {

        return ResponseEntity.ok(
                service.getRisquesEleves()
        );
    }
}
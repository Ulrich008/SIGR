package com.example.SIGR.controller;

import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.response.RisqueResponse;
import com.example.SIGR.services.RisqueService;

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
@RequestMapping("/api/risques")
@Tag(
        name = "Gestion des risques",
        description = """
                API permettant de gérer les risques :
                - création
                - modification
                - suppression
                - consultation
                """
)
public class RisqueController {

    private final RisqueService risqueService;

    public RisqueController(
            RisqueService risqueService
    ) {
        this.risqueService = risqueService;
    }

    /**
     * ================= CREATION =================
     *
     * ADMIN :
     * - Création des risques
     *
     * MANAGER :
     * - Création des risques
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping
    @Operation(
            summary = "Créer un risque",
            description = """
                    Permet de créer un nouveau risque.

                    Le code risque est généré automatiquement.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Risque créé avec succès"
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
    public ResponseEntity<RisqueResponse> create(

            @Valid

            @RequestBody

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données du risque à créer",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création risque",
                                    value = """
                                            {
                                              "libelle": "Fraude financière",
                                              "categorie": "Financier",
                                              "causeProbable": "Absence de contrôle",
                                              "consequenceProbable": "Perte financière",
                                              "statut": "ACTIF",
                                              "dateIdentification": "2026-05-07",
                                              "codeProcessus": "PROC-001",
                                              "codeCartographie": "CARTO-001",
                                              "typeRisque": "OPERATIONNEL"
                                            }
                                            """
                            )
                    )
            )

            RisqueRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        risqueService.create(request)
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
            summary = "Lister tous les risques",
            description = "Retourne la liste complète des risques"
    )
    public ResponseEntity<List<RisqueResponse>> getAll() {

        return ResponseEntity.ok(
                risqueService.getAll()
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
     * - Consultation autorisée
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'AGENT')")
    @GetMapping("/{code}")
    @Operation(
            summary = "Rechercher un risque par code",
            description = "Retourne les informations d'un risque via son code métier"
    )
    public ResponseEntity<RisqueResponse> getByCode(

            @Parameter(
                    description = "Code métier du risque",
                    example = "RIS-001"
            )

            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                risqueService.getByCode(code)
        );
    }

    /**
     * ================= MODIFICATION =================
     *
     * ADMIN :
     * - Modification des risques
     *
     * MANAGER :
     * - Modification des risques
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier un risque",
            description = """
                    Permet de modifier un risque existant.

                    Champ NON modifiable :
                    - code
                    """
    )
    public ResponseEntity<RisqueResponse> update(

            @Parameter(
                    description = "Code métier du risque",
                    example = "RIS-001"
            )

            @PathVariable String code,

            @Valid @RequestBody RisqueRequest request
    ) {

        return ResponseEntity.ok(
                risqueService.updateByCode(
                        code,
                        request
                )
        );
    }

    /**
     * ================= SUPPRESSION =================
     *
     * ADMIN :
     * - Suppression des risques
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer un risque",
            description = "Permet de supprimer un risque via son code métier"
    )
    public ResponseEntity<Void> delete(

            @Parameter(
                    description = "Code métier du risque",
                    example = "RIS-001"
            )

            @PathVariable String code
    ) {

        risqueService.deleteByCode(code);

        return ResponseEntity
                .noContent()
                .build();
    }
}
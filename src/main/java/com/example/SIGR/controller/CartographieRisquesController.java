package com.example.SIGR.controller;

import com.example.SIGR.dto.request.CartographieRisquesRequest;
import com.example.SIGR.dto.response.CartographieRisquesResponse;
import com.example.SIGR.services.CartographieRisquesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        value = "/api/cartographies",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(
        name = "Cartographie des risques",
        description = "Gestion des cartographies de risques"
)
public class CartographieRisquesController {

    private final CartographieRisquesService service;
    private final CartographieRisquesService cartographieRisquesService;

    public CartographieRisquesController(
            CartographieRisquesService service,
            CartographieRisquesService cartographieRisquesService) {
        this.service = service;
        this.cartographieRisquesService = cartographieRisquesService;
    }

    /**
     * ================= CREATE =================
     *
     * ADMIN :
     * - Création des cartographies
     *
     * RESPONSABLE_RISQUES :
     * - Création des cartographies
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RESPONSABLE_RISQUES')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Créer une cartographie de risques",
            description = "Création automatique du code cartographie",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création cartographie",
                                    value = """
                                            {
                                              "titre": "Cartographie des risques 2026",
                                              "periode": "2026-01-01",
                                              "seuilFaible": 7,
                                              "seuilMoyen": 14,
                                              "seuilEleve": 25,
                                              "statut": "BROUILLON"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cartographie créée avec succès"
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
    public ResponseEntity<CartographieRisquesResponse> create(
            @Valid @RequestBody CartographieRisquesRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    /**
     * ================= GET ALL =================
     *
     * Tous les profils :
     * - Consultation en lecture seule
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(
            summary = "Lister toutes les cartographies",
            description = "Retourne toutes les cartographies (lecture seule pour tous les profils)"
    )
    public ResponseEntity<List<CartographieRisquesResponse>> getAll() {

        return ResponseEntity.ok(
                service.getAll()
        );
    }

    /**
     * ================= GET BY CODE =================
     *
     * Tous les profils :
     * - Consultation en lecture seule
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    @Operation(
            summary = "Obtenir une cartographie",
            description = "Recherche via le code métier (lecture seule pour tous les profils)"
    )
    public ResponseEntity<CartographieRisquesResponse> getByCode(
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                service.getByCode(code)
        );
    }

    /**
     * ================= UPDATE =================
     *
     * ADMIN :
     * - Modification des cartographies
     *
     * RESPONSABLE_RISQUES :
     * - Modification des cartographies
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RESPONSABLE_RISQUES')")
    @PutMapping(
            value = "/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Modifier une cartographie",
            description = "Modification via le code métier"
    )
    public ResponseEntity<CartographieRisquesResponse> update(
            @PathVariable String code,
            @Valid @RequestBody CartographieRisquesRequest request
    ) {

        return ResponseEntity.ok(
                service.update(code, request)
        );
    }

    /**
     * ================= DELETE =================
     *
     * ADMIN :
     * - Suppression des cartographies
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer une cartographie",
            description = "Suppression via le code métier"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {

        service.delete(code);

        return ResponseEntity
                .noContent()
                .build();
    }

    /**
     * ================= EXPORT EXCEL =================
     *
     * Tous les profils :
     * - Consultation en lecture seule
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/export/excel")
    @Operation(
            summary = "Exporter la cartographie en Excel",
            description = "Génère un fichier Excel contenant la cartographie détaillée des risques (lecture seule pour tous les profils)"
    )
    public ResponseEntity<byte[]> exportExcel() {

        byte[] excel = cartographieRisquesService.generateExcel();

        return ResponseEntity.ok()
                .header(
                        "Content-Type",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
                .header(
                        "Content-Disposition",
                        "attachment; filename=cartographie-risques.xlsx"
                )
                .body(excel);
    }

    /**
     * ================= EXPORT EXCEL PAR UNITE =================
     *
     * Tous les profils :
     * - Consultation en lecture seule
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/export/excel/unite/{codeUnite}")
    @Operation(
            summary = "Exporter la cartographie par unité administrative en Excel",
            description = "Génère un fichier Excel contenant la cartographie détaillée des risques pour une unité administrative spécifique (lecture seule pour tous les profils)"
    )
    public ResponseEntity<byte[]> exportExcelByUnite(
            @PathVariable String codeUnite
    ) {

        byte[] excel = cartographieRisquesService.generateExcelByUnite(codeUnite);

        return ResponseEntity.ok()
                .header(
                        "Content-Type",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
                .header(
                        "Content-Disposition",
                        "attachment; filename=cartographie-risques-" + codeUnite + ".xlsx"
                )
                .body(excel);
    }
}
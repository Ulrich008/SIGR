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

    public CartographieRisquesController(
            CartographieRisquesService service
    ) {
        this.service = service;
    }

    /**
     * ================= CREATE =================
     *
     * ADMIN :
     * - Création des cartographies
     *
     * MANAGER :
     * - Création des cartographies
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
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
                                              "seuilFaible": 30,
                                              "seuilMoyen": 60,
                                              "seuilEleve": 90,
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
     * ADMIN :
     * - Consultation complète
     *
     * MANAGER :
     * - Consultation complète
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping
    @Operation(
            summary = "Lister toutes les cartographies",
            description = "Retourne toutes les cartographies"
    )
    public ResponseEntity<List<CartographieRisquesResponse>> getAll() {

        return ResponseEntity.ok(
                service.getAll()
        );
    }

    /**
     * ================= GET BY CODE =================
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
            summary = "Obtenir une cartographie",
            description = "Recherche via le code métier"
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
     * MANAGER :
     * - Modification des cartographies
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
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
}
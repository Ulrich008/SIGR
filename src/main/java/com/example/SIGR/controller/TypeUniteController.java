package com.example.SIGR.controller;

import com.example.SIGR.dto.request.TypeUniteRequest;
import com.example.SIGR.dto.response.TypeUniteResponse;
import com.example.SIGR.services.TypeUniteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/typeunite")
@Tag(
        name = "Type d'unité",
        description = "API de gestion des types d'unités administratives"
)
public class TypeUniteController {

    private final TypeUniteService typeUniteService;

    public TypeUniteController(
            TypeUniteService typeUniteService
    ) {
        this.typeUniteService = typeUniteService;
    }

    /**
     * ================= CRÉATION =================
     *
     * ADMIN :
     * - Peut créer des types d'unités
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Créer un type d'unité",
            description = "Permet de créer un nouveau type d'unité administrative",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création",
                                    value = """
                                            {
                                              "code": "TU-001",
                                              "libelle": "Direction Générale",
                                              "description": "Unité de niveau direction"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<TypeUniteResponse> create(
            @Valid @RequestBody TypeUniteRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        typeUniteService.create(request)
                );
    }

    /**
     * ================= LISTE =================
     *
     * ADMIN :
     * - Accès total
     *
     * MANAGER :
     * - Consultation des types d'unités
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping
    @Operation(
            summary = "Lister tous les types d'unités",
            description = "Retourne la liste complète des types d'unités administratives"
    )
    public ResponseEntity<List<TypeUniteResponse>> getAll() {

        return ResponseEntity.ok(
                typeUniteService.getAll()
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
     * - Consultation simple
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'AGENT')")
    @GetMapping("/{code}")
    @Operation(
            summary = "Récupérer un type d'unité par code",
            description = "Retourne les informations d'un type d'unité à partir de son code"
    )
    public ResponseEntity<TypeUniteResponse> getByCode(
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                typeUniteService.getByCode(code)
        );
    }

    /**
     * ================= MODIFICATION =================
     *
     * ADMIN :
     * - Peut modifier tous les types d'unités
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier un type d'unité",
            description = """
                    Permet de modifier un type d'unité existant.

                    Champ NON modifiable :
                    - code
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification",
                                    value = """
                                            {
                                              "libelle": "Direction Technique",
                                              "description": "Unités techniques spécialisées"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<TypeUniteResponse> update(
            @PathVariable String code,
            @Valid @RequestBody TypeUniteRequest request
    ) {

        return ResponseEntity.ok(
                typeUniteService.update(code, request)
        );
    }

    /**
     * ================= SUPPRESSION =================
     *
     * ADMIN :
     * - Peut supprimer un type d'unité
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer un type d'unité",
            description = "Permet de supprimer un type d'unité via son code"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {

        typeUniteService.delete(code);

        return ResponseEntity
                .noContent()
                .build();
    }
}
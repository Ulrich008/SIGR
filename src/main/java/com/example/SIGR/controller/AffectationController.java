package com.example.SIGR.controller;

import com.example.SIGR.dto.request.AffectationRequest;
import com.example.SIGR.dto.response.AffectationResponse;
import com.example.SIGR.services.AffectationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/affectations")
@Tag(
        name = "Affectation",
        description = "API de gestion des affectations des agents"
)
public class AffectationController {

    private final AffectationService affectationService;

    public AffectationController(
            AffectationService affectationService
    ) {
        this.affectationService = affectationService;
    }

    /**
     * ================= CRÉATION =================
     *
     * ADMIN :
     * - Peut créer des affectations
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Créer une affectation",
            description = "Permet d'affecter un agent à une unité administrative",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création affectation",
                                    value = """
                                            {
                                              "code": "AFF-001",
                                              "matriculeAgent": "AGT001",
                                              "codeUnite": "DGB",
                                              "poste": "Chef Service Budget",
                                              "dateAffectation": "2025-01-10",
                                              "dateFinAffectation": "2026-01-10"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<AffectationResponse> create(
            @Valid @RequestBody AffectationRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        affectationService.create(request)
                );
    }

    /**
     * ================= LISTE =================
     *
     * ADMIN :
     * - Accès total
     *
     * MANAGER :
     * - Consultation des affectations
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping
    @Operation(
            summary = "Lister toutes les affectations",
            description = "Retourne la liste complète des affectations"
    )
    public ResponseEntity<List<AffectationResponse>> getAll() {

        return ResponseEntity.ok(
                affectationService.getAll()
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
     * - Peut consulter uniquement sa propre affectation
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'AGENT')")
    @GetMapping("/{code}")
    @Operation(
            summary = "Rechercher une affectation par code",
            description = "Retourne les informations d'une affectation à partir de son code métier"
    )
    public ResponseEntity<AffectationResponse> getByCode(
            @PathVariable String code,
            Authentication authentication
    ) {

        AffectationResponse response =
                affectationService.getByCode(code);

        String currentUser = authentication.getName();

        boolean isAdminOrManager =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(auth ->
                                auth.getAuthority().equals("ADMIN")
                                        || auth.getAuthority().equals("MANAGER")
                        );

        /**
         * ADMIN et MANAGER :
         * accès total
         */
        if (isAdminOrManager) {
            return ResponseEntity.ok(response);
        }

        /**
         * AGENT :
         * uniquement sa propre affectation
         */
        if (!response.getMatriculeAgent().equals(currentUser)) {

            throw new RuntimeException(
                    "Accès refusé : vous ne pouvez consulter que votre affectation"
            );
        }

        return ResponseEntity.ok(response);
    }

    /**
     * ================= MODIFICATION =================
     *
     * ADMIN :
     * - Peut modifier uniquement :
     *   - poste
     *   - dateAffectation
     *   - dateFinAffectation
     *
     * Les champs suivants NE sont PAS modifiables :
     * - code affectation
     * - matriculeAgent
     * - codeUnite
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier une affectation",
            description = """
                    Permet de modifier une affectation existante.

                    Champs NON modifiables :
                    - code
                    - matriculeAgent
                    - codeUnite
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification affectation",
                                    value = """
                                            {
                                              "poste": "Directeur du Budget",
                                              "dateAffectation": "2025-01-10",
                                              "dateFinAffectation": "2027-01-10"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<AffectationResponse> update(
            @PathVariable String code,
            @Valid @RequestBody AffectationRequest request
    ) {

        return ResponseEntity.ok(
                affectationService.update(
                        code,
                        request
                )
        );
    }

    /**
     * ================= SUPPRESSION =================
     *
     * ADMIN :
     * - Peut supprimer une affectation
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer une affectation",
            description = "Permet de supprimer une affectation via son code métier"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {

        affectationService.delete(code);

        return ResponseEntity
                .noContent()
                .build();
    }
}
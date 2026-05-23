package com.example.SIGR.controller;

import com.example.SIGR.dto.request.ProcessusRequest;
import com.example.SIGR.dto.response.ProcessusResponse;
import com.example.SIGR.services.ProcessusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/processus")
@Tag(
        name = "Processus",
        description = "API de gestion des processus"
)
public class ProcessusController {

    private final ProcessusService processusService;

    public ProcessusController(
            ProcessusService processusService
    ) {
        this.processusService = processusService;
    }

    /**
     * ================= CREATE =================
     *
     * ADMIN :
     * - Peut créer des processus
     *
     * MANAGER :
     * - Peut créer des processus
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping
    @Operation(
            summary = "Créer un processus",
            description = "Permet de créer un nouveau processus",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création processus",
                                    value = """
                                            {
                                              "libelle": "Gestion budgétaire",
                                              "finalite": "Assurer le suivi budgétaire",
                                              "typeProcessus": "METIER",
                                              "idUnite": "UNIT-001",
                                              "idProprietaire": "AGENT-001"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<ProcessusResponse> create(
            @Valid @RequestBody ProcessusRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        processusService.create(request)
                );
    }

    /**
     * ================= GET ALL =================
     *
     * ADMIN :
     * - Accès total
     *
     * MANAGER :
     * - Consultation complète
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping
    @Operation(
            summary = "Lister tous les processus",
            description = "Retourne la liste complète des processus"
    )
    public ResponseEntity<List<ProcessusResponse>> getAll() {

        return ResponseEntity.ok(
                processusService.getAll()
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
     * - Peut consulter uniquement les processus
     *   dont il est propriétaire
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'AGENT')")
    @GetMapping("/{code}")
    @Operation(
            summary = "Récupérer un processus par code",
            description = "Retourne les informations d'un processus à partir de son code"
    )
    public ResponseEntity<ProcessusResponse> getByCode(
            @PathVariable String code,
            Authentication authentication
    ) {

        ProcessusResponse response =
                processusService.getByCode(code);

        String currentUser =
                authentication.getName();

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
         * uniquement ses propres processus
         */
        if (!response.getIdProprietaire()
                .equals(currentUser)) {

            throw new RuntimeException(
                    "Accès refusé : vous ne pouvez consulter que vos propres processus"
            );
        }

        return ResponseEntity.ok(response);
    }


    /**
     * ================= UPDATE =================
     *
     * ADMIN :
     * - Peut modifier tous les processus
     *
     * MANAGER :
     * - Peut modifier les processus
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier un processus",
            description = "Permet de modifier un processus existant",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification processus",
                                    value = """
                                            {
                                              "libelle": "Gestion financière",
                                              "finalite": "Mise à jour finalité",
                                              "typeProcessus": "SUPPORT",
                                              "idUnite": "UNIT-001",
                                              "idProprietaire": "AGENT-001"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<ProcessusResponse> update(
            @PathVariable String code,
            @Valid @RequestBody ProcessusRequest request
    ) {

        return ResponseEntity.ok(
                processusService.updateByCode(
                        code,
                        request
                )
        );
    }

    /**
     * ================= DELETE =================
     *
     * ADMIN :
     * - Peut supprimer un processus
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer un processus",
            description = "Permet de supprimer un processus via son code"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {

        processusService.deleteByCode(code);

        return ResponseEntity
                .noContent()
                .build();
    }
}
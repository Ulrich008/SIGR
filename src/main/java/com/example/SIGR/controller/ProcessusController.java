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
     * RESPONSABLE_RISQUES :
     * - Peut créer des processus (Formalisation des risques)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES')")
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
     * Tous les profils :
     * - Consultation en lecture seule (Formalisation des risques)
     */
    @PreAuthorize("isAuthenticated()")
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
     * Tous les profils :
     * - Consultation en lecture seule (Formalisation des risques)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    @Operation(
            summary = "Récupérer un processus par code",
            description = "Retourne les informations d'un processus à partir de son code"
    )
    public ResponseEntity<ProcessusResponse> getByCode(
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                processusService.getByCode(code)
        );
    }


    /**
     * ================= UPDATE =================
     *
     * RESPONSABLE_RISQUES :
     * - Peut modifier les processus (Formalisation des risques)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES')")
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
     * RESPONSABLE_RISQUES :
     * - Peut supprimer un processus (Formalisation des risques)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES')")
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
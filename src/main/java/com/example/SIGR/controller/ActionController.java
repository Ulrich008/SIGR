package com.example.SIGR.controller;

import com.example.SIGR.dto.request.ActionRequest;
import com.example.SIGR.dto.response.ActionResponse;
import com.example.SIGR.services.ActionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/actions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Action", description = "API de gestion des actions (basée sur code métier)")
public class ActionController {

    private final ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    // ================= CREATE =================
    /**
     * ADMIN :
     * - Création des actions
     *
     * RESPONSABLE_RISQUES :
     * - Création des actions (Mitigation)
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RESPONSABLE_RISQUES')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Créer une action",
            description = "Création d'une action avec génération automatique du code métier",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création action",
                                    value = """
                                    {
                                      "libelle": "Mettre en place un contrôle interne",
                                      "dateDebut": "2026-05-07",
                                      "dateFin": "2026-06-15",
                                      "statut": "EN_COURS",
                                      "codePlan": "PLAN-001",
                                      "matriculeResponsable": "AGT-001"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<ActionResponse> create(
            @Valid @RequestBody ActionRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actionService.create(request));
    }

    // ================= GET ALL =================
    /**
     * Tous les profils :
     * - Consultation en lecture seule (Mitigation)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(
            summary = "Lister toutes les actions",
            description = "Retourne la liste complète des actions (lecture seule pour tous les profils)"
    )
    public ResponseEntity<List<ActionResponse>> getAll() {
        return ResponseEntity.ok(actionService.getAll());
    }

    // ================= GET BY CODE =================
    /**
     * Tous les profils :
     * - Consultation en lecture seule (Mitigation)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    @Operation(
            summary = "Récupérer une action par code",
            description = "Retourne une action à partir de son code métier (lecture seule pour tous les profils)"
    )
    public ResponseEntity<ActionResponse> getByCode(
            @PathVariable String code
    ) {
        return ResponseEntity.ok(actionService.getByCode(code));
    }

    // ================= UPDATE =================
    /**
     * ADMIN :
     * - Modification des actions
     *
     * RESPONSABLE_RISQUES :
     * - Modification des actions (Mitigation)
     *
     * RESPONSABLE_ACTION :
     * - Modification uniquement des actions qui lui sont affectées (Mitigation)
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RESPONSABLE_RISQUES', 'RESPONSABLE_ACTION')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier une action",
            description = "Met à jour une action via son code métier",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification action",
                                    value = """
                                    {
                                      "libelle": "Renforcer le contrôle budgétaire",
                                      "dateDebut": "2026-05-10",
                                      "dateFin": "2026-07-01",
                                      "statut": "TERMINE",
                                      "codePlan": "PLAN-001",
                                      "matriculeResponsable": "AGT-002"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<ActionResponse> update(
            @PathVariable String code,
            @Valid @RequestBody ActionRequest request
    ) {
        return ResponseEntity.ok(actionService.update(code, request));
    }

    // ================= DELETE =================
    /**
     * ADMIN :
     * - Suppression des actions
     *
     * RESPONSABLE_RISQUES :
     * - Suppression des actions (Mitigation)
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RESPONSABLE_RISQUES')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer une action",
            description = "Suppression d'une action via son code métier"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {
        actionService.delete(code);
        return ResponseEntity.noContent().build();
    }
}
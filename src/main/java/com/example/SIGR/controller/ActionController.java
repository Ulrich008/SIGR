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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
@Tag(name = "Action", description = "API de gestion des actions")
public class ActionController {

    private final ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    // ================= CREATE =================
    @PostMapping
    @Operation(
            summary = "Créer une action",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création action",
                                    value = """
                                    {
                                      "id": "ACT-001",
                                      "libelle": "Mettre en place un contrôle interne",
                                      "dateDebut": "2026-05-07",
                                      "dateFin": "2026-06-15",
                                      "statut": "EN_COURS",
                                      "idPlan": "PLAN-001",
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
    @GetMapping
    @Operation(summary = "Lister toutes les actions")
    public ResponseEntity<List<ActionResponse>> getAll() {

        return ResponseEntity.ok(actionService.getAll());
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une action par identifiant")
    public ResponseEntity<ActionResponse> getById(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(actionService.getById(id));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier une action",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification action",
                                    value = """
                                    {
                                      "id": "ACT-001",
                                      "libelle": "Renforcer le contrôle budgétaire",
                                      "dateDebut": "2026-05-10",
                                      "dateFin": "2026-07-01",
                                      "statut": "TERMINE",
                                      "idPlan": "PLAN-001",
                                      "matriculeResponsable": "AGT-002"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<ActionResponse> update(
            @PathVariable String id,
            @Valid @RequestBody ActionRequest request
    ) {

        return ResponseEntity.ok(actionService.update(id, request));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une action")
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ) {

        actionService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
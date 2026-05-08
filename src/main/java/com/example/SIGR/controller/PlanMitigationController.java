package com.example.SIGR.controller;

import com.example.SIGR.dto.request.PlanMitigationRequest;
import com.example.SIGR.dto.response.PlanMitigationResponse;
import com.example.SIGR.services.PlanMitigationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans-mitigation")
@Tag(name = "Plan de Mitigation", description = "API de gestion des plans de mitigation")
public class PlanMitigationController {

    private final PlanMitigationService service;

    public PlanMitigationController(PlanMitigationService service) {
        this.service = service;
    }

    // ================= CREATE =================
    @PostMapping
    @Operation(
            summary = "Créer un plan de mitigation",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création plan mitigation",
                                    value = """
                                    {
                                      "id": "PLAN-001",
                                      "description": "Réduction du risque de fraude",
                                      "dateCreation": "2026-05-07",
                                      "statut": "PLANIFIE",
                                      "idRisque": "RISK-001"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<PlanMitigationResponse> create(
            @Valid @RequestBody PlanMitigationRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    // ================= GET ALL =================
    @GetMapping
    @Operation(summary = "Lister tous les plans de mitigation")
    public ResponseEntity<List<PlanMitigationResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un plan de mitigation par ID")
    public ResponseEntity<PlanMitigationResponse> getById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier un plan de mitigation",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification plan mitigation",
                                    value = """
                                    {
                                      "description": "Mise à jour du plan de réduction de fraude",
                                      "dateCreation": "2026-05-07",
                                      "statut": "EN_COURS",
                                      "idRisque": "RISK-001"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<PlanMitigationResponse> update(
            @PathVariable String id,
            @Valid @RequestBody PlanMitigationRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un plan de mitigation")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
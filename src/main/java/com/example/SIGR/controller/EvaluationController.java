package com.example.SIGR.controller;

import com.example.SIGR.dto.request.EvaluationRequest;
import com.example.SIGR.dto.response.EvaluationResponse;
import com.example.SIGR.services.EvaluationService;

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
@RequestMapping("/api/evaluations")
@Tag(name = "Evaluation", description = "API de gestion des évaluations de risques")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    // ================= CREATE =================
    @PostMapping
    @Operation(
            summary = "Créer une évaluation",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création évaluation",
                                    value = """
                                    {
                                      "id": "EVAL-001",
                                      "impact": 4,
                                      "probabilite": 3,
                                      "dateEvaluation": "2026-05-07",
                                      "bonnesPratiques": "Renforcer les contrôles internes",
                                      "niveauControle": 2,
                                      "idRisque": "RISK-001",
                                      "idAgent": "AG-001"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<EvaluationResponse> create(
            @Valid @RequestBody EvaluationRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(evaluationService.create(request));
    }

    // ================= GET ALL =================
    @GetMapping
    @Operation(summary = "Lister toutes les évaluations")
    public ResponseEntity<List<EvaluationResponse>> getAll() {
        return ResponseEntity.ok(evaluationService.getAll());
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une évaluation par ID")
    public ResponseEntity<EvaluationResponse> getById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(evaluationService.getById(id));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier une évaluation",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification évaluation",
                                    value = """
                                    {
                                      "impact": 5,
                                      "probabilite": 4,
                                      "dateEvaluation": "2026-05-07",
                                      "bonnesPratiques": "Audit renforcé + contrôle trimestriel",
                                      "niveauControle": 3,
                                      "idRisque": "RISK-001",
                                      "idAgent": "AG-002"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<EvaluationResponse> update(
            @PathVariable String id,
            @Valid @RequestBody EvaluationRequest request
    ) {
        return ResponseEntity.ok(evaluationService.update(id, request));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une évaluation")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        evaluationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
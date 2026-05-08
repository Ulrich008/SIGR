package com.example.SIGR.controller;

import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.response.RisqueResponse;
import com.example.SIGR.services.RisqueService;

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
@RequestMapping("/api/risques")
@Tag(name = "Risque", description = "API de gestion des risques")
public class RisqueController {

    private final RisqueService risqueService;

    public RisqueController(RisqueService risqueService) {
        this.risqueService = risqueService;
    }

    // ================= CREATE =================
    @PostMapping
    @Operation(
            summary = "Créer un risque",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création risque",
                                    value = """
                                    {
                                      "id": "RISK-001",
                                      "libelle": "Fraude financière",
                                      "categorie": "Financier",
                                      "causeProbable": "Absence de contrôle",
                                      "consequenceProbable": "Perte financière",
                                      "statut": "ACTIF",
                                      "dateIdentification": "2026-05-07",
                                      "codeProcessus": "PROC-001",
                                      "idCartographie": "CARTO-001",
                                      "typeRisque": "OPERATIONNEL"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<RisqueResponse> create(
            @Valid @RequestBody RisqueRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(risqueService.create(request));
    }

    // ================= GET ALL =================
    @GetMapping
    @Operation(summary = "Lister tous les risques")
    public ResponseEntity<List<RisqueResponse>> getAll() {

        return ResponseEntity.ok(risqueService.getAll());
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un risque par identifiant")
    public ResponseEntity<RisqueResponse> getById(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(risqueService.getById(id));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier un risque",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification risque",
                                    value = """
                                    {
                                      "libelle": "Fraude budgétaire",
                                      "categorie": "Financier",
                                      "causeProbable": "Mauvais contrôle interne",
                                      "consequenceProbable": "Pertes budgétaires",
                                      "statut": "EN_COURS",
                                      "dateIdentification": "2026-05-07",
                                      "codeProcessus": "PROC-001",
                                      "idCartographie": "CARTO-001",
                                      "typeRisque": "OPERATIONNEL"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<RisqueResponse> update(
            @PathVariable String id,
            @Valid @RequestBody RisqueRequest request
    ) {

        return ResponseEntity.ok(risqueService.update(id, request));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un risque")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        risqueService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
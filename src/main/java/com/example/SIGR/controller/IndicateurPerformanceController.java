package com.example.SIGR.controller;

import com.example.SIGR.dto.request.IndicateurPerformanceRequest;
import com.example.SIGR.dto.response.IndicateurPerformanceResponse;
import com.example.SIGR.services.IndicateurPerformanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indicateurs-performance")
@Tag(
        name = "Indicateur de Performance",
        description = "API de gestion des indicateurs de performance (KPI)"
)
public class IndicateurPerformanceController {

    private final IndicateurPerformanceService service;

    public IndicateurPerformanceController(
            IndicateurPerformanceService service
    ) {
        this.service = service;
    }

    // ================= CREATE =================

    @PostMapping
    @Operation(
            summary = "Créer un indicateur de performance",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création KPI",
                                    value = """
                                    {
                                      "code": "KPI-001",
                                      "libelle": "Taux de satisfaction client",
                                      "frequence": "MENSUELLE",
                                      "valeurCible": 90,
                                      "valeurObtenue": 85,
                                      "seuilAlerte": 75,
                                      "dateMesure": "2026-05-07",
                                      "codeProcessus": "PROC-001"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<IndicateurPerformanceResponse> create(
            @Valid
            @org.springframework.web.bind.annotation.RequestBody
            IndicateurPerformanceRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    // ================= GET ALL =================

    @GetMapping
    @Operation(summary = "Lister tous les indicateurs de performance")
    public ResponseEntity<List<IndicateurPerformanceResponse>> getAll() {

        return ResponseEntity.ok(service.getAll());
    }

    // ================= GET BY ID =================

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un indicateur de performance par ID")
    public ResponseEntity<IndicateurPerformanceResponse> getById(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(service.getById(id));
    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier un indicateur de performance",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification KPI",
                                    value = """
                                    {
                                      "code": "KPI-001",
                                      "libelle": "Taux de satisfaction client mis à jour",
                                      "frequence": "MENSUELLE",
                                      "valeurCible": 95,
                                      "valeurObtenue": 88,
                                      "seuilAlerte": 80,
                                      "dateMesure": "2026-05-07",
                                      "codeProcessus": "PROC-001"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<IndicateurPerformanceResponse> update(
            @PathVariable String id,

            @Valid
            @org.springframework.web.bind.annotation.RequestBody
            IndicateurPerformanceRequest request
    ) {

        return ResponseEntity.ok(
                service.update(id, request)
        );
    }

    // ================= DELETE =================

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un indicateur de performance")
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
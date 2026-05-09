package com.example.SIGR.controller;

import com.example.SIGR.dto.request.RisqueResiduelRequest;
import com.example.SIGR.dto.response.RisqueResiduelResponse;
import com.example.SIGR.services.RisqueResiduelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risques-residuels")
@Tag(
        name = "Risque Résiduel",
        description = "API de gestion des risques résiduels basée uniquement sur le code métier"
)
public class RisqueResiduelController {

    private final RisqueResiduelService service;

    public RisqueResiduelController(RisqueResiduelService service) {
        this.service = service;
    }

    // ================= CREATE =================
    @PostMapping
    @Operation(
            summary = "Créer un risque résiduel",
            description = "Création d’un risque résiduel identifié par un code métier unique.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Création risque résiduel",
                                    value = """
                                    {
                                      "code": "RR-001",
                                      "impactResiduel": 3,
                                      "probabiliteResiduelle": 2,
                                      "idEvaluation": "EVAL-001",
                                      "idRisque": "RISK-001"
                                    }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Création réussie"),
                    @ApiResponse(responseCode = "400", description = "Données invalides")
            }
    )
    public ResponseEntity<RisqueResiduelResponse> create(
            @Valid @RequestBody RisqueResiduelRequest request
    ) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    // ================= GET ALL =================
    @GetMapping
    @Operation(
            summary = "Lister tous les risques résiduels",
            description = "Retourne la liste complète des risques résiduels."
    )
    public ResponseEntity<List<RisqueResiduelResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ================= UPDATE =================
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier un risque résiduel",
            description = "Met à jour un risque résiduel existant à partir de son code métier.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Modification risque résiduel",
                                    value = """
                                    {
                                      "code": "RR-001",
                                      "impactResiduel": 4,
                                      "probabiliteResiduelle": 1,
                                      "idEvaluation": "EVAL-001",
                                      "idRisque": "RISK-001"
                                    }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Modification réussie"),
                    @ApiResponse(responseCode = "404", description = "Risque résiduel introuvable")
            }
    )
    public ResponseEntity<RisqueResiduelResponse> updateByCode(
            @PathVariable String code,
            @Valid @RequestBody RisqueResiduelRequest request
    ) {
        return ResponseEntity.ok(service.updateBycode(code, request));
    }

    // ================= DELETE =================
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer un risque résiduel",
            description = "Suppression d’un risque résiduel via son code métier."
    )
    public ResponseEntity<Void> delete(@PathVariable String code) {
        service.deleteBycode(code);
        return ResponseEntity.noContent().build();
    }

    // ================= GET BY CODE =================
    @GetMapping("/code/{code}")
    @Operation(
            summary = "Rechercher par code",
            description = "Retourne un risque résiduel à partir de son code métier."
    )
    public ResponseEntity<RisqueResiduelResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    // ================= BY ÉVALUATION =================
    @GetMapping("/evaluation/{idEvaluation}")
    @Operation(
            summary = "Lister par évaluation",
            description = "Retourne les risques résiduels liés à une évaluation."
    )
    public ResponseEntity<List<RisqueResiduelResponse>> getByEvaluation(
            @PathVariable String idEvaluation
    ) {
        return ResponseEntity.ok(service.getByEvaluation(idEvaluation));
    }

    // ================= BY RISQUE =================
    @GetMapping("/risque/{idRisque}")
    @Operation(
            summary = "Lister par risque",
            description = "Retourne les risques résiduels liés à un risque."
    )
    public ResponseEntity<List<RisqueResiduelResponse>> getByRisque(
            @PathVariable String idRisque
    ) {
        return ResponseEntity.ok(service.getByRisque(idRisque));
    }

    // ================= RISQUES ÉLEVÉS =================
    @GetMapping("/eleves")
    @Operation(
            summary = "Lister les risques élevés",
            description = "Retourne les risques résiduels ayant un niveau de criticité élevé."
    )
    public ResponseEntity<List<RisqueResiduelResponse>> getRisquesElevés() {
        return ResponseEntity.ok(service.getRisquesElevés());
    }
}
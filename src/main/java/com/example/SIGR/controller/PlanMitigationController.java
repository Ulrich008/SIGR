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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans-mitigation")
@Tag(
        name = "Plan de Mitigation",
        description = "API de gestion des plans de mitigation (basée sur code métier)"
)
public class PlanMitigationController {

    private final PlanMitigationService service;

    public PlanMitigationController(PlanMitigationService service) {
        this.service = service;
    }

    // ================= CREATE =================
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES', 'RESPONSABLE_ACTION')")
    @PostMapping
    @Operation(
            summary = "Créer un plan de mitigation",
            description = "Le code est généré automatiquement",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Création plan mitigation",
                                    value = """
                                    {
                                      "description": "Réduction du risque de fraude",
                                      "dateCreation": "2026-05-07",
                                      "codeRisque": "RIS-001"
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(
            summary = "Lister tous les plans de mitigation",
            description = "Retourne tous les plans de mitigation"
    )
    public ResponseEntity<List<PlanMitigationResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ================= GET BY CODE =================
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    @Operation(
            summary = "Récupérer un plan de mitigation par code métier"
    )
    public ResponseEntity<PlanMitigationResponse> getByCode(
            @PathVariable String code
    ) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    // ================= UPDATE =================
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES', 'RESPONSABLE_ACTION')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier un plan de mitigation par code métier",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Modification plan mitigation",
                                    value = """
                                    {
                                      "description": "Mise à jour du plan",
                                      "dateCreation": "2026-05-07",
                                      "codeRisque": "RIS-001"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<PlanMitigationResponse> update(
            @PathVariable String code,
            @Valid @RequestBody PlanMitigationRequest request
    ) {
        return ResponseEntity.ok(service.updateByCode(code, request));
    }

    // ================= CLÔTURE =================
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'CCI')")
    @PatchMapping("/{code}/cloturer")
    @Operation(
            summary = "Clôturer un plan de mitigation",
            description = "Réservé au CCI : confirme que le plan est terminé et le clôture définitivement."
    )
    public ResponseEntity<PlanMitigationResponse> cloturer(
            @PathVariable String code
    ) {
        return ResponseEntity.ok(service.cloturer(code));
    }

    // ================= DELETE =================
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES', 'RESPONSABLE_ACTION')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer un plan de mitigation par code métier"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {
        service.deleteByCode(code);
        return ResponseEntity.noContent().build();
    }
}
package com.example.SIGR.controller;

import com.example.SIGR.dto.request.PlanAuditRequest;
import com.example.SIGR.dto.response.PlanAuditResponse;
import com.example.SIGR.services.PlanAuditService;

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
@RequestMapping("/api/plans-audit")
@Tag(
        name = "Plan d'audit",
        description = "API de gestion des plans d'audit"
)
public class PlanAuditController {

    private final PlanAuditService planAuditService;

    public PlanAuditController(
            PlanAuditService planAuditService
    ) {
        this.planAuditService = planAuditService;
    }

    /**
     * ================= GET ENUMS =================
     *
     * Ces endpoints sont accessibles sans restriction de rôle
     * car ils retournent uniquement des données de référence (enums)
     */
    @GetMapping("/enums/audit-propose")
    @Operation(
            summary = "Récupérer les types d'audit proposés",
            description = "Retourne la liste des types d'audit proposés disponibles"
    )
    public ResponseEntity<String[]> getAuditProposeEnums() {

        return ResponseEntity.ok(
                java.util.Arrays.stream(com.example.SIGR.entity.AuditPropose.values())
                        .map(Enum::name)
                        .toArray(String[]::new)
        );
    }

    @GetMapping("/enums/type-revue")
    @Operation(
            summary = "Récupérer les types de revue",
            description = "Retourne la liste des types de revue disponibles"
    )
    public ResponseEntity<String[]> getTypeRevueEnums() {

        return ResponseEntity.ok(
                java.util.Arrays.stream(com.example.SIGR.entity.TypeRevue.values())
                        .map(Enum::name)
                        .toArray(String[]::new)
        );
    }

    /**
     * ================= CREATE =================
     *
     * ADMIN :
     * - Peut créer des plans d'audit
     *
     * AUDITEUR :
     * - Peut créer des plans d'audit (Audit)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'AUDITEUR')")
    @PostMapping
    @Operation(
            summary = "Créer un plan d'audit",
            description = "Permet de créer un nouveau plan d'audit",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création plan d'audit",
                                    value = """
                                            {
                                              "libelle": "Audit financier 2026",
                                              "dateCreation": "2026-01-15",
                                              "codeUniteAdministrative": "UA-001",
                                              "codeProcessus": "PROC-001",
                                              "codeRisque": "R-001",
                                              "auditPropose": "FINANCIER",
                                              "typeRevue": "INTERNE",
                                              "objectifAudit": "Vérifier la conformité financière",
                                              "effetAuditIndicatif": "Amélioration des contrôles internes"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<PlanAuditResponse> create(
            @Valid @RequestBody PlanAuditRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        planAuditService.create(request)
                );
    }

    /**
     * ================= GET ALL =================
     *
     * Tous les profils :
     * - Consultation en lecture seule (Audit)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(
            summary = "Lister tous les plans d'audit",
            description = "Retourne la liste complète des plans d'audit (lecture seule pour tous les profils)"
    )
    public ResponseEntity<List<PlanAuditResponse>> getAll() {

        return ResponseEntity.ok(
                planAuditService.getAll()
        );
    }

    /**
     * ================= GET BY CODE =================
     *
     * Tous les profils :
     * - Consultation en lecture seule (Audit)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    @Operation(
            summary = "Récupérer un plan d'audit par code",
            description = "Retourne les informations d'un plan d'audit à partir de son code (lecture seule pour tous les profils)"
    )
    public ResponseEntity<PlanAuditResponse> getByCode(
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                planAuditService.getByCode(code)
        );
    }

    /**
     * ================= UPDATE =================
     *
     * ADMIN :
     * - Peut modifier tous les plans d'audit
     *
     * AUDITEUR :
     * - Peut modifier les plans d'audit (Audit)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'AUDITEUR')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier un plan d'audit",
            description = "Permet de modifier un plan d'audit existant",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification plan d'audit",
                                    value = """
                                            {
                                              "libelle": "Audit financier 2026 - Révisé",
                                              "dateCreation": "2026-01-15",
                                              "codeUniteAdministrative": "UA-001",
                                              "codeProcessus": "PROC-001",
                                              "codeRisque": "R-001",
                                              "auditPropose": "FINANCIER",
                                              "typeRevue": "INTERNE",
                                              "objectifAudit": "Vérifier la conformité financière - Mise à jour",
                                              "effetAuditIndicatif": "Amélioration des contrôles internes"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<PlanAuditResponse> update(
            @PathVariable String code,
            @Valid @RequestBody PlanAuditRequest request
    ) {

        return ResponseEntity.ok(
                planAuditService.update(
                        code,
                        request
                )
        );
    }

    /**
     * ================= DELETE =================
     *
     * ADMIN :
     * - Peut supprimer un plan d'audit
     *
     * AUDITEUR :
     * - Peut supprimer un plan d'audit (Audit)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'AUDITEUR')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer un plan d'audit",
            description = "Permet de supprimer un plan d'audit via son code"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {

        planAuditService.delete(code);

        return ResponseEntity
                .noContent()
                .build();
    }
}

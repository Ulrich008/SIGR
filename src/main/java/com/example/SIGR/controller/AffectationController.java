package com.example.SIGR.controller;

import com.example.SIGR.dto.request.AffectationRequest;
import com.example.SIGR.dto.response.AffectationResponse;
import com.example.SIGR.services.AffectationService;
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
@RequestMapping("/api/affectations")
@Tag(
        name = "Affectation",
        description = "API de gestion des affectations des agents"
)
public class AffectationController {

    private final AffectationService affectationService;

    public AffectationController(AffectationService affectationService) {
        this.affectationService = affectationService;
    }

    /**
     * ================= CREATION =================
     */
    @PostMapping
    @Operation(
            summary = "Créer une affectation",
            description = "Permet d’affecter un agent à une unité administrative",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création affectation",
                                    value = """
                                    {
                                      "code": "AFF-001",
                                      "matriculeAgent": "AGT001",
                                      "codeUnite": "DGB",
                                      "poste": "Chef Service Budget",
                                      "dateAffectation": "2025-01-10",
                                      "dateFinAffectation": "2026-01-10"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<AffectationResponse> create(
            @Valid @RequestBody AffectationRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(affectationService.create(request));
    }

    /**
     * ================= LISTE =================
     */
    @GetMapping
    @Operation(
            summary = "Lister toutes les affectations",
            description = "Retourne la liste complète des affectations"
    )
    public ResponseEntity<List<AffectationResponse>> getAll() {

        return ResponseEntity.ok(
                affectationService.getAll()
        );
    }

    /**
     * ================= RECHERCHE PAR CODE =================
     */
    @GetMapping("/{code}")
    @Operation(
            summary = "Rechercher une affectation par code",
            description = "Retourne les informations d’une affectation à partir de son code métier"
    )
    public ResponseEntity<AffectationResponse> getByCode(
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                affectationService.getByCode(code)
        );
    }

    /**
     * ================= MODIFICATION =================
     */
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier une affectation",
            description = "Permet de modifier une affectation existante",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification affectation",
                                    value = """
                                    {
                                      "matriculeAgent": "AGT001",
                                      "codeUnite": "DGB",
                                      "poste": "Directeur du Budget",
                                      "dateAffectation": "2025-01-10",
                                      "dateFinAffectation": "2027-01-10"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<AffectationResponse> update(
            @PathVariable String code,
            @Valid @RequestBody AffectationRequest request
    ) {

        return ResponseEntity.ok(
                affectationService.update(code, request)
        );
    }

    /**
     * ================= SUPPRESSION =================
     */
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer une affectation",
            description = "Permet de supprimer une affectation via son code métier"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {

        affectationService.delete(code);

        return ResponseEntity.noContent().build();
    }
}
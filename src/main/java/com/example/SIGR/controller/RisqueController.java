package com.example.SIGR.controller;

import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.response.RisqueResponse;
import com.example.SIGR.services.RisqueService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risques")
@Tag(name = "Gestion des risques", description = "API permettant de gérer les risques (création, modification, suppression, consultation)")
public class RisqueController {

    private final RisqueService risqueService;

    public RisqueController(RisqueService risqueService) {
        this.risqueService = risqueService;
    }

    @PostMapping
    @Operation(
            summary = "Créer un risque",
            description = "Permet de créer un nouveau risque dans le système"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Risque créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<RisqueResponse> create(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données du risque à créer",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple de création de risque",
                                    value = """
                                    {
                                      "code": "RIS-001",
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
            RisqueRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(risqueService.create(request));
    }

    @GetMapping
    @Operation(
            summary = "Lister tous les risques",
            description = "Retourne la liste complète des risques enregistrés"
    )
    public ResponseEntity<List<RisqueResponse>> getAll() {
        return ResponseEntity.ok(risqueService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer un risque par ID",
            description = "Retourne un risque via son identifiant technique (UUID)"
    )
    public ResponseEntity<RisqueResponse> getById(
            @Parameter(description = "Identifiant UUID du risque", example = "b3f1c2a0-8d1e-4c6a-9a5b-123456789abc")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(risqueService.getById(id));
    }

    @GetMapping("/code/{code}")
    @Operation(
            summary = "Récupérer un risque par code",
            description = "Retourne un risque via son code métier (ex: RIS-001)"
    )
    public ResponseEntity<RisqueResponse> getByCode(
            @Parameter(description = "Code métier du risque", example = "RIS-001")
            @PathVariable String code
    ) {
        return ResponseEntity.ok(risqueService.getByCode(code));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier un risque par ID",
            description = "Met à jour un risque existant via son identifiant technique"
    )
    public ResponseEntity<RisqueResponse> updateById(
            @Parameter(description = "UUID du risque", example = "b3f1c2a0-8d1e-4c6a-9a5b-123456789abc")
            @PathVariable String id,
            @Valid @RequestBody RisqueRequest request
    ) {
        return ResponseEntity.ok(risqueService.updateById(id, request));
    }

    @PutMapping("/code/{code}")
    @Operation(
            summary = "Modifier un risque par code",
            description = "Met à jour un risque via son code métier"
    )
    public ResponseEntity<RisqueResponse> updateByCode(
            @Parameter(description = "Code métier du risque", example = "RIS-001")
            @PathVariable String code,
            @Valid @RequestBody RisqueRequest request
    ) {
        return ResponseEntity.ok(risqueService.updateByCode(code, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un risque par ID",
            description = "Supprime un risque via son identifiant technique"
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID du risque", example = "b3f1c2a0-8d1e-4c6a-9a5b-123456789abc")
            @PathVariable String id
    ) {
        risqueService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/code/{code}")
    @Operation(
            summary = "Supprimer un risque par code",
            description = "Supprime un risque via son code métier"
    )
    public ResponseEntity<Void> deleteByCode(
            @Parameter(description = "Code métier du risque", example = "RIS-001")
            @PathVariable String code
    ) {
        risqueService.deleteByCode(code);
        return ResponseEntity.noContent().build();
    }
}
package com.example.SIGR.controller;

import com.example.SIGR.dto.request.UniteAdministrativeRequest;
import com.example.SIGR.dto.response.UniteAdministrativeResponse;
import com.example.SIGR.services.UniteAdministrativeService;
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
@RequestMapping("/api/uniteadministrative")
@Tag(name = "Unité administrative", description = "API de gestion des unités administratives")
public class UniteAdministrativeController {

    private final UniteAdministrativeService uniteService;

    public UniteAdministrativeController(UniteAdministrativeService uniteService) {
        this.uniteService = uniteService;
    }

    @PostMapping
    @Operation(
            summary = "Créer une unité administrative",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création unité",
                                    value = """
                                    {
                                      "id": "DGB",
                                      "libelle": "Direction Générale du Budget",
                                      "idTypeUnite": "DIR_GEN",
                                      "codeMinistere": "MEF",
                                      "idUniteParent": null,
                                      "niveauHierarchique": 2
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<UniteAdministrativeResponse> create(@Valid @RequestBody UniteAdministrativeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(uniteService.create(request));
    }

    @GetMapping
    @Operation(summary = "Lister toutes les unités")
    public ResponseEntity<List<UniteAdministrativeResponse>> getAll() {
        return ResponseEntity.ok(uniteService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une unité par son ID")
    public ResponseEntity<UniteAdministrativeResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(uniteService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier une unité administrative",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification unité",
                                    value = """
                                    {
                                      "libelle": "Direction Générale du Budget Modifiée",
                                      "idTypeUnite": "DIR_GEN",
                                      "codeMinistere": "MEF",
                                      "niveauHierarchique": 2
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<UniteAdministrativeResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UniteAdministrativeRequest request) {
        return ResponseEntity.ok(uniteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une unité administrative")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        uniteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
package com.example.SIGR.controller;

import com.example.SIGR.dto.request.TypeUniteRequest;
import com.example.SIGR.dto.response.TypeUniteResponse;
import com.example.SIGR.services.TypeUniteService;
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
@RequestMapping("/api/typeunite")
@Tag(name = "Type d'unité", description = "API de gestion des types d’unités administratives")
public class TypeUniteController {

    private final TypeUniteService typeUniteService;

    public TypeUniteController(TypeUniteService typeUniteService) {
        this.typeUniteService = typeUniteService;
    }

    @PostMapping
    @Operation(
            summary = "Créer un type d'unité",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création type unité",
                                    value = """
                                    {
                                     
                                      "libelle": "Direction Générale",
                                      "description": "Unités de niveau direction générale",
                                      "creePar": "admin"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<TypeUniteResponse> create(@Valid @RequestBody TypeUniteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(typeUniteService.create(request));
    }

    @GetMapping
    @Operation(summary = "Lister tous les types d'unités")
    public ResponseEntity<List<TypeUniteResponse>> getAll() {
        return ResponseEntity.ok(typeUniteService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un type d'unité par son ID")
    public ResponseEntity<TypeUniteResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(typeUniteService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier un type d'unité",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification type unité",
                                    value = """
                                    {
                                      "libelle": "Direction Technique",
                                      "description": "Unités techniques spécialisées",
                                      "creePar": "admin"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<TypeUniteResponse> update(
            @PathVariable String id,
            @Valid @RequestBody TypeUniteRequest request) {
        return ResponseEntity.ok(typeUniteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un type d'unité")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        typeUniteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
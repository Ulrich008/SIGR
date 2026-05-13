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
@Tag(name = "Type d'unite", description = "API de gestion des types d'unites administratives")
public class TypeUniteController {

    private final TypeUniteService typeUniteService;

    public TypeUniteController(TypeUniteService typeUniteService) {
        this.typeUniteService = typeUniteService;
    }

    // ================= CREATE =================
    @PostMapping
    @Operation(
            summary = "Creer un type d'unite",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple creation",
                                    value = """
                                    {
                                      "code": "TU-001",
                                      "libelle": "Direction Generale",
                                      "description": "Unite de niveau direction"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<TypeUniteResponse> create(
            @Valid @RequestBody TypeUniteRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(typeUniteService.create(request));
    }

    // ================= GET ALL =================
    @GetMapping
    @Operation(summary = "Lister tous les types d'unites")
    public ResponseEntity<List<TypeUniteResponse>> getAll() {
        return ResponseEntity.ok(typeUniteService.getAll());
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    @Operation(summary = "Recuperer un type d'unite par ID")
    public ResponseEntity<TypeUniteResponse> getById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(typeUniteService.getById(id));
    }

    // ================= GET BY CODE =================
    @GetMapping("/code/{code}")
    @Operation(summary = "Recuperer un type d'unite par code")
    public ResponseEntity<TypeUniteResponse> getByCode(
            @PathVariable String code
    ) {
        return ResponseEntity.ok(typeUniteService.getByCode(code));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier un type d'unite",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification",
                                    value = """
                                    {
                                      "code": "TU-002",
                                      "libelle": "Direction Technique",
                                      "description": "Unites techniques specialisees",
                                      "creePar": "admin"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<TypeUniteResponse> update(
            @PathVariable String id,
            @Valid @RequestBody TypeUniteRequest request
    ) {
        return ResponseEntity.ok(typeUniteService.update(id, request));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un type d'unite")
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ) {
        typeUniteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
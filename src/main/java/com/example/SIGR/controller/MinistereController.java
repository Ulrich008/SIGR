package com.example.SIGR.controller;

import com.example.SIGR.dto.request.MinistereRequest;
import com.example.SIGR.dto.response.MinistereResponse;
import com.example.SIGR.services.MinistereService;

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
@RequestMapping("/api/ministeres")
@Tag(name = "Ministère", description = "API de gestion des ministères")
public class MinistereController {

    private final MinistereService ministereService;

    public MinistereController(MinistereService ministereService) {
        this.ministereService = ministereService;
    }
    @PostMapping
    @Operation(
            summary = "Créer un ministère",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création ministère",
                                    value = """
                                    {
                                      "nom": "Ministère des Finances",
                                      "sigle": "MFIN",
                                      "description": "Gestion des finances publiques",
                                      "creePar": "admin"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<MinistereResponse> create(
            @Valid @org.springframework.web.bind.annotation.RequestBody MinistereRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ministereService.create(request));
    }

    // ================= GET ALL =================
    @GetMapping
    @Operation(summary = "Lister tous les ministères")
    public ResponseEntity<List<MinistereResponse>> getAll() {

        return ResponseEntity.ok(ministereService.getAll());
    }

    // ================= GET BY ID =================
    @GetMapping("/{code}")
    @Operation(summary = "Récupérer un ministère par code")
    public ResponseEntity<MinistereResponse> getById(@PathVariable String code) {

        return ResponseEntity.ok(ministereService.getById(code));
    }

    // ================= UPDATE =================
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier un ministère",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification ministère",
                                    value = """
                                    {
                                      "nom": "Ministère de l’Économie",
                                      "sigle": "ME",
                                      "description": "Mise à jour description",
                                      "creePar": "admin"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<MinistereResponse> update(
            @PathVariable String code,
            @Valid @org.springframework.web.bind.annotation.RequestBody MinistereRequest request) {

        return ResponseEntity.ok(ministereService.update(code, request));
    }

    // ================= DELETE =================
    @DeleteMapping("/{code}")
    @Operation(summary = "Supprimer un ministère")
    public ResponseEntity<Void> delete(@PathVariable String code) {

        ministereService.delete(code);

        return ResponseEntity.noContent().build();
    }
}
package com.example.SIGR.controller;

import com.example.SIGR.dto.request.ProcessusRequest;
import com.example.SIGR.dto.response.ProcessusResponse;
import com.example.SIGR.services.ProcessusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/processus")
@Tag(name = "Processus", description = "API de gestion des processus")
public class ProcessusController {

    private final ProcessusService processusService;

    public ProcessusController(ProcessusService processusService) {
        this.processusService = processusService;
    }

    // ================= CREATE =================
    @PostMapping
    @Operation(
            summary = "Créer un processus",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création processus",
                                    value = """
                                    {
                                      "libelle": "Gestion budgétaire",
                                      "finalite": "Assurer le suivi budgétaire",
                                      "typeProcessus": "METIER",
                                      "idUnite": "UNIT-001",
                                      "idProprietaire": "AGENT-001"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<ProcessusResponse> create(
            @Valid @RequestBody ProcessusRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(processusService.create(request));
    }

    // ================= GET ALL =================
    @GetMapping
    @Operation(summary = "Lister tous les processus")
    public ResponseEntity<List<ProcessusResponse>> getAll() {
        return ResponseEntity.ok(processusService.getAll());
    }

    // ================= GET BY CODE =================
    @GetMapping("/{code}")
    @Operation(summary = "Récupérer un processus par code")
    public ResponseEntity<ProcessusResponse> getByCode(
            @PathVariable String code
    ) {
        return ResponseEntity.ok(processusService.getByCode(code));
    }

    // ================= UPDATE =================
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier un processus",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification processus",
                                    value = """
                                    {
                                      "libelle": "Gestion financière",
                                      "finalite": "Mise à jour finalité",
                                      "typeProcessus": "SUPPORT",
                                      "idUnite": "UNIT-001",
                                      "idProprietaire": "AGENT-001"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<ProcessusResponse> update(
            @PathVariable String code,
            @Valid @RequestBody ProcessusRequest request
    ) {
        return ResponseEntity.ok(
                processusService.updateByCode(code, request)
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{code}")
    @Operation(summary = "Supprimer un processus")
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {
        processusService.deleteByCode(code);
        return ResponseEntity.noContent().build();
    }
}
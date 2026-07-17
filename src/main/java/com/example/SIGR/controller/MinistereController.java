package com.example.SIGR.controller;

import com.example.SIGR.dto.request.MinistereRequest;
import com.example.SIGR.dto.response.MinistereResponse;
import com.example.SIGR.services.MinistereService;

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
@RequestMapping("/api/ministeres")
@Tag(
        name = "Ministère",
        description = "API de gestion des ministères"
)
public class MinistereController {

    private final MinistereService ministereService;

    public MinistereController(MinistereService ministereService) {
        this.ministereService = ministereService;
    }

    /**
     * ================= CREATE =================
     */
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping
    @Operation(
            summary = "Créer un ministère",
            description = "Permet de créer un nouveau ministère avec un code métier unique",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création ministère",
                                    value = """
                                            {
                                              "code": "MIN-FIN",
                                              "nom": "Ministère des Finances",
                                              "sigle": "MFIN",
                                              "description": "Gestion des finances publiques"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<MinistereResponse> create(
            @Valid @RequestBody MinistereRequest request
    ) {

        MinistereResponse response =
                ministereService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * ================= GET ALL =================
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(
            summary = "Lister tous les ministères",
            description = "Retourne la liste complète des ministères enregistrés"
    )
    public ResponseEntity<List<MinistereResponse>> getAll() {

        return ResponseEntity.ok(
                ministereService.getAll()
        );
    }

    /**
     * ================= GET BY ID =================
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer un ministère par ID",
            description = "Recherche un ministère via son identifiant technique (UUID)"
    )
    public ResponseEntity<MinistereResponse> getById(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                ministereService.getById(id)
        );
    }

    /**
     * ================= GET BY CODE =================
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/code/{code}")
    @Operation(
            summary = "Récupérer un ministère par code métier",
            description = "Recherche un ministère via son code métier"
    )
    public ResponseEntity<MinistereResponse> getByCode(
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                ministereService.getByCode(code)
        );
    }

    /**
     * ================= UPDATE =================
     * Réservé au SUPER_ADMIN : un ADMIN peut consulter son propre
     * ministère mais ne peut ni le modifier, ni le supprimer.
     */
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier un ministère",
            description = "Met à jour les informations d'un ministère existant",
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
                                              "description": "Mise à jour description"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<MinistereResponse> update(
            @PathVariable String id,
            @Valid @RequestBody MinistereRequest request
    ) {

        return ResponseEntity.ok(
                ministereService.update(id, request)
        );
    }

    /**
     * ================= DELETE =================
     * Réservé au SUPER_ADMIN (voir update ci-dessus).
     */
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un ministère",
            description = "Supprime définitivement un ministère via son ID"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ) {

        ministereService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
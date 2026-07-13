package com.example.SIGR.controller;

import com.example.SIGR.dto.request.ProfilRequest;
import com.example.SIGR.dto.response.ProfilResponse;

import com.example.SIGR.services.ProfilService;

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
@RequestMapping("/api/profils")
@Tag(
        name = "Profil",
        description = "API de gestion des profils et permissions"
)
public class ProfilController {

    private final ProfilService profilService;

    public ProfilController(
            ProfilService profilService
    ) {
        this.profilService = profilService;
    }

    /**
     * ================= CREATE =================
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Créer un profil",
            description = "Permet de créer un nouveau profil",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création profil",
                                    value = """
                                            {
                                              "code": "CMMR",
                                              "libelle": "Comité Ministériel de Maîtrise des Risques",
                                              "description": "Profil chargé de validation des risques"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<ProfilResponse> create(
            @Valid @RequestBody ProfilRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        profilService.create(request)
                );
    }

    /**
     * ================= GET ALL =================
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(
            summary = "Lister tous les profils",
            description = "Retourne la liste complète des profils"
    )
    public ResponseEntity<List<ProfilResponse>> getAll() {

        return ResponseEntity.ok(
                profilService.getAll()
        );
    }

    /**
     * ================= GET BY CODE =================
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    @Operation(
            summary = "Rechercher un profil par code",
            description = "Retourne un profil via son code métier"
    )
    public ResponseEntity<ProfilResponse> getByCode(
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                profilService.getByCode(code)
        );
    }

    /**
     * ================= UPDATE =================
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier un profil",
            description = "Permet de modifier un profil existant",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification profil",
                                    value = """
                                            {
                                              "code": "CMMR",
                                              "libelle": "CMMR National",
                                              "description": "Validation et supervision des risques"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<ProfilResponse> update(
            @PathVariable String code,
            @Valid @RequestBody ProfilRequest request
    ) {

        return ResponseEntity.ok(
                profilService.update(code, request)
        );
    }

    /**
     * ================= DELETE =================
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer un profil",
            description = "Permet de supprimer un profil via son code"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {

        profilService.delete(code);

        return ResponseEntity
                .noContent()
                .build();
    }
}
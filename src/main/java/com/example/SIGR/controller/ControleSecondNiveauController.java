package com.example.SIGR.controller;

import com.example.SIGR.dto.request.ControleSecondNiveauRequest;
import com.example.SIGR.dto.response.ConstatsRecommandationsResponse;
import com.example.SIGR.dto.response.ControleSecondNiveauResponse;
import com.example.SIGR.dto.response.LigneControleResponse;
import com.example.SIGR.services.ControleSecondNiveauService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/controles-second-niveau")
@Tag(name = "Contrôle de second niveau", description = "API de gestion des contrôles de second niveau (Contrôle Interne)")
public class ControleSecondNiveauController {

    private final ControleSecondNiveauService service;

    public ControleSecondNiveauController(ControleSecondNiveauService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'CONTROLEUR_INTERNE')")
    @PostMapping
    @Operation(summary = "Créer un contrôle de second niveau")
    public ResponseEntity<ControleSecondNiveauResponse> create(@Valid @RequestBody ControleSecondNiveauRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "Lister tous les contrôles de second niveau")
    public ResponseEntity<List<ControleSecondNiveauResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    @Operation(summary = "Récupérer un contrôle de second niveau par code")
    public ResponseEntity<ControleSecondNiveauResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'CONTROLEUR_INTERNE')")
    @PutMapping("/{code}")
    @Operation(summary = "Modifier un contrôle de second niveau")
    public ResponseEntity<ControleSecondNiveauResponse> update(
            @PathVariable String code,
            @Valid @RequestBody ControleSecondNiveauRequest request
    ) {
        return ResponseEntity.ok(service.update(code, request));
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'CONTROLEUR_INTERNE')")
    @DeleteMapping("/{code}")
    @Operation(summary = "Supprimer un contrôle de second niveau")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        service.delete(code);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/constats-recommandations")
    @Operation(summary = "Constats et recommandations agrégés pour un couple Unité administrative + Processus")
    public ResponseEntity<ConstatsRecommandationsResponse> getConstatsEtRecommandations(
            @RequestParam String codeUnite,
            @RequestParam String codeProcessus
    ) {
        return ResponseEntity.ok(service.getConstatsEtRecommandations(codeUnite, codeProcessus));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/lignes")
    @Operation(summary = "Lignes détaillées (constat + analyse + recommandation reliés) pour un couple Unité administrative + Processus")
    public ResponseEntity<List<LigneControleResponse>> getLignesDetaillees(
            @RequestParam String codeUnite,
            @RequestParam String codeProcessus
    ) {
        return ResponseEntity.ok(service.getLignesDetaillees(codeUnite, codeProcessus));
    }
}

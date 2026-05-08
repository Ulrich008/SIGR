package com.example.SIGR.controller;

import com.example.SIGR.dto.request.CartographieRisquesRequest;
import com.example.SIGR.dto.response.CartographieRisquesResponse;
import com.example.SIGR.services.CartographieRisquesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartographies")
@Tag(name = "Cartographie des risques", description = "Gestion des cartographies de risques")
public class CartographieRisquesController {

    private final CartographieRisquesService service;

    public CartographieRisquesController(CartographieRisquesService service) {
        this.service = service;
    }

    // ================= CREATE =================
    @PostMapping
    @Operation(
            summary = "Créer une cartographie de risques",
            description = "Permet de créer une nouvelle cartographie avec ses seuils et sa période",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création cartographie",
                                    value = """
                                    {
                                      "id": "CARTO-001",
                                      "titre": "Cartographie des risques 2026",
                                      "periode": "2026-01-01",
                                      "seuilFaible": 30,
                                      "seuilMoyen": 60,
                                      "seuilEleve": 90,
                                      "statut": "BROUILLON"
                                    }
                                    """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cartographie créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<CartographieRisquesResponse> create(
            @Valid @RequestBody CartographieRisquesRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    // ================= GET ALL =================
    @GetMapping
    @Operation(
            summary = "Lister toutes les cartographies",
            description = "Retourne la liste de toutes les cartographies de risques"
    )
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    public List<CartographieRisquesResponse> getAll() {
        return service.getAll();
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir une cartographie par ID",
            description = "Permet de récupérer une cartographie spécifique"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cartographie trouvée"),
            @ApiResponse(responseCode = "404", description = "Cartographie introuvable")
    })
    public CartographieRisquesResponse getById(@PathVariable String id) {
        return service.getById(id);
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier une cartographie",
            description = "Met à jour les informations d'une cartographie existante",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification cartographie",
                                    value = """
                                    {
                                      "titre": "Cartographie mise à jour 2026",
                                      "periode": "2026-06-01",
                                      "seuilFaible": 25,
                                      "seuilMoyen": 55,
                                      "seuilEleve": 85,
                                      "statut": "ACTIVE"
                                    }
                                    """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "404", description = "Cartographie introuvable")
    })
    public CartographieRisquesResponse update(
            @PathVariable String id,
            @Valid @RequestBody CartographieRisquesRequest request
    ) {
        return service.update(id, request);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une cartographie",
            description = "Supprime une cartographie de risques par son identifiant"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Suppression réussie"),
            @ApiResponse(responseCode = "404", description = "Cartographie introuvable")
    })
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
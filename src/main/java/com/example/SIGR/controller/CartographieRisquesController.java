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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        value = "/api/cartographies",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Cartographie des risques", description = "Gestion des cartographies de risques")
public class CartographieRisquesController {

    private final CartographieRisquesService service;

    public CartographieRisquesController(CartographieRisquesService service) {
        this.service = service;
    }

    // ================= CREATE =================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Créer une cartographie de risques",
            description = "Création d'une nouvelle cartographie avec seuils et période",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création cartographie",
                                    value = """
                                    {
                                      "code": "CARTO-001",
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
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cartographie créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<CartographieRisquesResponse> create(
            @Valid @RequestBody CartographieRisquesRequest request
    ) {
        CartographieRisquesResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ================= GET ALL =================
    @GetMapping
    @Operation(
            summary = "Lister toutes les cartographies",
            description = "Retourne toutes les cartographies de risques"
    )
    public ResponseEntity<List<CartographieRisquesResponse>> getAll() {

        return ResponseEntity.ok(service.getAll());
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir une cartographie par ID",
            description = "Récupère une cartographie spécifique"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cartographie trouvée"),
            @ApiResponse(responseCode = "404", description = "Cartographie introuvable")
    })
    public ResponseEntity<CartographieRisquesResponse> getById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ================= GET BY CODE =================
    @GetMapping("/code/{code}")
    @Operation(
            summary = "Obtenir une cartographie par code",
            description = "Recherche une cartographie via son code métier"
    )
    public ResponseEntity<CartographieRisquesResponse> getByCode(
            @PathVariable String code
    ) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    // ================= UPDATE =================
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Modifier une cartographie",
            description = "Met à jour une cartographie existante",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification cartographie",
                                    value = """
                                    {
                                      "code": "CARTO-001",
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
    public ResponseEntity<CartographieRisquesResponse> update(
            @PathVariable String id,
            @Valid @RequestBody CartographieRisquesRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une cartographie",
            description = "Suppression définitive d'une cartographie"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Suppression réussie"),
            @ApiResponse(responseCode = "404", description = "Cartographie introuvable")
    })
    public ResponseEntity<Void> delete(@PathVariable String id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
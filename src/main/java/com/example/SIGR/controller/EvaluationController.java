package com.example.SIGR.controller;

import com.example.SIGR.dto.request.EvaluationRequest;
import com.example.SIGR.dto.response.CartographieRisqueDetailResponse;
import com.example.SIGR.dto.response.EvaluationResponse;
import com.example.SIGR.services.CartographieRisquesService;
import com.example.SIGR.services.EvaluationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@Tag(
        name = "Evaluation",
        description = "API de gestion des évaluations des risques inhérents et résiduels"
)
public class EvaluationController {

    private final EvaluationService evaluationService;
    private final CartographieRisquesService cartographieRisquesService;

    public EvaluationController(EvaluationService evaluationService, CartographieRisquesService cartographieRisquesService) {
        this.evaluationService = evaluationService;
        this.cartographieRisquesService = cartographieRisquesService;
    }

    // ================= CREATION =================

    /**
     * ADMIN :
     * - Création des évaluations
     *
     * RESPONSABLE_RISQUES :
     * - Création des évaluations (Évaluation)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES', 'RESPONSABLE_ACTION')")
    @PostMapping
    @Operation(
            summary = "Créer une évaluation",
            description = """
                    Crée une évaluation de risque basée sur :
                    - impact et probabilité inhérents
                    - protection et prévention
                    - calcul automatique du risque résiduel
                    - calcul automatique du rang de priorité
                    - génération automatique du code
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Évaluation créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    public ResponseEntity<EvaluationResponse> create(

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Données de création d'une évaluation",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création",
                                    value = """
                                            {
                                              "impactInherent": 4,
                                              "probabiliteInherente": 3,
                                              "protection": 2,
                                              "prevention": 1,

                                              "controleExistants": "Pare-feu actif et contrôle d'accès",
                                              "controleInexistants": "Absence de supervision continue",
                                              "dejaSurvenu": true,

                                              "dateDebut": "2026-05-21",
                                              "dateFin": "2026-06-01",

                                              "recommandation": "Renforcer les contrôles internes",
                                              "bonnesPratiques": "Audit régulier",

                                              "codeRisque": "RIS-001",
                                              "matriculeAgent": "AGT-001"
                                            }
                                            """
                            )
                    )
            )
            EvaluationRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(evaluationService.create(request));
    }

    // ================= LISTE =================

    /**
     * Tous les profils :
     * - Consultation en lecture seule (Évaluation)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(
            summary = "Lister toutes les évaluations",
            description = "Retourne la liste complète des évaluations avec leurs scores calculés (lecture seule pour tous les profils)"
    )
    public ResponseEntity<List<EvaluationResponse>> getAll() {

        return ResponseEntity.ok(
                evaluationService.getAll()
        );
    }

    // ================= GET BY CODE =================

    /**
     * Tous les profils :
     * - Consultation en lecture seule (Évaluation)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    @Operation(
            summary = "Récupérer une évaluation",
            description = "Retourne une évaluation via son code métier (lecture seule pour tous les profils)"
    )
    public ResponseEntity<EvaluationResponse> getByCode(

            @Parameter(
                    description = "Code de l'évaluation",
                    example = "EVAL-001"
            )
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                evaluationService.getByCode(code)
        );
    }

    // ================= UPDATE =================

    /**
     * ADMIN :
     * - Modification des évaluations
     *
     * RESPONSABLE_RISQUES :
     * - Modification des évaluations (Évaluation)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES', 'RESPONSABLE_ACTION')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier une évaluation",
            description = """
                    Met à jour une évaluation existante.

                    Règles :
                    - Le code de l'évaluation ne peut pas être modifié
                    - Les scores sont recalculés automatiquement
                    - Le rang de priorité est recalculé automatiquement
                    - Protection et prévention influencent le risque résiduel
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Évaluation mise à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Évaluation introuvable"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    public ResponseEntity<EvaluationResponse> update(

            @Parameter(
                    description = "Code de l'évaluation",
                    example = "EVAL-001"
            )
            @PathVariable String code,

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Données de mise à jour de l'évaluation",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple update",
                                    value = """
                                            {
                                              "impactInherent": 5,
                                              "probabiliteInherente": 4,
                                              "protection": 3,
                                              "prevention": 2,

                                              "controleExistants": "Audit trimestriel",
                                              "controleInexistants": "Absence de PRA",
                                              "dejaSurvenu": false,

                                              "dateDebut": "2026-06-11",
                                              "dateFin": "2026-06-30",

                                              "recommandation": "Renforcement global du contrôle interne",
                                              "bonnesPratiques": "Audit trimestriel",

                                              "codeRisque": "RIS-001",
                                              "matriculeAgent": "AGT-002"
                                            }
                                            """
                            )
                    )
            )
            EvaluationRequest request
    ) {

        return ResponseEntity.ok(
                evaluationService.update(code, request)
        );
    }

    // ================= DELETE =================

    /**
     * ADMIN :
     * - Suppression des évaluations
     *
     * RESPONSABLE_RISQUES :
     * - Suppression des évaluations (Évaluation)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES', 'RESPONSABLE_ACTION')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer une évaluation",
            description = "Supprime définitivement une évaluation via son code métier"
    )
    public ResponseEntity<Void> delete(

            @Parameter(
                    description = "Code de l'évaluation",
                    example = "EVAL-001"
            )
            @PathVariable String code
    ) {

        evaluationService.delete(code);

        return ResponseEntity.noContent().build();
    }

    /**
     * Tous les profils :
     * - Consultation en lecture seule (Évaluation)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail")
    @Operation(
            summary = "Détail complet de la cartographie des risques",
            description = """
                    Retourne une vue complète des risques avec :
                    - processus associé
                    - unité administrative
                    - scores inhérents et résiduels
                    - contrôles et plan d'action (lecture seule pour tous les profils)
                    """
    )
    public ResponseEntity<List<CartographieRisqueDetailResponse>> getDetail() {

        return ResponseEntity.ok(
                evaluationService.getCartographieDetail()
        );
    }



}


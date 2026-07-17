package com.example.SIGR.controller;

import com.example.SIGR.dto.request.AvisRisqueRequest;
import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.response.RisqueResponse;
import com.example.SIGR.services.RisqueService;

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
@RequestMapping("/api/risques")
@Tag(
        name = "Gestion des risques",
        description = """
                API permettant de gérer les risques :
                - création
                - modification
                - suppression
                - consultation
                """
)
public class RisqueController {

    private final RisqueService risqueService;

    public RisqueController(
            RisqueService risqueService
    ) {
        this.risqueService = risqueService;
    }

    /**
     * ================= CREATION =================
     *
     * ADMIN :
     * - Création des risques
     *
     * RESPONSABLE_RISQUES :
     * - Création des risques (Formalisation des risques)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES')")
    @PostMapping
    @Operation(
            summary = "Créer un risque",
            description = """
                    Permet de créer un nouveau risque.

                    Le code risque est généré automatiquement.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Risque créé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            )
    })
    public ResponseEntity<RisqueResponse> create(

            @Valid

            @RequestBody

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Données du risque à créer",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création risque",
                                    value = """
                                            {
                                              "libelle": "Fraude financière",
                                              "categorie": "Financier",
                                              "causeProbable": "Absence de contrôle",
                                              "consequenceProbable": "Perte financière",
                                              "statut": "ACTIF",
                                              "dateIdentification": "2026-05-07",
                                              "codeProcessus": "PROC-001",
                                              "codeCartographie": "CARTO-001",
                                              "typeRisque": "OPERATIONNEL"
                                            }
                                            """
                            )
                    )
            )

            RisqueRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        risqueService.create(request)
                );
    }

    /**
     * ================= LISTE =================
     *
     * Tous les profils :
     * - Consultation en lecture seule (Formalisation des risques)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(
            summary = "Lister tous les risques",
            description = "Retourne la liste complète des risques (lecture seule pour tous les profils)"
    )
    public ResponseEntity<List<RisqueResponse>> getAll() {

        return ResponseEntity.ok(
                risqueService.getAll()
        );
    }

    /**
     * ================= RECHERCHE PAR CODE =================
     *
     * Tous les profils :
     * - Consultation en lecture seule (Formalisation des risques)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    @Operation(
            summary = "Rechercher un risque par code",
            description = "Retourne les informations d'un risque via son code métier (lecture seule pour tous les profils)"
    )
    public ResponseEntity<RisqueResponse> getByCode(

            @Parameter(
                    description = "Code métier du risque",
                    example = "RIS-001"
            )

            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                risqueService.getByCode(code)
        );
    }

    /**
     * ================= MODIFICATION =================
     *
     * ADMIN :
     * - Modification des risques
     *
     * RESPONSABLE_RISQUES :
     * - Modification des risques (Formalisation des risques)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier un risque",
            description = """
                    Permet de modifier un risque existant.

                    Champ NON modifiable :
                    - code
                    """
    )
    public ResponseEntity<RisqueResponse> update(

            @Parameter(
                    description = "Code métier du risque",
                    example = "RIS-001"
            )

            @PathVariable String code,

            @Valid @RequestBody RisqueRequest request
    ) {

        return ResponseEntity.ok(
                risqueService.updateByCode(
                        code,
                        request
                )
        );
    }

    /**
     * ================= TRANSMETTRE =================
     *
     * RESPONSABLE_RISQUES :
     * - Fait entrer le dossier dans le circuit de validation
     *   (Formalisation -> Pilote), une fois les 4 étapes complétées.
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES')")
    @PatchMapping("/{code}/transmettre")
    @Operation(
            summary = "Transmettre un risque au circuit de validation",
            description = "Fait entrer le risque dans le circuit de validation (étape Pilote de processus)."
    )
    public ResponseEntity<RisqueResponse> transmettre(

            @Parameter(
                    description = "Code métier du risque",
                    example = "RIS-001"
            )
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                risqueService.transmettre(code)
        );
    }

    /**
     * ================= VALIDER / DIFFÉRER / REJETER =================
     *
     * CMMR, CCI, PILOTE :
     * - Se prononcent sur le risque (avis + motif) sans pouvoir en
     *   modifier le contenu. Chacun ne peut agir que sur les dossiers
     *   actuellement à sa propre étape (vérifié en service).
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'PILOTE', 'CCI', 'CMMR')")
    @PatchMapping("/{code}/avis")
    @Operation(
            summary = "Valider, différer ou rejeter un risque",
            description = """
                    Enregistre l'avis porté sur un risque (Valider, Différer,
                    Rejeter) ainsi que son motif éventuel, sans modifier le
                    contenu du risque. Le motif est obligatoire en cas de
                    différé ou de rejet. Fait avancer ou reculer le dossier
                    dans le circuit de validation (Pilote -> CCI -> CMMR).
                    """
    )
    public ResponseEntity<RisqueResponse> validerAvis(

            @Parameter(
                    description = "Code métier du risque",
                    example = "RIS-001"
            )
            @PathVariable String code,

            @Valid @RequestBody AvisRisqueRequest request
    ) {

        return ResponseEntity.ok(
                risqueService.validerAvis(code, request)
        );
    }

    /**
     * ================= SUPPRESSION =================
     *
     * ADMIN :
     * - Suppression des risques
     *
     * RESPONSABLE_RISQUES :
     * - Suppression des risques (Formalisation des risques)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer un risque",
            description = "Permet de supprimer un risque via son code métier"
    )
    public ResponseEntity<Void> delete(

            @Parameter(
                    description = "Code métier du risque",
                    example = "RIS-001"
            )

            @PathVariable String code
    ) {

        risqueService.deleteByCode(code);

        return ResponseEntity
                .noContent()
                .build();
    }
}
package com.example.SIGR.controller;

import com.example.SIGR.dto.request.AvisRisqueRequest;
import com.example.SIGR.dto.request.RisqueRequest;
import com.example.SIGR.dto.request.SuiviRecommandationRequest;
import com.example.SIGR.dto.response.AvisHistoriqueResponse;
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
     * MANAGER_RISQUE :
     * - Création des risques (Formalisation des risques)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'MANAGER_RISQUE', 'CORRESPONDANT_RISQUE')")
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
                                              "finalite": "Assurer le suivi budgétaire",
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
     * MANAGER_RISQUE :
     * - Modification des risques (Formalisation des risques)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'MANAGER_RISQUE', 'CORRESPONDANT_RISQUE')")
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
     * Relaie le dossier à l'étape suivante du circuit (sans avis) :
     * - CORRESPONDANT_RISQUE / MANAGER_RISQUE : fait entrer le dossier dans
     *   le circuit (Formalisation -> Manager Risque).
     * - MANAGER_RISQUE : relaie vers le CCI, ou renvoie au Correspondant
     *   pour correction après un différé du Responsable/CMMR.
     * - CCI : relaie vers le Responsable puis, une fois son visa obtenu,
     *   vers le CMMR.
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'MANAGER_RISQUE', 'CORRESPONDANT_RISQUE', 'CCI')")
    @PatchMapping("/{code}/transmettre")
    @Operation(
            summary = "Transmettre un risque à l'étape suivante du circuit de validation",
            description = "Relaie le risque à l'étape suivante du circuit (Formalisation -> Manager Risque -> CCI -> Responsable -> CCI -> CMMR), sans avis."
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
     * RESPONSABLE_RISQUES, CMMR :
     * - Seuls profils qui se prononcent réellement sur le risque (avis +
     *   motif) sans pouvoir en modifier le contenu — le CCI et le Manager
     *   Risque ne font que relayer (voir transmettre()). Chacun ne peut
     *   agir que sur les dossiers actuellement à sa propre étape (vérifié
     *   en service).
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'RESPONSABLE_RISQUES', 'CMMR')")
    @PatchMapping("/{code}/avis")
    @Operation(
            summary = "Valider, différer ou rejeter un risque",
            description = """
                    Enregistre l'avis porté sur un risque (Valider, Différer,
                    Rejeter) ainsi que son motif éventuel, sans modifier le
                    contenu du risque. Le motif est obligatoire en cas de
                    différé ou de rejet. Fait avancer le dossier (Responsable
                    -> CCI -> CMMR -> Validée) ou le renvoie à Manager Risque
                    en cas de différé.
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
     * ================= CLÔTURE =================
     *
     * CCI :
     * - Confirme que le risque est effectivement résolu et le clôture,
     *   indépendamment de l'étape où il se trouve dans le circuit de
     *   validation de la cartographie.
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'CCI')")
    @PatchMapping("/{code}/cloturer")
    @Operation(
            summary = "Clôturer un risque",
            description = "Réservé au CCI : confirme que le risque est résolu et le clôture définitivement."
    )
    public ResponseEntity<RisqueResponse> cloturer(

            @Parameter(
                    description = "Code métier du risque",
                    example = "RIS-001"
            )
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                risqueService.cloturer(code)
        );
    }

    /**
     * ================= HISTORIQUE DES AVIS =================
     *
     * Tous les profils :
     * - Consultation en lecture seule de l'historique complet des avis
     *   de validation (Transmis, Validé, Différé, Rejeté), y compris les
     *   décisions passées écrasées depuis sur le risque lui-même.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}/historique-avis")
    @Operation(
            summary = "Historique des avis de validation d'un risque",
            description = """
                    Retourne, dans l'ordre chronologique, chaque changement
                    d'avis/étape/motif survenu sur ce risque au fil de son
                    passage dans le circuit de validation — reconstitué à
                    partir des révisions Envers, puisque le risque lui-même
                    ne conserve que le dernier avis en date.
                    """
    )
    public ResponseEntity<List<AvisHistoriqueResponse>> getHistoriqueAvis(

            @Parameter(
                    description = "Code métier du risque",
                    example = "RIS-001"
            )
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                risqueService.getHistoriqueAvis(code)
        );
    }

    /**
     * ================= SUIVI DES ACTIONS DE MITIGATION =================
     *
     * ADMIN / MANAGER_RISQUE / CMMR :
     * - Peuvent renseigner le statut de suivi et la décision sur le suivi
     *   des actions de mitigation du risque (menu "Suivi des Risques").
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'MANAGER_RISQUE', 'CORRESPONDANT_RISQUE', 'CMMR')")
    @PatchMapping("/{code}/suivi")
    @Operation(
            summary = "Enregistrer le suivi des actions de mitigation d'un risque",
            description = "Permet de renseigner le statut d'avancement et la décision sur le suivi des actions de mitigation"
    )
    public ResponseEntity<RisqueResponse> enregistrerSuivi(
            @Parameter(description = "Code métier du risque", example = "RIS-001")
            @PathVariable String code,
            @Valid @RequestBody SuiviRecommandationRequest request
    ) {

        return ResponseEntity.ok(
                risqueService.enregistrerSuivi(code, request)
        );
    }

    /**
     * ================= SUPPRESSION =================
     *
     * ADMIN :
     * - Suppression des risques
     *
     * MANAGER_RISQUE :
     * - Suppression des risques (Formalisation des risques)
     */
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'MANAGER_RISQUE', 'CORRESPONDANT_RISQUE')")
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
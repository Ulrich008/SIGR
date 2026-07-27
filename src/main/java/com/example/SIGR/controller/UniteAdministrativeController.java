package com.example.SIGR.controller;

import com.example.SIGR.dto.request.UniteAdministrativeRequest;
import com.example.SIGR.dto.response.ImportResultResponse;
import com.example.SIGR.dto.response.UniteAdministrativeResponse;
import com.example.SIGR.services.UniteAdministrativeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import jakarta.validation.Valid;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/unite-administrative")
@Tag(
        name = "Unité administrative",
        description = "API de gestion des unités administratives"
)
public class UniteAdministrativeController {

    private final UniteAdministrativeService uniteService;

    public UniteAdministrativeController(
            UniteAdministrativeService uniteService
    ) {
        this.uniteService = uniteService;
    }

    /**
     * ================= CRÉATION =================
     *
     * ADMIN :
     * - Peut créer des unités administratives
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Créer une unité administrative",
            description = "Permet de créer une nouvelle unité administrative",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création unité",
                                    value = """
                                            {
                                              "code": "DGB",
                                              "libelle": "Direction Générale du Budget",
                                              "idTypeUnite": "DIR_GEN",
                                              "codeMinistere": "MEF",
                                              "idUniteParent": null,
                                              "niveauHierarchique": 2
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<UniteAdministrativeResponse> create(
            @Valid @RequestBody UniteAdministrativeRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        uniteService.create(request)
                );
    }

    /**
     * ================= IMPORT EXCEL =================
     *
     * ADMIN et SUPER_ADMIN uniquement.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Importer des unités administratives depuis un fichier Excel",
            description = """
                    Chaque ligne est traitée indépendamment : une ligne en
                    erreur n'empêche pas l'import des autres. Le fichier doit
                    respecter le modèle téléchargeable via GET /import/modele.
                    Une unité parente référencée doit déjà exister (en base
                    ou sur une ligne précédente du même fichier).
                    """
    )
    public ResponseEntity<ImportResultResponse> importUnites(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(uniteService.importFromExcel(file));
    }

    /**
     * ================= MODÈLE D'IMPORT =================
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @GetMapping("/import/modele")
    @Operation(
            summary = "Télécharger le modèle Excel pour l'import des unités administratives"
    )
    public ResponseEntity<byte[]> telechargerModeleImport() {
        byte[] modele = uniteService.generateImportTemplate();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .header(
                        org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("modele_import_unites_administratives.xlsx")
                                .build()
                                .toString()
                )
                .body(modele);
    }

    /**
     * ================= LISTE =================
     *
     * Tous les profils authentifiés :
     * - Consultation en lecture seule (données de référence)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(
            summary = "Lister toutes les unités",
            description = "Retourne la liste complète des unités administratives"
    )
    public ResponseEntity<List<UniteAdministrativeResponse>> getAll() {

        return ResponseEntity.ok(
                uniteService.getAll()
        );
    }

    /**
     * ================= RECHERCHE PAR CODE =================
     *
     * Tous les profils authentifiés :
     * - Consultation en lecture seule (données de référence)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    @Operation(
            summary = "Récupérer une unité par son code",
            description = "Retourne les informations d'une unité administrative à partir de son code"
    )
    public ResponseEntity<UniteAdministrativeResponse> getByCode(
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                uniteService.getByCode(code)
        );
    }

    /**
     * ================= MODIFICATION =================
     *
     * ADMIN :
     * - Peut modifier toutes les unités administratives
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{code}")
    @Operation(
            summary = "Modifier une unité administrative",
            description = "Permet de modifier une unité administrative existante",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification unité",
                                    value = """
                                            {
                                              "libelle": "Direction Générale du Budget Modifiée",
                                              "idTypeUnite": "DIR_GEN",
                                              "codeMinistere": "MEF",
                                              "niveauHierarchique": 2
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<UniteAdministrativeResponse> update(
            @PathVariable String code,
            @RequestBody UniteAdministrativeRequest request
    ) {

        return ResponseEntity.ok(
                uniteService.update(code, request)
        );
    }

    /**
     * ================= SUPPRESSION =================
     *
     * ADMIN :
     * - Peut supprimer une unité administrative
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{code}")
    @Operation(
            summary = "Supprimer une unité administrative",
            description = "Permet de supprimer une unité administrative via son code"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String code
    ) {

        uniteService.delete(code);

        return ResponseEntity
                .noContent()
                .build();
    }
}
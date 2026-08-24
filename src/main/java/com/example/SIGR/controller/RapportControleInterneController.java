package com.example.SIGR.controller;

import com.example.SIGR.dto.request.AvisRapportCIRequest;
import com.example.SIGR.dto.request.DecisionSuiviRequest;
import com.example.SIGR.dto.request.RapportControleInterneRequest;
import com.example.SIGR.dto.request.StatutSuiviRequest;
import com.example.SIGR.dto.response.AvisHistoriqueRapportCIResponse;
import com.example.SIGR.dto.response.RapportControleInterneResponse;
import com.example.SIGR.services.RapportControleInterneService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rapports-controle-interne")
@Tag(name = "Rapport de contrôle interne", description = "API de gestion des rapports de contrôle interne, de leur PDF et de leur circuit de validation")
public class RapportControleInterneController {

    private final RapportControleInterneService service;

    public RapportControleInterneController(RapportControleInterneService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'CONTROLEUR_INTERNE')")
    @PostMapping
    @Operation(summary = "Créer un rapport de contrôle interne")
    public ResponseEntity<RapportControleInterneResponse> create(@Valid @RequestBody RapportControleInterneRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "Lister tous les rapports de contrôle interne")
    public ResponseEntity<List<RapportControleInterneResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    @Operation(summary = "Récupérer un rapport de contrôle interne par code")
    public ResponseEntity<RapportControleInterneResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'CONTROLEUR_INTERNE')")
    @DeleteMapping("/{code}")
    @Operation(summary = "Supprimer un rapport de contrôle interne (notamment après un différé/rejet de la CCI, pour en recréer un nouveau)")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        service.delete(code);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'CONTROLEUR_INTERNE')")
    @PostMapping("/{code}/generer-pdf")
    @Operation(summary = "Générer (ou régénérer, tant qu'il n'est pas transmis) le PDF du rapport et le faire passer en attente de validation")
    public ResponseEntity<RapportControleInterneResponse> genererPdf(@PathVariable String code) {
        return ResponseEntity.ok(service.genererPdf(code));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}/pdf")
    @Operation(summary = "Télécharger le PDF déjà généré du rapport")
    public ResponseEntity<byte[]> getPdf(@PathVariable String code) {
        byte[] pdf = service.getPdf(code);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(code + ".pdf").build().toString())
                .body(pdf);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'CONTROLEUR_INTERNE')")
    @PostMapping("/{code}/transmettre")
    @Operation(summary = "Transmettre le rapport à la CCI pour avis")
    public ResponseEntity<RapportControleInterneResponse> transmettre(@PathVariable String code) {
        return ResponseEntity.ok(service.transmettre(code));
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'CCI')")
    @PatchMapping("/{code}/avis")
    @Operation(summary = "Enregistrer l'avis de la CCI sur un rapport transmis (Valider, Différer, Rejeter)")
    public ResponseEntity<RapportControleInterneResponse> validerAvis(
            @PathVariable String code,
            @Valid @RequestBody AvisRapportCIRequest request
    ) {
        return ResponseEntity.ok(service.validerAvis(code, request));
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'CONTROLEUR_INTERNE')")
    @PatchMapping("/{code}/suivi/statut")
    @Operation(summary = "Enregistrer le statut d'avancement du suivi des recommandations (Contrôleur Interne)")
    public ResponseEntity<RapportControleInterneResponse> enregistrerStatutSuivi(
            @PathVariable String code,
            @Valid @RequestBody StatutSuiviRequest request
    ) {
        return ResponseEntity.ok(service.enregistrerStatutSuivi(code, request));
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'CCI')")
    @PatchMapping("/{code}/suivi/decision")
    @Operation(summary = "Enregistrer la décision de la CCI sur le suivi des recommandations")
    public ResponseEntity<RapportControleInterneResponse> enregistrerDecisionSuivi(
            @PathVariable String code,
            @Valid @RequestBody DecisionSuiviRequest request
    ) {
        return ResponseEntity.ok(service.enregistrerDecisionSuivi(code, request));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}/historique-avis")
    @Operation(
            summary = "Historique des avis de validation d'un rapport de contrôle interne",
            description = """
                    Retourne, dans l'ordre chronologique, chaque changement
                    de statut/motif survenu sur ce rapport au fil de son
                    passage dans le circuit de validation (Transmis, Validé,
                    Différé, Rejeté) — reconstitué à partir des révisions
                    Envers. Fonctionne aussi pour un rapport déjà supprimé
                    depuis (cas d'un différé/rejet), puisque c'est alors la
                    seule trace qui en subsiste.
                    """
    )
    public ResponseEntity<List<AvisHistoriqueRapportCIResponse>> getHistoriqueAvis(
            @Parameter(description = "Code métier du rapport", example = "RCI_DGB001")
            @PathVariable String code
    ) {
        return ResponseEntity.ok(service.getHistoriqueAvis(code));
    }
}

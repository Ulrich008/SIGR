package com.example.SIGR.controller;

import com.example.SIGR.dto.request.AgentRequest;
import com.example.SIGR.dto.response.AgentResponse;
import com.example.SIGR.services.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@Tag(
        name = "Agent",
        description = "API de gestion des agents"
)
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    /**
     * ================= CREATION =================
     */
    @PostMapping
    @Operation(
            summary = "Créer un agent",
            description = "Permet de créer un nouvel agent dans le système",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple création agent",
                                    value = """
                                    {
                                      "matricule": "AGT001",
                                      "npi": "1234567890",
                                      "nom": "ASSOGBA",
                                      "prenoms": "Ulrich",
                                      "sexe": "MASCULIN",
                                      "role": "ADMIN",
                                      "dateNaissance": "1998-05-10",
                                      "datePriseService": "2024-01-15",
                                      "codeUnite": "DGB"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<AgentResponse> create(
            @Valid @RequestBody AgentRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agentService.create(request));
    }

    /**
     * ================= LISTE =================
     */
    @GetMapping
    @Operation(
            summary = "Lister tous les agents",
            description = "Retourne la liste complète des agents"
    )
    public ResponseEntity<List<AgentResponse>> getAll() {

        return ResponseEntity.ok(agentService.getAll());
    }

    /**
     * ================= RECHERCHE PAR MATRICULE =================
     */
    @GetMapping("/{matricule}")
    @Operation(
            summary = "Rechercher un agent par matricule",
            description = "Retourne les informations d’un agent à partir de son matricule"
    )
    public ResponseEntity<AgentResponse> getByMatricule(
            @PathVariable String matricule
    ) {

        return ResponseEntity.ok(
                agentService.getByMatricule(matricule)
        );
    }

    /**
     * ================= MODIFICATION =================
     */
    @PutMapping("/{matricule}")
    @Operation(
            summary = "Modifier un agent",
            description = "Permet de modifier les informations d’un agent",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple modification agent",
                                    value = """
                                    {
                                      "npi": "9876543210",
                                      "nom": "ASSOGBA",
                                      "prenoms": "Ulrich Junior",
                                      "sexe": "MASCULIN",
                                      "role": "UTILISATEUR",
                                      "dateNaissance": "1998-05-10",
                                      "datePriseService": "2024-01-15",
                                      "codeUnite": "DGB"
                                    }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<AgentResponse> update(
            @PathVariable String matricule,
            @Valid @RequestBody AgentRequest request
    ) {

        return ResponseEntity.ok(
                agentService.update(matricule, request)
        );
    }

    /**
     * ================= SUPPRESSION =================
     */
    @DeleteMapping("/{matricule}")
    @Operation(
            summary = "Supprimer un agent",
            description = "Permet de supprimer un agent via son matricule"
    )
    public ResponseEntity<Void> delete(
            @PathVariable String matricule
    ) {

        agentService.delete(matricule);

        return ResponseEntity.noContent().build();
    }
}
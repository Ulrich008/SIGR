package com.example.SIGR.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class RapportControleInterneRequest {

    @NotBlank(message = "L'unité administrative est obligatoire")
    private String codeUniteAdministrative;

    @NotBlank(message = "Le processus est obligatoire")
    private String codeProcessus;

    @NotNull(message = "La date d'émission est obligatoire")
    private LocalDate dateEmission;

    @Size(max = 2000, message = "Le préambule ne doit pas dépasser 2000 caractères")
    private String preambule;

    @Valid
    private List<ActionCorrectiveRequest> actionsCorrectives;

    @Size(max = 2000, message = "La conclusion ne doit pas dépasser 2000 caractères")
    private String conclusion;

    public String getCodeUniteAdministrative() { return codeUniteAdministrative; }
    public RapportControleInterneRequest setCodeUniteAdministrative(String codeUniteAdministrative) { this.codeUniteAdministrative = codeUniteAdministrative; return this; }

    public String getCodeProcessus() { return codeProcessus; }
    public RapportControleInterneRequest setCodeProcessus(String codeProcessus) { this.codeProcessus = codeProcessus; return this; }

    public LocalDate getDateEmission() { return dateEmission; }
    public RapportControleInterneRequest setDateEmission(LocalDate dateEmission) { this.dateEmission = dateEmission; return this; }

    public String getPreambule() { return preambule; }
    public RapportControleInterneRequest setPreambule(String preambule) { this.preambule = preambule; return this; }

    public List<ActionCorrectiveRequest> getActionsCorrectives() { return actionsCorrectives; }
    public RapportControleInterneRequest setActionsCorrectives(List<ActionCorrectiveRequest> actionsCorrectives) { this.actionsCorrectives = actionsCorrectives; return this; }

    public String getConclusion() { return conclusion; }
    public RapportControleInterneRequest setConclusion(String conclusion) { this.conclusion = conclusion; return this; }
}

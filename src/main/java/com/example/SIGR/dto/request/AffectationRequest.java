package com.example.SIGR.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AffectationRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotBlank(message = "Le matricule de l'agent est obligatoire")
    private String matriculeAgent;

    @NotBlank(message = "Le code de l'unité est obligatoire")
    private String codeUnite;

    @NotBlank(message = "Le poste est obligatoire")
    @Size(max = 100, message = "Le poste ne doit pas dépasser 100 caractères")
    private String poste;

    @NotNull(message = "La date d'affectation est obligatoire")
    private LocalDate dateAffectation;

    private LocalDate dateFinAffectation;

    // ================= GETTERS / SETTERS =================

    public String getCode() {
        return code;
    }

    public AffectationRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMatriculeAgent() {
        return matriculeAgent;
    }

    public AffectationRequest setMatriculeAgent(String matriculeAgent) {
        this.matriculeAgent = matriculeAgent;
        return this;
    }

    public String getCodeUnite() {
        return codeUnite;
    }

    public AffectationRequest setCodeUnite(String codeUnite) {
        this.codeUnite = codeUnite;
        return this;
    }

    public String getPoste() {
        return poste;
    }

    public AffectationRequest setPoste(String poste) {
        this.poste = poste;
        return this;
    }

    public LocalDate getDateAffectation() {
        return dateAffectation;
    }

    public AffectationRequest setDateAffectation(LocalDate dateAffectation) {
        this.dateAffectation = dateAffectation;
        return this;
    }

    public LocalDate getDateFinAffectation() {
        return dateFinAffectation;
    }

    public AffectationRequest setDateFinAffectation(LocalDate dateFinAffectation) {
        this.dateFinAffectation = dateFinAffectation;
        return this;
    }
}
package com.example.SIGR.dto.request;

import com.example.SIGR.entity.Role;
import com.example.SIGR.entity.Sexe;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class AgentRequest {

    /**
     * Obligatoire seulement à la création
     */
    @Size(max = 20, message = "Le matricule ne doit pas dépasser 20 caractères")
    private String matricule;

    /**
     * Facultatif lors du update
     */
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    @Size(max = 20, message = "Le NPI ne doit pas dépasser 20 caractères")
    private String npi;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
    private String prenoms;

    @NotNull(message = "Le sexe est obligatoire")
    private Sexe sexe;

    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateNaissance;

    @NotNull(message = "La date de prise de service est obligatoire")
    @PastOrPresent(message = "La date de prise de service ne peut pas être dans le futur")
    private LocalDate datePriseService;

    @NotBlank(message = "Le code de l'unité est obligatoire")
    private String codeUnite;

    // ================= GETTERS / SETTERS =================

    public String getMatricule() {
        return matricule;
    }

    public AgentRequest setMatricule(String matricule) {
        this.matricule = matricule;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AgentRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getNpi() {
        return npi;
    }

    public AgentRequest setNpi(String npi) {
        this.npi = npi;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public AgentRequest setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getPrenoms() {
        return prenoms;
    }

    public AgentRequest setPrenoms(String prenoms) {
        this.prenoms = prenoms;
        return this;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public AgentRequest setSexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public AgentRequest setRole(Role role) {
        this.role = role;
        return this;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public AgentRequest setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
        return this;
    }

    public LocalDate getDatePriseService() {
        return datePriseService;
    }

    public AgentRequest setDatePriseService(LocalDate datePriseService) {
        this.datePriseService = datePriseService;
        return this;
    }

    public String getCodeUnite() {
        return codeUnite;
    }

    public AgentRequest setCodeUnite(String codeUnite) {
        this.codeUnite = codeUnite;
        return this;
    }
}
package com.example.SIGR.dto.request;

import com.example.SIGR.entity.Role;
import com.example.SIGR.entity.Sexe;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class AgentRequest {

    /**
     * ================= PASSWORD =================
     * Obligatoire à la création
     * Facultatif à la modification
     */
    @Size(
            min = 6,
            message = "Le mot de passe doit contenir au moins 6 caractères"
    )
    private String password;

    /**
     * ================= NPI =================
     */
    @Size(
            max = 20,
            message = "Le NPI ne doit pas dépasser 20 caractères"
    )
    private String npi;

    /**
     * ================= NOM =================
     */
    @NotBlank(message = "Le nom est obligatoire")
    @Size(
            max = 50,
            message = "Le nom ne doit pas dépasser 50 caractères"
    )
    private String nom;

    /**
     * ================= PRENOMS =================
     */
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(
            max = 100,
            message = "Le prénom ne doit pas dépasser 100 caractères"
    )
    private String prenoms;

    /**
     * ================= SEXE =================
     */
    @NotNull(message = "Le sexe est obligatoire")
    private Sexe sexe;

    /**
     * ================= ROLE =================
     */
    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

    /**
     * ================= PROFIL =================
     * Obligatoire uniquement pour le rôle AGENT (voir AgentServiceImpl) :
     * un ADMIN/SUPER_ADMIN n'a pas de profil métier, son accès vient
     * uniquement de son rôle technique.
     */
    private String codeProfil;

    /**
     * ================= DATE NAISSANCE =================
     */
    @NotNull(message = "La date de naissance est obligatoire")
    @Past(
            message = "La date de naissance doit être dans le passé"
    )
    private LocalDate dateNaissance;

    /**
     * ================= DATE PRISE SERVICE =================
     */
    @NotNull(message = "La date de prise de service est obligatoire")
    @PastOrPresent(
            message = "La date de prise de service ne peut pas être dans le futur"
    )
    private LocalDate datePriseService;

    /**
     * ================= UNITE =================
     * Obligatoire pour AGENT/ADMIN (voir AgentServiceImpl) : un
     * SUPER_ADMIN a un accès global et n'est rattaché à aucune unité.
     */
    private String codeUnite;

    /**
     * ================= MINISTERE =================
     * Obligatoire pour tous les rôles, y compris SUPER_ADMIN : sans ce
     * champ, impossible de savoir de quel ministère relève l'agent une
     * fois connecté (même un SUPER_ADMIN doit avoir un ministère de
     * rattachement, seule son unité administrative est facultative).
     */
    @NotBlank(message = "Le code du ministère est obligatoire")
    private String codeMinistere;

    // =====================================================
    // ================= GETTERS / SETTERS =================
    // =====================================================

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

    public String getCodeProfil() {
        return codeProfil;
    }

    public AgentRequest setCodeProfil(String codeProfil) {
        this.codeProfil = codeProfil;
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

    public String getCodeMinistere() {
        return codeMinistere;
    }

    public AgentRequest setCodeMinistere(String codeMinistere) {
        this.codeMinistere = codeMinistere;
        return this;
    }
}
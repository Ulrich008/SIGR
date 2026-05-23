package com.example.SIGR.dto.response;

import com.example.SIGR.entity.Role;
import com.example.SIGR.entity.Sexe;

import java.time.LocalDate;

public class AgentResponse {

    /**
     * ================= ID =================
     */
    private String id;

    /**
     * ================= MATRICULE =================
     */
    private String matricule;

    /**
     * ================= INFORMATIONS =================
     */
    private String npi;

    private String nom;

    private String prenoms;

    private Sexe sexe;

    /**
     * ================= ROLE =================
     */
    private Role role;

    /**
     * ================= PROFIL =================
     */
    private String codeProfil;

    private String libelleProfil;

    /**
     * ================= STATUS =================
     */
    private Boolean enabled;

    /**
     * ================= DATES =================
     */
    private LocalDate dateNaissance;

    private LocalDate datePriseService;

    /**
     * ================= UNITE =================
     */
    private String codeUnite;

    private String libelleUnite;

    // =========================================================
    // ================= CONSTRUCTEUR ==========================
    // =========================================================

    public AgentResponse(
            String id,
            String matricule,
            String npi,
            String nom,
            String prenoms,
            Sexe sexe,
            Role role,
            String codeProfil,
            String libelleProfil,
            Boolean enabled,
            LocalDate dateNaissance,
            LocalDate datePriseService,
            String codeUnite,
            String libelleUnite
    ) {
        this.id = id;
        this.matricule = matricule;
        this.npi = npi;
        this.nom = nom;
        this.prenoms = prenoms;
        this.sexe = sexe;
        this.role = role;
        this.codeProfil = codeProfil;
        this.libelleProfil = libelleProfil;
        this.enabled = enabled;
        this.dateNaissance = dateNaissance;
        this.datePriseService = datePriseService;
        this.codeUnite = codeUnite;
        this.libelleUnite = libelleUnite;
    }

    // =========================================================
    // ================= GETTERS ===============================
    // =========================================================

    public String getId() {
        return id;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getNpi() {
        return npi;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenoms() {
        return prenoms;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public Role getRole() {
        return role;
    }

    public String getCodeProfil() {
        return codeProfil;
    }

    public String getLibelleProfil() {
        return libelleProfil;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public LocalDate getDatePriseService() {
        return datePriseService;
    }

    public String getCodeUnite() {
        return codeUnite;
    }

    public String getLibelleUnite() {
        return libelleUnite;
    }
}
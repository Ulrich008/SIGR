package com.example.SIGR.dto.response;

import com.example.SIGR.entity.Role;
import com.example.SIGR.entity.Sexe;

import java.time.LocalDate;

public class AgentResponse {

    /**
     * ID technique UUID
     */
    private String id;

    /**
     * Matricule métier
     */
    private String matricule;

    private String npi;

    private String nom;

    private String prenoms;

    private Sexe sexe;

    private Role role;

    private LocalDate dateNaissance;

    private LocalDate datePriseService;

    /**
     * Informations unité administrative
     */
    private String codeUnite;

    private String libelleUnite;

    public AgentResponse(String id,
                         String matricule,
                         String npi,
                         String nom,
                         String prenoms,
                         Sexe sexe,
                         Role role,
                         LocalDate dateNaissance,
                         LocalDate datePriseService,
                         String codeUnite,
                         String libelleUnite) {

        this.id = id;
        this.matricule = matricule;
        this.npi = npi;
        this.nom = nom;
        this.prenoms = prenoms;
        this.sexe = sexe;
        this.role = role;
        this.dateNaissance = dateNaissance;
        this.datePriseService = datePriseService;
        this.codeUnite = codeUnite;
        this.libelleUnite = libelleUnite;
    }

    // ================= GETTERS =================

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
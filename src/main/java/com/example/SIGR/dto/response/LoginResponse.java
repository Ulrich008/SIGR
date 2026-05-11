package com.example.SIGR.dto.response;

public class LoginResponse {

    /**
     * Token JWT
     */
    private String token;

    /**
     * Type du token
     */
    private String type = "Bearer";

    /**
     * Informations utilisateur
     */
    private String matricule;

    private String nom;

    private String prenoms;

    private String role;

    // ================= CONSTRUCTEUR =================

    public LoginResponse(
            String token,
            String matricule,
            String nom,
            String prenoms,
            String role
    ) {
        this.token = token;
        this.matricule = matricule;
        this.nom = nom;
        this.prenoms = prenoms;
        this.role = role;
    }

    // ================= GETTERS =================

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenoms() {
        return prenoms;
    }

    public String getRole() {
        return role;
    }
}
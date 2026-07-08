package com.example.SIGR.dto.response;

public class LoginResponse {

    private String token;
    private String type = "Bearer";
    private String matricule;
    private String nom;
    private String prenoms;
    private String role;
    private String codeProfil;      // ← ajouté
    private String libelleProfil;   // ← ajouté (optionnel, pratique pour l'affichage)
    private String codeUnite;
    private String codeMinistere;

    public LoginResponse(
            String token,
            String matricule,
            String nom,
            String prenoms,
            String role,
            String codeProfil,
            String libelleProfil,
            String codeUnite,
            String codeMinistere
    ) {
        this.token = token;
        this.matricule = matricule;
        this.nom = nom;
        this.prenoms = prenoms;
        this.role = role;
        this.codeProfil = codeProfil;
        this.libelleProfil = libelleProfil;
        this.codeUnite = codeUnite;
        this.codeMinistere = codeMinistere;
    }

    public String getToken() { return token; }
    public String getType() { return type; }
    public String getMatricule() { return matricule; }
    public String getNom() { return nom; }
    public String getPrenoms() { return prenoms; }
    public String getRole() { return role; }
    public String getCodeProfil() { return codeProfil; }
    public String getLibelleProfil() { return libelleProfil; }
    public String getCodeUnite() { return codeUnite; }
    public String getCodeMinistere() { return codeMinistere; }
}
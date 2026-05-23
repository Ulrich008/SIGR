package com.example.SIGR.dto.request;

import com.example.SIGR.entity.TypeProcessus;
import jakarta.validation.constraints.*;

public class ProcessusRequest {


    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 200, message = "Le libellé ne doit pas dépasser 200 caractères")
    private String libelle;

    @Size(max = 1000, message = "La finalité est trop longue")
    private String finalite;

    @NotNull(message = "Le type de processus est obligatoire")
    private TypeProcessus typeProcessus;

    @NotBlank(message = "L'identifiant de l'unité est obligatoire")
    private String idUnite;

    private String idProprietaire;

    // ================= GETTERS / SETTERS =================

    public String getCode() {
        return code;
    }

    public ProcessusRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public ProcessusRequest setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getFinalite() {
        return finalite;
    }

    public ProcessusRequest setFinalite(String finalite) {
        this.finalite = finalite;
        return this;
    }

    public TypeProcessus getTypeProcessus() {
        return typeProcessus;
    }

    public ProcessusRequest setTypeProcessus(TypeProcessus typeProcessus) {
        this.typeProcessus = typeProcessus;
        return this;
    }

    public String getIdUnite() {
        return idUnite;
    }

    public ProcessusRequest setIdUnite(String idUnite) {
        this.idUnite = idUnite;
        return this;
    }

    public String getIdProprietaire() {
        return idProprietaire;
    }

    public ProcessusRequest setIdProprietaire(String idProprietaire) {
        this.idProprietaire = idProprietaire;
        return this;
    }
}
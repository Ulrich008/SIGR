package com.example.SIGR.dto.request;

import com.example.SIGR.entity.StatutCartographie;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class CartographieRisquesRequest {

    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 200, message = "Le titre ne doit pas dépasser 200 caractères")
    private String titre;

    @NotNull(message = "La période est obligatoire")
    private LocalDate periode;

    @NotNull(message = "Le seuil faible est obligatoire")
    @Min(value = 0, message = "Le seuil faible doit être >= 0")
    private Integer seuilFaible;

    @NotNull(message = "Le seuil moyen est obligatoire")
    @Min(value = 0, message = "Le seuil moyen doit être >= 0")
    private Integer seuilMoyen;

    @NotNull(message = "Le seuil élevé est obligatoire")
    @Min(value = 0, message = "Le seuil élevé doit être >= 0")
    private Integer seuilEleve;

    @NotNull(message = "Le statut est obligatoire")
    private StatutCartographie statut;

    @NotBlank(message = "L'unité administrative est obligatoire")
    private String codeUniteAdministrative;

    // ================= GETTERS / SETTERS =================

    public String getCode() {
        return code;
    }

    public CartographieRisquesRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getTitre() {
        return titre;
    }

    public CartographieRisquesRequest setTitre(String titre) {
        this.titre = titre;
        return this;
    }

    public LocalDate getPeriode() {
        return periode;
    }

    public CartographieRisquesRequest setPeriode(LocalDate periode) {
        this.periode = periode;
        return this;
    }

    public Integer getSeuilFaible() {
        return seuilFaible;
    }

    public CartographieRisquesRequest setSeuilFaible(Integer seuilFaible) {
        this.seuilFaible = seuilFaible;
        return this;
    }

    public Integer getSeuilMoyen() {
        return seuilMoyen;
    }

    public CartographieRisquesRequest setSeuilMoyen(Integer seuilMoyen) {
        this.seuilMoyen = seuilMoyen;
        return this;
    }

    public Integer getSeuilEleve() {
        return seuilEleve;
    }

    public CartographieRisquesRequest setSeuilEleve(Integer seuilEleve) {
        this.seuilEleve = seuilEleve;
        return this;
    }

    public StatutCartographie getStatut() {
        return statut;
    }

    public CartographieRisquesRequest setStatut(StatutCartographie statut) {
        this.statut = statut;
        return this;
    }

    public String getCodeUniteAdministrative() {
        return codeUniteAdministrative;
    }

    public CartographieRisquesRequest setCodeUniteAdministrative(String codeUniteAdministrative) {
        this.codeUniteAdministrative = codeUniteAdministrative;
        return this;
    }
}
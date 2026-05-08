package com.example.SIGR.dto.request;

import com.example.SIGR.entity.StatutCartographie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CartographieRisquesRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String titre;

    @NotNull
    private LocalDate periode;

    @NotNull
    private Integer seuilFaible;

    @NotNull
    private Integer seuilMoyen;

    @NotNull
    private Integer seuilEleve;

    @NotNull
    private StatutCartographie statut;

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public CartographieRisquesRequest setId(String id) {
        this.id = id;
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
}
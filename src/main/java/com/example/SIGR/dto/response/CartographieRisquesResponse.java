package com.example.SIGR.dto.response;

import com.example.SIGR.entity.StatutCartographie;

import java.time.LocalDate;

public class CartographieRisquesResponse {

    private String id;
    private String code;
    private String titre;
    private LocalDate periode;

    private Integer seuilFaible;
    private Integer seuilMoyen;
    private Integer seuilEleve;

    private StatutCartographie statut;

    private String codeUniteAdministrative;
    private String libelleUniteAdministrative;

    private int nombreRisques;

    public CartographieRisquesResponse(
            String id,
            String code,
            String titre,
            LocalDate periode,
            Integer seuilFaible,
            Integer seuilMoyen,
            Integer seuilEleve,
            StatutCartographie statut,
            String codeUniteAdministrative,
            String libelleUniteAdministrative,
            int nombreRisques
    ) {
        this.id = id;
        this.code = code;
        this.titre = titre;
        this.periode = periode;
        this.seuilFaible = seuilFaible;
        this.seuilMoyen = seuilMoyen;
        this.seuilEleve = seuilEleve;
        this.statut = statut;
        this.codeUniteAdministrative = codeUniteAdministrative;
        this.libelleUniteAdministrative = libelleUniteAdministrative;
        this.nombreRisques = nombreRisques;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getTitre() {
        return titre;
    }

    public LocalDate getPeriode() {
        return periode;
    }

    public Integer getSeuilFaible() {
        return seuilFaible;
    }

    public Integer getSeuilMoyen() {
        return seuilMoyen;
    }

    public Integer getSeuilEleve() {
        return seuilEleve;
    }

    public StatutCartographie getStatut() {
        return statut;
    }

    public String getCodeUniteAdministrative() {
        return codeUniteAdministrative;
    }

    public String getLibelleUniteAdministrative() {
        return libelleUniteAdministrative;
    }

    public int getNombreRisques() {
        return nombreRisques;
    }
}
package com.example.SIGR.dto.response;

import java.time.LocalDate;

/**
 * Une "ligne" d'un Contrôle de second niveau (Tests, Revues, Vérification,
 * Évolution de conformité, Anomalie ou Faiblesse), avec son constat, son
 * analyse et sa recommandation toujours regroupés ensemble et rattachés à
 * leur contrôle d'origine — pour que le Rapport de contrôle interne (étapes
 * Préambule et Analyses/Recommandations, ainsi que le PDF) puisse afficher
 * ces éléments en sachant clairement à quoi chacun se rapporte, plutôt que
 * comme des listes plates mélangées.
 */
public class LigneControleResponse {

    private String codeControle;
    private LocalDate dateControle;
    private String categorie; // Tests | Revues | Vérification | Évolution de conformité | Anomalie | Faiblesse
    private String libelle;
    private String constat;
    private String analyse;
    private String recommandation;

    public LigneControleResponse(
            String codeControle,
            LocalDate dateControle,
            String categorie,
            String libelle,
            String constat,
            String analyse,
            String recommandation
    ) {
        this.codeControle = codeControle;
        this.dateControle = dateControle;
        this.categorie = categorie;
        this.libelle = libelle;
        this.constat = constat;
        this.analyse = analyse;
        this.recommandation = recommandation;
    }

    public String getCodeControle() { return codeControle; }
    public LocalDate getDateControle() { return dateControle; }
    public String getCategorie() { return categorie; }
    public String getLibelle() { return libelle; }
    public String getConstat() { return constat; }
    public String getAnalyse() { return analyse; }
    public String getRecommandation() { return recommandation; }
}

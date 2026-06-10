package com.example.SIGR.entity;

public enum StrategieRisque {
    TRAITER("Traiter (ou réduire)", "Mettre en place des dispositifs de contrôle interne pour réduire le risque"),
    TRANSFERER("Transférer (ou partager)", "Partager le risque avec une autre entité (ex : contrat d'assurance, externalisation, sous-traitance)"),
    TOLERER("Tolérer (ou accepter)", "Ne prendre aucune mesure particulière - le risque est jugé comme étant acceptable"),
    TERMINER("Terminer (ou supprimer)", "Abandonner l'activité à laquelle le risque est lié");

    private final String label;
    private final String description;

    StrategieRisque(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}

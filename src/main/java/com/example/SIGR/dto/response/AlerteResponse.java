package com.example.SIGR.dto.response;

import java.time.LocalDate;

public class AlerteResponse {
    private String id;
    private String type; // RISQUE_NON_GERE, INDICATEUR_PROCHE_SEUIL
    private String titre;
    private String description;
    private String codeElement; // code du risque ou de l'indicateur
    private String libelleElement;
    private LocalDate dateAlerte;
    private String severite; // CRITIQUE, HAUTE, MOYENNE, FAIBLE
    private String codeProcessus;
    private String libelleProcessus;
    
    public AlerteResponse() {}
    
    public AlerteResponse(String type, String titre, String description, String codeElement, 
                         String libelleElement, String severite, String codeProcessus, String libelleProcessus) {
        this.type = type;
        this.titre = titre;
        this.description = description;
        this.codeElement = codeElement;
        this.libelleElement = libelleElement;
        this.dateAlerte = LocalDate.now();
        this.severite = severite;
        this.codeProcessus = codeProcessus;
        this.libelleProcessus = libelleProcessus;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCodeElement() { return codeElement; }
    public void setCodeElement(String codeElement) { this.codeElement = codeElement; }
    
    public String getLibelleElement() { return libelleElement; }
    public void setLibelleElement(String libelleElement) { this.libelleElement = libelleElement; }
    
    public LocalDate getDateAlerte() { return dateAlerte; }
    public void setDateAlerte(LocalDate dateAlerte) { this.dateAlerte = dateAlerte; }
    
    public String getSeverite() { return severite; }
    public void setSeverite(String severite) { this.severite = severite; }
    
    public String getCodeProcessus() { return codeProcessus; }
    public void setCodeProcessus(String codeProcessus) { this.codeProcessus = codeProcessus; }
    
    public String getLibelleProcessus() { return libelleProcessus; }
    public void setLibelleProcessus(String libelleProcessus) { this.libelleProcessus = libelleProcessus; }
}

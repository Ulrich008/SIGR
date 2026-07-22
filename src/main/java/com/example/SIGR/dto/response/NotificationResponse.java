package com.example.SIGR.dto.response;

import com.example.SIGR.entity.SeveriteAlerte;
import com.example.SIGR.entity.TypeNotification;

import java.time.LocalDateTime;

public class NotificationResponse {

    private String id;
    private TypeNotification type;
    private String titre;
    private String description;
    private String codeElement;
    private String libelleElement;
    private String codeProcessus;
    private String libelleProcessus;
    private SeveriteAlerte severite;
    private LocalDateTime dateAlerte;
    private Boolean lu;
    private LocalDateTime dateLecture;

    public NotificationResponse(
            String id,
            TypeNotification type,
            String titre,
            String description,
            String codeElement,
            String libelleElement,
            String codeProcessus,
            String libelleProcessus,
            SeveriteAlerte severite,
            LocalDateTime dateAlerte,
            Boolean lu,
            LocalDateTime dateLecture
    ) {
        this.id = id;
        this.type = type;
        this.titre = titre;
        this.description = description;
        this.codeElement = codeElement;
        this.libelleElement = libelleElement;
        this.codeProcessus = codeProcessus;
        this.libelleProcessus = libelleProcessus;
        this.severite = severite;
        this.dateAlerte = dateAlerte;
        this.lu = lu;
        this.dateLecture = dateLecture;
    }

    public String getId() { return id; }
    public TypeNotification getType() { return type; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public String getCodeElement() { return codeElement; }
    public String getLibelleElement() { return libelleElement; }
    public String getCodeProcessus() { return codeProcessus; }
    public String getLibelleProcessus() { return libelleProcessus; }
    public SeveriteAlerte getSeverite() { return severite; }
    public LocalDateTime getDateAlerte() { return dateAlerte; }
    public Boolean getLu() { return lu; }
    public LocalDateTime getDateLecture() { return dateLecture; }
}

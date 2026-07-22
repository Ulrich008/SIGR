package com.example.SIGR.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Notification persistée, adressée à un agent destinataire précis
 * (ciblage par rôle/étape — voir NotificationServiceImpl). Contrairement
 * à l'ancien système Alerte (calcul à la volée, sans état), chaque ligne
 * ici a un identifiant stable et un statut lu/non-lu par destinataire.
 */
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_notification")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, nullable = false)
    private TypeNotification type;

    @Column(name = "titre", length = 200, nullable = false)
    private String titre;

    @Column(name = "description", length = 2000)
    private String description;

    /**
     * Code métier de l'élément concerné (risque, indicateur...).
     */
    @Column(name = "code_element", length = 50)
    private String codeElement;

    @Column(name = "libelle_element", length = 500)
    private String libelleElement;

    @Column(name = "code_processus", length = 50)
    private String codeProcessus;

    @Column(name = "libelle_processus", length = 500)
    private String libelleProcessus;

    @Enumerated(EnumType.STRING)
    @Column(name = "severite", length = 20, nullable = false)
    private SeveriteAlerte severite;

    @Column(name = "date_alerte", nullable = false)
    private LocalDateTime dateAlerte;

    /**
     * Agent destinataire — une notification appartient à un seul agent.
     * Le ciblage (quel(s) agent(s) reçoivent quelle alerte) est résolu en
     * amont dans NotificationServiceImpl, une ligne étant créée par
     * destinataire concerné.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "matricule_destinataire", referencedColumnName = "matricule_agent", nullable = false)
    private Agent destinataire;

    @Column(name = "lu", nullable = false)
    private Boolean lu = false;

    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;

    // ===================== GETTERS / SETTERS =====================

    public String getId() { return id; }
    public Notification setId(String id) { this.id = id; return this; }

    public TypeNotification getType() { return type; }
    public Notification setType(TypeNotification type) { this.type = type; return this; }

    public String getTitre() { return titre; }
    public Notification setTitre(String titre) { this.titre = titre; return this; }

    public String getDescription() { return description; }
    public Notification setDescription(String description) { this.description = description; return this; }

    public String getCodeElement() { return codeElement; }
    public Notification setCodeElement(String codeElement) { this.codeElement = codeElement; return this; }

    public String getLibelleElement() { return libelleElement; }
    public Notification setLibelleElement(String libelleElement) { this.libelleElement = libelleElement; return this; }

    public String getCodeProcessus() { return codeProcessus; }
    public Notification setCodeProcessus(String codeProcessus) { this.codeProcessus = codeProcessus; return this; }

    public String getLibelleProcessus() { return libelleProcessus; }
    public Notification setLibelleProcessus(String libelleProcessus) { this.libelleProcessus = libelleProcessus; return this; }

    public SeveriteAlerte getSeverite() { return severite; }
    public Notification setSeverite(SeveriteAlerte severite) { this.severite = severite; return this; }

    public LocalDateTime getDateAlerte() { return dateAlerte; }
    public Notification setDateAlerte(LocalDateTime dateAlerte) { this.dateAlerte = dateAlerte; return this; }

    public Agent getDestinataire() { return destinataire; }
    public Notification setDestinataire(Agent destinataire) { this.destinataire = destinataire; return this; }

    public Boolean getLu() { return lu; }
    public Notification setLu(Boolean lu) { this.lu = lu; return this; }

    public LocalDateTime getDateLecture() { return dateLecture; }
    public Notification setDateLecture(LocalDateTime dateLecture) { this.dateLecture = dateLecture; return this; }
}

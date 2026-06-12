package com.example.SIGR.entity;

import com.example.SIGR.entity.audit.Auditable;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import java.time.LocalDate;

@Entity
@Table(name = "mission")
@Audited
public class Mission extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_mission", updatable = false, nullable = false)
    private String id;

    @Column(name = "code_mission", length = 20, unique = true, nullable = false)
    private String code;

    @Column(name = "libelle_mission", length = 200, nullable = false)
    private String libelle;

    @Column(length = 500)
    private String description;

    /**
     * Une mission appartient à un processus.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_processus", nullable = false)
    private Processus processus;

    /**
     * Date de début de la mission.
     */
    @Column(name = "date_debut")
    private LocalDate dateDebut;

    /**
     * Date de fin de la mission.
     */
    @Column(name = "date_fin")
    private LocalDate dateFin;

    /**
     * Statut de la mission.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", length = 20)
    private StatutMission statut;

    /**
     * Responsable de la mission.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsable")
    private Agent responsable;

    // ================= GETTERS / SETTERS =================

    public String getId() {
        return id;
    }

    public Mission setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Mission setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public Mission setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Mission setDescription(String description) {
        this.description = description;
        return this;
    }

    public Processus getProcessus() {
        return processus;
    }

    public Mission setProcessus(Processus processus) {
        this.processus = processus;
        return this;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public Mission setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public Mission setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public StatutMission getStatut() {
        return statut;
    }

    public Mission setStatut(StatutMission statut) {
        this.statut = statut;
        return this;
    }

    public Agent getResponsable() {
        return responsable;
    }

    public Mission setResponsable(Agent responsable) {
        this.responsable = responsable;
        return this;
    }
}

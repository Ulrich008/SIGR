package com.example.SIGR.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Jeton à usage unique permettant à un agent de réinitialiser son mot de
 * passe suite au lien reçu par email (voir AuthServiceImpl.motDePasseOublie /
 * reinitialiserMotDePasse). Volontairement une table séparée plutôt qu'un
 * champ sur Agent : plusieurs demandes successives ne doivent pas se
 * marcher dessus, et un jeton expiré/utilisé doit rester traçable.
 */
@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_token", length = 50)
    private String id;

    @Column(name = "token", unique = true, nullable = false, length = 100)
    private String token;

    @ManyToOne
    @JoinColumn(name = "matricule_agent", referencedColumnName = "matricule_agent", nullable = false)
    private Agent agent;

    @Column(name = "date_expiration", nullable = false)
    private LocalDateTime dateExpiration;

    @Column(name = "utilise", nullable = false)
    private boolean utilise = false;

    public String getId() {
        return id;
    }

    public PasswordResetToken setId(String id) {
        this.id = id;
        return this;
    }

    public String getToken() {
        return token;
    }

    public PasswordResetToken setToken(String token) {
        this.token = token;
        return this;
    }

    public Agent getAgent() {
        return agent;
    }

    public PasswordResetToken setAgent(Agent agent) {
        this.agent = agent;
        return this;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public PasswordResetToken setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
        return this;
    }

    public boolean isUtilise() {
        return utilise;
    }

    public PasswordResetToken setUtilise(boolean utilise) {
        this.utilise = utilise;
        return this;
    }
}

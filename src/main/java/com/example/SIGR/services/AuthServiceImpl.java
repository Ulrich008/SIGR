package com.example.SIGR.services;

import com.example.SIGR.dto.request.ForgotPasswordRequest;
import com.example.SIGR.dto.request.LoginRequest;
import com.example.SIGR.dto.request.ResetPasswordRequest;
import com.example.SIGR.dto.response.LoginResponse;

import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.PasswordResetToken;

import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.PasswordResetTokenRepository;

import com.example.SIGR.security.JwtService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    /** Durée de validité d'un lien de réinitialisation de mot de passe. */
    private static final int DUREE_VALIDITE_TOKEN_HEURES = 1;

    private final AgentRepository agentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public AuthServiceImpl(
            AgentRepository agentRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            PasswordResetTokenRepository passwordResetTokenRepository,
            EmailService emailService
    ) {
        this.agentRepository = agentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        // Recherche de l'agent
        Agent agent = agentRepository
                .findByMatricule(request.getMatricule())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Matricule ou mot de passe incorrect"
                        )
                );

        // Vérification du compte
        if (Boolean.FALSE.equals(agent.getEnabled())) {

            throw new RuntimeException(
                    "Votre compte est désactivé"
            );
        }

        // Vérification mot de passe
        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        agent.getPassword()
                );

        if (!passwordMatches) {

            throw new RuntimeException(
                    "Matricule ou mot de passe incorrect"
            );
        }

        // Génération du token
        String token = jwtService.generateToken(
                agent.getMatricule(),
                agent.getRole().name()
        );

        // Réponse
        return new LoginResponse(
                token,
                agent.getMatricule(),
                agent.getNom(),
                agent.getPrenoms(),
                agent.getRole().name(),
                agent.getProfil() != null ? agent.getProfil().getCode() : null,
                agent.getProfil() != null ? agent.getProfil().getLibelle() : null,
                agent.getUnite() != null ? agent.getUnite().getCode() : null,
                agent.getMinistere() != null ? agent.getMinistere().getCode() : null
        );
    }

    // ================= MOT DE PASSE OUBLIÉ =================

    @Override
    public void motDePasseOublie(ForgotPasswordRequest request) {

        Optional<Agent> agentOpt = agentRepository.findByMatricule(request.getMatricule());

        // Ne jamais informer l'appelant si le matricule existe ou non, ni
        // s'il dispose d'un email — la réponse HTTP renvoyée par
        // AuthController est identique dans tous les cas.
        if (agentOpt.isEmpty()) {
            return;
        }

        Agent agent = agentOpt.get();

        if (agent.getEmail() == null || agent.getEmail().isBlank()) {
            return;
        }

        // Une nouvelle demande invalide toute demande précédente non utilisée.
        passwordResetTokenRepository.deleteByAgent_Matricule(agent.getMatricule());

        PasswordResetToken resetToken = new PasswordResetToken()
                .setToken(UUID.randomUUID().toString())
                .setAgent(agent)
                .setDateExpiration(LocalDateTime.now().plusHours(DUREE_VALIDITE_TOKEN_HEURES))
                .setUtilise(false);

        passwordResetTokenRepository.save(resetToken);

        String lien = frontendUrl + "/auth/reinitialiser-mot-de-passe?token=" + resetToken.getToken();

        emailService.envoyer(
                agent.getEmail(),
                "[SIGR] Réinitialisation de votre mot de passe",
                "Bonjour " + agent.getPrenoms() + ",\n\n"
                        + "Une demande de réinitialisation de mot de passe a été effectuée pour votre compte SIGR (matricule "
                        + agent.getMatricule() + ").\n\n"
                        + "Cliquez sur le lien suivant pour choisir un nouveau mot de passe (valable "
                        + DUREE_VALIDITE_TOKEN_HEURES + " heure) :\n"
                        + lien + "\n\n"
                        + "Si vous n'êtes pas à l'origine de cette demande, ignorez simplement cet email : "
                        + "votre mot de passe actuel reste inchangé."
        );
    }

    @Override
    public void reinitialiserMotDePasse(ResetPasswordRequest request) {

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Lien de réinitialisation invalide ou expiré"));

        if (resetToken.isUtilise() || resetToken.getDateExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Lien de réinitialisation invalide ou expiré");
        }

        Agent agent = resetToken.getAgent();
        agent.setPassword(passwordEncoder.encode(request.getNouveauMotDePasse()));
        agentRepository.save(agent);

        resetToken.setUtilise(true);
        passwordResetTokenRepository.save(resetToken);
    }
}
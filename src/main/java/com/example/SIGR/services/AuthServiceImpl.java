package com.example.SIGR.services;

import com.example.SIGR.dto.request.LoginRequest;
import com.example.SIGR.dto.response.LoginResponse;

import com.example.SIGR.entity.Agent;

import com.example.SIGR.repository.AgentRepository;

import com.example.SIGR.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AgentRepository agentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            AgentRepository agentRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.agentRepository = agentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
                agent.getRole().name()
        );
    }
}
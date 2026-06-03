package com.example.SIGR.config;

import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.Role;

import com.example.SIGR.repository.AgentRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class    DataInitializer implements CommandLineRunner {

    private final AgentRepository agentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            AgentRepository agentRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.agentRepository = agentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // Vérifie si un admin existe déjà
        boolean adminExists =
                agentRepository.existsByMatricule("ADMIN001");

        if (adminExists) {
            return;
        }

        // Création admin par défaut
        Agent admin = new Agent();

        admin.setMatricule("ADMIN001");

        admin.setPassword(
                passwordEncoder.encode("admin123")
        );

        admin.setNom("SUPER");

        admin.setPrenoms("ADMIN");

        admin.setRole(Role.ADMIN);

        admin.setEnabled(true);

        agentRepository.save(admin);

        System.out.println("""
                
                ================================
                ADMIN PAR DÉFAUT CRÉÉ
                Matricule : ADMIN001
                Password  : admin123
                ================================
                
                """);
    }
}
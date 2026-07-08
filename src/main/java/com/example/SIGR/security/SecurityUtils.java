package com.example.SIGR.security;

import com.example.SIGR.entity.Agent;
import com.example.SIGR.repository.AgentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {

    private final AgentRepository agentRepository;
    private static AgentRepository staticAgentRepository;

    public SecurityUtils(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
        SecurityUtils.staticAgentRepository = agentRepository;
    }

    public static String getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user.getUsername(); // matricule
        }
        return null;
    }

    public static String getCurrentRole() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(Object::toString)
                .orElse(null);
    }

    public static String getCurrentMinistereCode() {
        String matricule = getCurrentUser();
        if (matricule == null) {
            return null;
        }

        Optional<Agent> optionalAgent = staticAgentRepository.findByMatricule(matricule);

        if (optionalAgent.isEmpty() || optionalAgent.get().getMinistere() == null) {
            return null;
        }

        return optionalAgent.get().getMinistere().getCode();
    }
}
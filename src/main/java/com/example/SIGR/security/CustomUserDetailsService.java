package com.example.SIGR.security;

import com.example.SIGR.entity.Agent;
import com.example.SIGR.repository.AgentRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AgentRepository agentRepository;

    public CustomUserDetailsService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String matricule)
            throws UsernameNotFoundException {

        Agent agent = agentRepository
                .findByMatricule(matricule)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Agent introuvable : " + matricule
                        )
                );

        Set<GrantedAuthority> authorities = new HashSet<>();

        // Rôle technique (AGENT, MANAGER, ADMIN)
        if (agent.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority(agent.getRole().name()));
        }

        // Profil métier (CMMR, CCI, PILOTE, RESPONSABLE_RISQUES, ...)
        if (agent.getProfil() != null) {
            authorities.add(new SimpleGrantedAuthority(agent.getProfil().getCode()));
        }

        return new User(
                agent.getMatricule(),
                agent.getPassword(),
                agent.getEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}
package com.example.SIGR.security;

import com.example.SIGR.entity.Agent;
import com.example.SIGR.repository.AgentRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.Collections;

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

        return new User(
                agent.getMatricule(),
                agent.getPassword(),
                agent.getEnabled(),
                true,
                true,
                true,
                Collections.singleton(() ->
                        agent.getRole().name()
                )
        );
    }
}
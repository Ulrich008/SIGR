package com.example.SIGR.security;

import com.example.SIGR.repository.AgentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

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

    /**
     * Vérifie si l'utilisateur courant possède une autorité précise
     * (ex: "SUPER_ADMIN"). Utilisé pour les contournements globaux
     * (super-admin) qui ne doivent pas dépendre de l'ordre non garanti
     * d'un Set<GrantedAuthority>.
     */
    public static boolean hasAuthority(String authority) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(authority));
    }

    /**
     * Recherche le ministère de l'utilisateur courant en se retrouvant
     * lui-même via son matricule. Passe par une requête SQL native
     * (AgentRepository.findCodeMinistereByMatricule) plutôt que par
     * findByMatricule : une requête dérivée JPQL respecte le filtre
     * Hibernate "ministereFilter" actif sur la session, alors que c'est
     * précisément ce filtre que cette méthode sert à calculer. En passant
     * par le filtre, un blocage auto-référentiel serait possible. Une
     * requête native est entièrement immunisée contre ce filtre.
     */
    public static String getCurrentMinistereCode() {
        String matricule = getCurrentUser();
        if (matricule == null) {
            return null;
        }

        return staticAgentRepository.findCodeMinistereByMatricule(matricule);
    }

    /**
     * Code (métier) de l'unité administrative de l'utilisateur courant, sur
     * le même principe que getCurrentMinistereCode() (requête native pour
     * rester immunisé au filtre Hibernate qu'elle sert justement à
     * calculer). Utilisé pour le cantonnement du Correspondant Risque à sa
     * propre UA (voir MinistereInterceptor / filtre "uaFilter").
     */
    public static String getCurrentCodeUnite() {
        String matricule = getCurrentUser();
        if (matricule == null) {
            return null;
        }

        return staticAgentRepository.findCodeUniteByMatricule(matricule);
    }
}
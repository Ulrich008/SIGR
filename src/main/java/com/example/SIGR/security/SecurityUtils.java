package com.example.SIGR.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SecurityUtils {

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
}
package com.example.SIGR.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    /**
     * Clé secrète JWT
     * Minimum 32 caractères pour HS256
     */
    private static final String SECRET_KEY =
            "SIGR_SECRET_KEY_2026_ULRICH_APPLICATION";

    /**
     * Expiration : 24 heures
     */
    private static final long EXPIRATION =
            1000 * 60 * 60 * 24;

    /**
     * Génération de la clé
     */
    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes()
        );
    }

    /**
     * Génération du token JWT
     */
    public String generateToken(
            String matricule,
            String role
    ) {

        return Jwts.builder()

                // sujet principal
                .setSubject(matricule)

                // rôle
                .claim("role", role)

                // date création
                .setIssuedAt(new Date())

                // expiration
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION
                        )
                )

                // signature
                .signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    /**
     * Extraction du matricule
     */
    public String extractMatricule(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    /**
     * Extraction du rôle
     */
    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    /**
     * Vérifie si le token est expiré
     */
    public boolean isTokenExpired(String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    /**
     * Vérifie si le token est valide
     */
    public boolean isTokenValid(String token) {

        return !isTokenExpired(token);
    }

    /**
     * Extraction complète des claims
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()

                .setSigningKey(getSigningKey())

                .build()

                .parseClaimsJws(token)

                .getBody();
    }
}
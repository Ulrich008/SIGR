package com.example.SIGR.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Intercepte toute violation de contrainte SQL (clé étrangère, unicité...)
     * avant qu'elle ne remonte comme RuntimeException générique avec le
     * message brut de Postgres/Hibernate. Couvre TOUTES les suppressions
     * (et créations/modifications) de l'application, quelle que soit
     * l'entité : pas besoin de dupliquer un try/catch dans chaque service.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        Map<String, String> response = new HashMap<>();

        String cause = ex.getMostSpecificCause().getMessage();
        boolean estUneCleEtrangere = cause != null
                && cause.toLowerCase().contains("foreign key");

        response.put("message", estUneCleEtrangere
                ? "Suppression impossible : cet élément est encore utilisé par d'autres données. "
                        + "Supprimez ou modifiez d'abord les éléments qui en dépendent."
                : "Cette opération viole une contrainte d'unicité ou d'intégrité des données."
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();

        // Certaines exceptions (NullPointerException notamment) n'ont pas
        // de message : sans ce filet, le frontend reçoit {"message": null}
        // et affiche son texte générique "Données invalides", qui n'aide
        // pas à comprendre ce qui s'est réellement passé.
        boolean messageAbsent = ex.getMessage() == null || ex.getMessage().isBlank();
        if (messageAbsent) {
            // Sans message exploitable pour l'utilisateur, on garde au
            // moins la trace complète côté serveur pour pouvoir diagnostiquer.
            log.error("RuntimeException sans message", ex);
        }

        String message = messageAbsent
                ? "Une erreur inattendue est survenue (" + ex.getClass().getSimpleName() + ")."
                : ex.getMessage();

        response.put("message", message);

        // Retourner 401 pour les erreurs d'authentification
        if (ex.getMessage() != null &&
                (ex.getMessage().contains("Matricule ou mot de passe incorrect") ||
                        ex.getMessage().contains("désactivé") ||
                        ex.getMessage().contains("compte"))) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // Retourner 400 pour les autres erreurs
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
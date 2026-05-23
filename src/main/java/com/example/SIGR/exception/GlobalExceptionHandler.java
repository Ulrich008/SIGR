package com.example.SIGR.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());

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
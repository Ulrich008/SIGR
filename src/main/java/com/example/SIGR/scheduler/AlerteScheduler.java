package com.example.SIGR.scheduler;

import com.example.SIGR.dto.response.AlerteResponse;
import com.example.SIGR.services.AlerteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlerteScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AlerteScheduler.class);
    
    private final AlerteService alerteService;

    public AlerteScheduler(AlerteService alerteService) {
        this.alerteService = alerteService;
    }

    /**
     * Vérifie les alertes tous les jours à 8h00 du matin
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void verifierAlertesQuotidiennes() {
        logger.info("Début de la vérification quotidienne des alertes...");
        
        try {
            List<AlerteResponse> alertes = alerteService.detecterToutesAlertes();
            
            if (alertes.isEmpty()) {
                logger.info("Aucune alerte détectée.");
            } else {
                logger.info("{} alerte(s) détectée(s):", alertes.size());
                for (AlerteResponse alerte : alertes) {
                    logger.info(" - [{}] {}: {}", alerte.getSeverite(), alerte.getTitre(), alerte.getDescription());
                }
                
                // Ici, vous pouvez ajouter la logique pour envoyer des notifications
                // par email, SMS, ou via un système de notification interne
                envoyerNotifications(alertes);
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification des alertes", e);
        }
    }

    /**
     * Vérifie les alertes critiques toutes les heures
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void verifierAlertesCritiques() {
        logger.info("Vérification horaire des alertes critiques...");
        
        try {
            List<AlerteResponse> toutesAlertes = alerteService.detecterToutesAlertes();
            
            // Filtrer uniquement les alertes critiques
            List<AlerteResponse> alertesCritiques = toutesAlertes.stream()
                    .filter(a -> "CRITIQUE".equals(a.getSeverite()))
                    .toList();
            
            if (!alertesCritiques.isEmpty()) {
                logger.warn("{} alerte(s) critique(s) détectée(s):", alertesCritiques.size());
                for (AlerteResponse alerte : alertesCritiques) {
                    logger.warn(" - [CRITIQUE] {}: {}", alerte.getTitre(), alerte.getDescription());
                }
                
                // Envoyer des notifications immédiates pour les alertes critiques
                envoyerNotificationsCritiques(alertesCritiques);
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification des alertes critiques", e);
        }
    }

    private void envoyerNotifications(List<AlerteResponse> alertes) {
        // TODO: Implémenter l'envoi de notifications
        // - Email aux responsables
        // - Notifications dans l'application
        // - SMS pour les alertes critiques
        logger.info("Envoi de notifications pour {} alertes", alertes.size());
    }

    private void envoyerNotificationsCritiques(List<AlerteResponse> alertes) {
        // TODO: Implémenter l'envoi de notifications critiques
        // - Email immédiat aux responsables
        // - SMS aux responsables
        // - Notification dans l'application
        logger.warn("Envoi de notifications critiques pour {} alertes", alertes.size());
    }
}

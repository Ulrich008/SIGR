package com.example.SIGR.scheduler;

import com.example.SIGR.dto.response.NotificationResponse;
import com.example.SIGR.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sweep planifié de détection des situations à notifier (risques non
 * gérés, indicateurs proches de leur seuil, risques en attente de
 * validation). Tourne toutes les heures : la déduplication faite dans
 * NotificationServiceImpl (pas de doublon tant qu'une notification
 * identique n'a pas été lue) rend les exécutions répétées sans risque
 * de spam. L'envoi d'email pour les notifications CRITIQUE/HAUTE
 * nouvellement créées est géré directement par le service.
 */
@Component
public class AlerteScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AlerteScheduler.class);

    private final NotificationService notificationService;

    public AlerteScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void genererNotifications() {
        logger.info("Début du sweep de détection des notifications...");
        try {
            List<NotificationResponse> nouvelles = notificationService.genererNotifications();
            if (nouvelles.isEmpty()) {
                logger.info("Aucune nouvelle notification.");
            } else {
                logger.info("{} nouvelle(s) notification(s) créée(s) :", nouvelles.size());
                for (NotificationResponse n : nouvelles) {
                    logger.info(" - [{}] {} ({})", n.getSeverite(), n.getTitre(), n.getType());
                }
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la génération des notifications", e);
        }
    }
}

package com.example.SIGR.services;

import com.example.SIGR.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    /**
     * Détecte les situations à notifier (risques non gérés, indicateurs
     * proches de leur seuil, risques en attente de validation) et
     * persiste une notification par destinataire concerné — en évitant
     * les doublons tant qu'une notification identique n'a pas été lue.
     * Retourne uniquement les notifications nouvellement créées lors de
     * cet appel (utile pour ne notifier par email que les nouveautés).
     */
    List<NotificationResponse> genererNotifications();

    List<NotificationResponse> listerPourAgent(String matricule);

    long compterNonLuesPourAgent(String matricule);

    NotificationResponse obtenirParId(String id, String matricule);

    NotificationResponse marquerCommeLue(String id, String matricule);

    void marquerToutesCommeLues(String matricule);
}

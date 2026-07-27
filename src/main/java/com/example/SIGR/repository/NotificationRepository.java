package com.example.SIGR.repository;

import com.example.SIGR.entity.Notification;
import com.example.SIGR.entity.TypeNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> findByDestinataire_MatriculeOrderByDateAlerteDesc(String matricule);

    long countByDestinataire_MatriculeAndLuFalse(String matricule);

    Optional<Notification> findByIdAndDestinataire_Matricule(String id, String matricule);

    /**
     * Évite de recréer une notification en double pour le même agent tant
     * que la précédente (identique par type + élément concerné) n'a pas
     * été lue : le sweep planifié (voir AlerteScheduler) tourne toutes les
     * heures, sans cette vérification chaque exécution dupliquerait les
     * alertes non résolues.
     */
    boolean existsByDestinataire_MatriculeAndTypeAndCodeElementAndLuFalse(
            String matriculeDestinataire,
            TypeNotification type,
            String codeElement
    );

    List<Notification> findByDestinataire_MatriculeAndLuFalse(String matricule);

    /**
     * Purge les notifications d'un agent avant sa suppression : la colonne
     * matricule_destinataire est NOT NULL sans ON DELETE CASCADE, donc
     * supprimer un agent ayant déjà reçu une notification serait sinon
     * toujours rejeté par la contrainte de clé étrangère.
     */
    void deleteByDestinataire_Matricule(String matricule);
}

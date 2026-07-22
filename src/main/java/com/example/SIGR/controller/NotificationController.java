package com.example.SIGR.controller;

import com.example.SIGR.dto.response.NotificationResponse;
import com.example.SIGR.security.SecurityUtils;
import com.example.SIGR.services.NotificationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Toutes les routes ne portent que sur les notifications de l'agent
 * connecté (matricule résolu via SecurityUtils) : aucune notion de
 * "voir les notifications d'un autre agent" n'existe ici, par design.
 */
@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("isAuthenticated()")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationResponse> getMesNotifications() {
        return notificationService.listerPourAgent(SecurityUtils.getCurrentUser());
    }

    @GetMapping("/non-lues/nombre")
    public Map<String, Long> getNombreNonLues() {
        long total = notificationService.compterNonLuesPourAgent(SecurityUtils.getCurrentUser());
        return Map.of("nonLues", total);
    }

    @GetMapping("/{id}")
    public NotificationResponse getParId(@PathVariable String id) {
        return notificationService.obtenirParId(id, SecurityUtils.getCurrentUser());
    }

    @PatchMapping("/{id}/lu")
    public NotificationResponse marquerCommeLue(@PathVariable String id) {
        return notificationService.marquerCommeLue(id, SecurityUtils.getCurrentUser());
    }

    @PatchMapping("/tout-lu")
    public void marquerToutesCommeLues() {
        notificationService.marquerToutesCommeLues(SecurityUtils.getCurrentUser());
    }
}

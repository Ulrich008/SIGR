package com.example.SIGR.controller;

import com.example.SIGR.dto.response.AlerteResponse;
import com.example.SIGR.services.AlerteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alertes")
public class AlerteController {

    private final AlerteService alerteService;

    public AlerteController(AlerteService alerteService) {
        this.alerteService = alerteService;
    }

    @GetMapping("/risques-non-geres")
    public List<AlerteResponse> getRisquesNonGeres() {
        return alerteService.detecterRisquesNonGeres();
    }

    @GetMapping("/indicateurs-proches-seuil")
    public List<AlerteResponse> getIndicateursProchesSeuil() {
        return alerteService.detecterIndicateursProchesSeuil();
    }

    @GetMapping("/toutes")
    public List<AlerteResponse> getToutesAlertes() {
        return alerteService.detecterToutesAlertes();
    }
}

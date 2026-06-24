package com.example.SIGR.services;

import com.example.SIGR.dto.response.AlerteResponse;
import com.example.SIGR.entity.Action;
import com.example.SIGR.entity.IndicateurPerformance;
import com.example.SIGR.entity.PlanMitigation;
import com.example.SIGR.entity.Risque;
import com.example.SIGR.entity.StatutAction;
import com.example.SIGR.entity.StatutRisque;
import com.example.SIGR.repository.ActionRepository;
import com.example.SIGR.repository.IndicateurPerformanceRepository;
import com.example.SIGR.repository.PlanMitigationRepository;
import com.example.SIGR.repository.RisqueRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface AlerteService {
    List<AlerteResponse> detecterRisquesNonGeres();
    List<AlerteResponse> detecterIndicateursProchesSeuil();
    List<AlerteResponse> detecterToutesAlertes();
}

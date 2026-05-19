package com.example.SIGR.services;

import com.example.SIGR.dto.request.IndicateurPerformanceRequest;
import com.example.SIGR.dto.response.IndicateurPerformanceResponse;

import com.example.SIGR.entity.IndicateurPerformance;
import com.example.SIGR.entity.Processus;

import com.example.SIGR.repository.IndicateurPerformanceRepository;
import com.example.SIGR.repository.ProcessusRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndicateurPerformanceServiceImpl
        implements IndicateurPerformanceService {

    private final IndicateurPerformanceRepository repository;

    private final ProcessusRepository processusRepository;

    public IndicateurPerformanceServiceImpl(
            IndicateurPerformanceRepository repository,
            ProcessusRepository processusRepository
    ) {
        this.repository = repository;
        this.processusRepository = processusRepository;
    }

    /**
     * ================= CREATE =================
     */
    @Override
    public IndicateurPerformanceResponse create(
            IndicateurPerformanceRequest request
    ) {

        // ================= PROCESSUS =================

        Processus processus =
                processusRepository
                        .findByCode(request.getCodeProcessus())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Processus introuvable : "
                                                + request.getCodeProcessus()
                                )
                        );

        // ================= GENERATION CODE =================

        long total = repository.count() + 1;

        String code =
                String.format(
                        "KPI-%03d",
                        total
                );

        while (repository.existsByCode(code)) {

            total++;

            code = String.format(
                    "KPI-%03d",
                    total
            );
        }

        // ================= CREATION =================

        IndicateurPerformance kpi =
                new IndicateurPerformance();

        kpi.setCode(code);

        kpi.setLibelle(
                request.getLibelle()
        );

        kpi.setFrequence(
                request.getFrequence()
        );

        kpi.setValeurCible(
                request.getValeurCible()
        );

        kpi.setValeurObtenue(
                request.getValeurObtenue()
        );

        kpi.setSeuilAlerte(
                request.getSeuilAlerte()
        );

        kpi.setDateMesure(
                request.getDateMesure()
        );

        kpi.setProcessus(processus);

        /**
         * Valeur imposée par le système
         */
        kpi.setUniteMesure("%");

        IndicateurPerformance saved =
                repository.save(kpi);

        return toResponse(saved);
    }

    /**
     * ================= GET BY CODE =================
     */
    @Override
    public IndicateurPerformanceResponse getByCode(
            String code
    ) {

        IndicateurPerformance kpi =
                repository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "KPI introuvable : "
                                                + code
                                )
                        );

        return toResponse(kpi);
    }

    /**
     * ================= GET ALL =================
     */
    @Override
    public List<IndicateurPerformanceResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * ================= UPDATE =================
     */
    @Override
    public IndicateurPerformanceResponse update(
            String code,
            IndicateurPerformanceRequest request
    ) {

        // ================= KPI =================

        IndicateurPerformance kpi =
                repository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "KPI introuvable : "
                                                + code
                                )
                        );

        // ================= PROCESSUS =================

        Processus processus =
                processusRepository
                        .findByCode(request.getCodeProcessus())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Processus introuvable : "
                                                + request.getCodeProcessus()
                                )
                        );

        // ================= UPDATE =================

        kpi.setLibelle(
                request.getLibelle()
        );

        kpi.setFrequence(
                request.getFrequence()
        );

        kpi.setValeurCible(
                request.getValeurCible()
        );

        kpi.setValeurObtenue(
                request.getValeurObtenue()
        );

        kpi.setSeuilAlerte(
                request.getSeuilAlerte()
        );

        kpi.setDateMesure(
                request.getDateMesure()
        );

        kpi.setProcessus(processus);

        /**
         * Valeur imposée par le système
         */
        kpi.setUniteMesure("%");

        IndicateurPerformance updated =
                repository.save(kpi);

        return toResponse(updated);
    }

    /**
     * ================= DELETE =================
     */
    @Override
    public void delete(
            String code
    ) {

        IndicateurPerformance kpi =
                repository.findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "KPI introuvable : "
                                                + code
                                )
                        );

        repository.delete(kpi);
    }

    /**
     * ================= RESPONSE =================
     */
    private IndicateurPerformanceResponse toResponse(
            IndicateurPerformance kpi
    ) {

        return new IndicateurPerformanceResponse(
                kpi.getId(),
                kpi.getCode(),
                kpi.getLibelle(),
                kpi.getUniteMesure(),
                kpi.getFrequence(),
                kpi.getValeurCible(),
                kpi.getValeurObtenue(),
                kpi.getSeuilAlerte(),
                kpi.getDateMesure(),

                kpi.getProcessus() != null
                        ? kpi.getProcessus().getCode()
                        : null,

                kpi.getProcessus() != null
                        ? kpi.getProcessus().getLibelle()
                        : null,

                kpi.getEcartCible(),
                kpi.getStatut()
        );
    }
}
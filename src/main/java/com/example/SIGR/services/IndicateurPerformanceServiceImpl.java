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
public class IndicateurPerformanceServiceImpl implements IndicateurPerformanceService {

    private final IndicateurPerformanceRepository repository;
    private final ProcessusRepository processusRepository;

    public IndicateurPerformanceServiceImpl(
            IndicateurPerformanceRepository repository,
            ProcessusRepository processusRepository
    ) {
        this.repository = repository;
        this.processusRepository = processusRepository;
    }

    // ================= CREATE =================

    @Override
    public IndicateurPerformanceResponse create(IndicateurPerformanceRequest request) {

        if (repository.existsByCode(request.getCode())) {
            throw new RuntimeException(
                    "Code KPI déjà existant : " + request.getCode()
            );
        }

        Processus processus = processusRepository.findById(request.getCodeProcessus())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Processus introuvable : " + request.getCodeProcessus()
                        )
                );

        IndicateurPerformance kpi = new IndicateurPerformance()
                .setCode(request.getCode())
                .setLibelle(request.getLibelle())
                .setFrequence(request.getFrequence())
                .setValeurCible(request.getValeurCible())
                .setValeurObtenue(request.getValeurObtenue())
                .setSeuilAlerte(request.getSeuilAlerte())
                .setDateMesure(request.getDateMesure())
                .setProcessus(processus);

        /**
         * L’unité de mesure est toujours %
         */
        kpi.setUniteMesure("%");

        return toResponse(repository.save(kpi));
    }

    // ================= GET BY ID =================

    @Override
    public IndicateurPerformanceResponse getById(String id) {

        IndicateurPerformance kpi = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "KPI introuvable (id) : " + id
                        )
                );

        return toResponse(kpi);
    }

    // ================= GET ALL =================

    @Override
    public List<IndicateurPerformanceResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================

    @Override
    public IndicateurPerformanceResponse update(
            String id,
            IndicateurPerformanceRequest request
    ) {

        IndicateurPerformance kpi = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "KPI introuvable (id) : " + id
                        )
                );

        Processus processus = processusRepository.findById(request.getCodeProcessus())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Processus introuvable : " + request.getCodeProcessus()
                        )
                );

        kpi.setCode(request.getCode())
                .setLibelle(request.getLibelle())
                .setFrequence(request.getFrequence())
                .setValeurCible(request.getValeurCible())
                .setValeurObtenue(request.getValeurObtenue())
                .setSeuilAlerte(request.getSeuilAlerte())
                .setDateMesure(request.getDateMesure())
                .setProcessus(processus);

        /**
         * Valeur imposée par le système
         */
        kpi.setUniteMesure("%");

        return toResponse(repository.save(kpi));
    }

    // ================= DELETE =================

    @Override
    public void delete(String id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException(
                    "KPI introuvable (id) : " + id
            );
        }

        repository.deleteById(id);
    }

    // ================= MAPPER =================

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
                kpi.getProcessus().getCode(),
                kpi.getProcessus().getLibelle(),
                kpi.getEcartCible(),
                kpi.getStatut()
        );
    }
}
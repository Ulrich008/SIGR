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

    @Override
    public IndicateurPerformanceResponse create(IndicateurPerformanceRequest request) {

        if (repository.existsByCode(request.getCode())) {
            throw new RuntimeException("Code KPI existe déjà : " + request.getCode());
        }

        Processus processus = processusRepository.findById(request.getCodeProcessus())
                .orElseThrow(() ->
                        new RuntimeException("Processus introuvable : " + request.getCodeProcessus())
                );

        IndicateurPerformance kpi = new IndicateurPerformance()
                .setCode(request.getCode())
                .setLibelle(request.getLibelle())
                .setUniteMesure(request.getUniteMesure())
                .setFrequence(request.getFrequence())
                .setValeurCible(request.getValeurCible())
                .setValeurObtenue(request.getValeurObtenue())
                .setSeuilAlerte(request.getSeuilAlerte())
                .setDateMesure(request.getDateMesure())
                .setProcessus(processus);

        IndicateurPerformance saved = repository.save(kpi);

        return toResponse(saved);
    }

    @Override
    public IndicateurPerformanceResponse getById(String code) {

        IndicateurPerformance kpi = repository.findById(code)
                .orElseThrow(() ->
                        new RuntimeException("KPI introuvable : " + code)
                );

        return toResponse(kpi);
    }

    @Override
    public List<IndicateurPerformanceResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public IndicateurPerformanceResponse update(String code, IndicateurPerformanceRequest request) {

        IndicateurPerformance kpi = repository.findById(code)
                .orElseThrow(() ->
                        new RuntimeException("KPI introuvable : " + code)
                );

        Processus processus = processusRepository.findById(request.getCodeProcessus())
                .orElseThrow(() ->
                        new RuntimeException("Processus introuvable : " + request.getCodeProcessus())
                );

        kpi.setLibelle(request.getLibelle())
                .setUniteMesure(request.getUniteMesure())
                .setFrequence(request.getFrequence())
                .setValeurCible(request.getValeurCible())
                .setValeurObtenue(request.getValeurObtenue())
                .setSeuilAlerte(request.getSeuilAlerte())
                .setDateMesure(request.getDateMesure())
                .setProcessus(processus);

        return toResponse(repository.save(kpi));
    }

    @Override
    public void delete(String code) {

        if (!repository.existsById(code)) {
            throw new RuntimeException("KPI introuvable : " + code);
        }

        repository.deleteById(code);
    }

    private IndicateurPerformanceResponse toResponse(IndicateurPerformance kpi) {

        return new IndicateurPerformanceResponse(
                kpi.getCode(),
                kpi.getLibelle(),
                kpi.getUniteMesure(),
                kpi.getFrequence(),
                kpi.getValeurCible(),
                kpi.getValeurObtenue(),
                kpi.getSeuilAlerte(),
                kpi.getDateMesure(),
                kpi.getProcessus().getCode(),
                kpi.getProcessus().getNom(),
                kpi.getEcartCible(),
                kpi.getStatut()
        );
    }
}
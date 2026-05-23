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

    // ================= VALIDATION METIER =================

    private void validerChamps(IndicateurPerformanceRequest request) {

        double valeurCible   = request.getValeurCible();
        double valeurObtenue = request.getValeurObtenue();
        double seuilAlerte   = request.getSeuilAlerte();

        // ✅ Règle 1 : aucune valeur ne dépasse 100%
        if (valeurCible > 100) {
            throw new RuntimeException(
                    "La valeur cible ne peut pas dépasser 100% (valeur saisie : " + valeurCible + ")"
            );
        }
        if (valeurObtenue > 100) {
            throw new RuntimeException(
                    "La valeur obtenue ne peut pas dépasser 100% (valeur saisie : " + valeurObtenue + ")"
            );
        }
        if (seuilAlerte > 100) {
            throw new RuntimeException(
                    "Le seuil d'alerte ne peut pas dépasser 100% (valeur saisie : " + seuilAlerte + ")"
            );
        }

        // ✅ Règle 2 : aucune valeur n'est négative
        if (valeurCible < 0) {
            throw new RuntimeException(
                    "La valeur cible ne peut pas être négative."
            );
        }
        if (valeurObtenue < 0) {
            throw new RuntimeException(
                    "La valeur obtenue ne peut pas être négative."
            );
        }
        if (seuilAlerte < 0) {
            throw new RuntimeException(
                    "Le seuil d'alerte ne peut pas être négatif."
            );
        }

        // ✅ Règle 3 : la valeur cible doit être inférieure ou égale à la valeur obtenue
        if (valeurCible <=  valeurObtenue) {
            throw new RuntimeException(
                    "La valeur cible (" + valeurCible + "%) ne peut pas être supérieure "
                            + "à la valeur obtenue (" + valeurObtenue + "%)."
            );
        }
    }

    // ================= CREATE =================
    @Override
    public IndicateurPerformanceResponse create(
            IndicateurPerformanceRequest request
    ) {
        // ✅ Validation métier
        validerChamps(request);

        // ================= PROCESSUS =================
        Processus processus = processusRepository
                .findByCode(request.getCodeProcessus())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Processus introuvable : " + request.getCodeProcessus()
                        )
                );

        // ================= GENERATION CODE =================
        long total = repository.count() + 1;
        String code = String.format("KPI-%03d", total);

        while (repository.existsByCode(code)) {
            total++;
            code = String.format("KPI-%03d", total);
        }

        // ================= CREATION =================
        IndicateurPerformance kpi = new IndicateurPerformance();

        kpi.setCode(code);
        kpi.setLibelle(request.getLibelle());
        kpi.setFrequence(request.getFrequence());
        kpi.setValeurCible(request.getValeurCible());
        kpi.setValeurObtenue(request.getValeurObtenue());
        kpi.setSeuilAlerte(request.getSeuilAlerte());
        kpi.setDateMesure(request.getDateMesure());
        kpi.setProcessus(processus);
        // Valeur imposée par le système
        kpi.setUniteMesure("%");

        return toResponse(repository.save(kpi));
    }

    // ================= GET BY CODE =================
    @Override
    public IndicateurPerformanceResponse getByCode(String code) {

        IndicateurPerformance kpi = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("KPI introuvable : " + code)
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
            String code,
            IndicateurPerformanceRequest request
    ) {
        // ✅ Validation métier
        validerChamps(request);

        // ================= KPI =================
        IndicateurPerformance kpi = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("KPI introuvable : " + code)
                );

        // ================= PROCESSUS =================
        Processus processus = processusRepository
                .findByCode(request.getCodeProcessus())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Processus introuvable : " + request.getCodeProcessus()
                        )
                );

        // ================= UPDATE =================
        kpi.setLibelle(request.getLibelle());
        kpi.setFrequence(request.getFrequence());
        kpi.setValeurCible(request.getValeurCible());
        kpi.setValeurObtenue(request.getValeurObtenue());
        kpi.setSeuilAlerte(request.getSeuilAlerte());
        kpi.setDateMesure(request.getDateMesure());
        kpi.setProcessus(processus);
        // Valeur imposée par le système
        kpi.setUniteMesure("%");

        return toResponse(repository.save(kpi));
    }

    // ================= DELETE =================
    @Override
    public void delete(String code) {

        IndicateurPerformance kpi = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("KPI introuvable : " + code)
                );

        repository.delete(kpi);
    }

    // ================= RESPONSE =================
    private IndicateurPerformanceResponse toResponse(IndicateurPerformance kpi) {

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
                kpi.getProcessus() != null ? kpi.getProcessus().getCode()    : null,
                kpi.getProcessus() != null ? kpi.getProcessus().getLibelle() : null,
                kpi.getEcartCible(),
                kpi.getStatut()
        );
    }
}
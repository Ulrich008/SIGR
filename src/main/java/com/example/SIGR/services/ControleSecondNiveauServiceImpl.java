package com.example.SIGR.services;

import com.example.SIGR.dto.request.ControleSecondNiveauRequest;
import com.example.SIGR.dto.response.ConstatsRecommandationsResponse;
import com.example.SIGR.dto.response.ControleSecondNiveauResponse;
import com.example.SIGR.dto.response.LigneControleResponse;
import com.example.SIGR.entity.ControleSecondNiveau;
import com.example.SIGR.entity.Processus;
import com.example.SIGR.entity.UniteAdministrative;
import com.example.SIGR.repository.ControleSecondNiveauRepository;
import com.example.SIGR.repository.ProcessusRepository;
import com.example.SIGR.repository.UniteAdministrativeRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ControleSecondNiveauServiceImpl implements ControleSecondNiveauService {

    private final ControleSecondNiveauRepository repository;
    private final UniteAdministrativeRepository uniteRepository;
    private final ProcessusRepository processusRepository;

    public ControleSecondNiveauServiceImpl(
            ControleSecondNiveauRepository repository,
            UniteAdministrativeRepository uniteRepository,
            ProcessusRepository processusRepository
    ) {
        this.repository = repository;
        this.uniteRepository = uniteRepository;
        this.processusRepository = processusRepository;
    }

    // ================= CREATE =================
    @Override
    public ControleSecondNiveauResponse create(ControleSecondNiveauRequest request) {

        UniteAdministrative unite = uniteRepository.findByCode(request.getCodeUniteAdministrative())
                .orElseThrow(() -> new RuntimeException(
                        "Unité administrative introuvable : " + request.getCodeUniteAdministrative()
                ));

        Processus processus = processusRepository.findByCode(request.getCodeProcessus())
                .orElseThrow(() -> new RuntimeException(
                        "Processus introuvable : " + request.getCodeProcessus()
                ));

        ControleSecondNiveau controle = new ControleSecondNiveau();
        controle.setCode(generateCode(unite.getCode()));
        controle.setUniteAdministrative(unite);
        controle.setProcessus(processus);

        mapRequestToEntity(controle, request);

        return toResponse(repository.save(controle));
    }

    // ================= GET BY CODE =================
    @Override
    public ControleSecondNiveauResponse getByCode(String code) {
        return toResponse(findOrThrow(code));
    }

    // ================= GET ALL =================
    @Override
    public List<ControleSecondNiveauResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @Override
    public ControleSecondNiveauResponse update(String code, ControleSecondNiveauRequest request) {

        ControleSecondNiveau controle = findOrThrow(code);

        UniteAdministrative unite = uniteRepository.findByCode(request.getCodeUniteAdministrative())
                .orElseThrow(() -> new RuntimeException(
                        "Unité administrative introuvable : " + request.getCodeUniteAdministrative()
                ));

        Processus processus = processusRepository.findByCode(request.getCodeProcessus())
                .orElseThrow(() -> new RuntimeException(
                        "Processus introuvable : " + request.getCodeProcessus()
                ));

        controle.setUniteAdministrative(unite);
        controle.setProcessus(processus);

        mapRequestToEntity(controle, request);

        return toResponse(repository.save(controle));
    }

    // ================= DELETE =================
    @Override
    public void delete(String code) {
        repository.delete(findOrThrow(code));
    }

    // ================= CONSTATS / RECOMMANDATIONS AGRÉGÉS =================
    /**
     * Reprend tous les ControleSecondNiveau jamais saisis pour ce couple
     * UA+Processus (pas de notion de "déjà inclus dans un rapport
     * précédent") et en extrait tous les champs constat/recommandation non
     * vides — alimente l'étape 2 (lecture seule) du formulaire Rapport de
     * contrôle interne, ainsi que le PDF du rapport.
     */
    @Override
    public ConstatsRecommandationsResponse getConstatsEtRecommandations(String codeUnite, String codeProcessus) {

        List<ControleSecondNiveau> controles =
                repository.findByUniteAdministrative_CodeAndProcessus_Code(codeUnite, codeProcessus);

        List<String> constats = new ArrayList<>();
        List<String> recommandations = new ArrayList<>();

        for (ControleSecondNiveau c : controles) {
            Stream.of(c.getTestsConstats(), c.getRevuesConstats(), c.getVerificationConstats(),
                            c.getEvolutionResultatsConformite(), c.getAnomalieConstat(), c.getFaiblesseConstat())
                    .filter(s -> s != null && !s.isBlank())
                    .forEach(constats::add);

            Stream.of(c.getTestsRecommandation(), c.getRevuesRecommandation(), c.getVerificationRecommandation(),
                            c.getEvolutionRecommandation(), c.getAnomalieRecommandation(), c.getFaiblesseRecommandation())
                    .filter(s -> s != null && !s.isBlank())
                    .forEach(recommandations::add);
        }

        return new ConstatsRecommandationsResponse(constats, recommandations);
    }

    // ================= LIGNES DÉTAILLÉES (constat + analyse + recommandation reliés) =================
    /**
     * Même portée que getConstatsEtRecommandations (tous les ControleSecondNiveau
     * du couple UA+Processus), mais sans éclater constat/analyse/recommandation
     * dans des listes plates séparées : chaque ligne (Tests, Revues,
     * Vérification, Évolution de conformité, Anomalie, Faiblesse) reste un bloc
     * unique rattaché à son contrôle d'origine, pour affichage groupé côté
     * formulaire Rapport (Préambule, Analyses/Recommandations) et PDF.
     */
    @Override
    public List<LigneControleResponse> getLignesDetaillees(String codeUnite, String codeProcessus) {

        List<ControleSecondNiveau> controles =
                repository.findByUniteAdministrative_CodeAndProcessus_Code(codeUnite, codeProcessus);

        List<LigneControleResponse> lignes = new ArrayList<>();

        for (ControleSecondNiveau c : controles) {
            ajouterLigneSiNonVide(lignes, c, "Tests", c.getTestsLibelle(), c.getTestsConstats(), c.getTestsAnalyse(), c.getTestsRecommandation());
            ajouterLigneSiNonVide(lignes, c, "Revues", c.getRevuesLibelle(), c.getRevuesConstats(), c.getRevuesAnalyse(), c.getRevuesRecommandation());
            ajouterLigneSiNonVide(lignes, c, "Vérification", c.getVerificationLibelleDesPieces(), c.getVerificationConstats(), c.getVerificationAnalyse(), c.getVerificationRecommandation());
            ajouterLigneSiNonVide(lignes, c, "Évolution de conformité", c.getEvolutionIntituleOperation(), c.getEvolutionResultatsConformite(), c.getEvolutionAnalyse(), c.getEvolutionRecommandation());
            ajouterLigneSiNonVide(lignes, c, "Anomalie", null, c.getAnomalieConstat(), c.getAnomalieAnalyse(), c.getAnomalieRecommandation());
            ajouterLigneSiNonVide(lignes, c, "Faiblesse", null, c.getFaiblesseConstat(), c.getFaiblesseAnalyse(), c.getFaiblesseRecommandation());
        }

        return lignes;
    }

    private void ajouterLigneSiNonVide(
            List<LigneControleResponse> lignes, ControleSecondNiveau c, String categorie,
            String libelle, String constat, String analyse, String recommandation
    ) {
        boolean vide = (constat == null || constat.isBlank())
                && (analyse == null || analyse.isBlank())
                && (recommandation == null || recommandation.isBlank());
        if (vide) {
            return;
        }
        lignes.add(new LigneControleResponse(
                c.getCode(), c.getDateControle(), categorie, libelle, constat, analyse, recommandation
        ));
    }

    // ================= HELPERS =================

    private ControleSecondNiveau findOrThrow(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Contrôle de second niveau introuvable : " + code));
    }

    private void mapRequestToEntity(ControleSecondNiveau controle, ControleSecondNiveauRequest request) {
        controle.setDateControle(request.getDateControle());

        controle.setTestsLibelle(request.getTestsLibelle());
        controle.setTestsConstats(request.getTestsConstats());
        controle.setTestsAnalyse(request.getTestsAnalyse());
        controle.setTestsRecommandation(request.getTestsRecommandation());

        controle.setRevuesLibelle(request.getRevuesLibelle());
        controle.setRevuesConstats(request.getRevuesConstats());
        controle.setRevuesAnalyse(request.getRevuesAnalyse());
        controle.setRevuesRecommandation(request.getRevuesRecommandation());

        controle.setVerificationLibelleDesPieces(request.getVerificationLibelleDesPieces());
        controle.setVerificationConstats(request.getVerificationConstats());
        controle.setVerificationAnalyse(request.getVerificationAnalyse());
        controle.setVerificationRecommandation(request.getVerificationRecommandation());

        controle.setEvolutionIntituleOperation(request.getEvolutionIntituleOperation());
        controle.setEvolutionProceduresInternesRenforcements(request.getEvolutionProceduresInternesRenforcements());
        controle.setEvolutionResultatsConformite(request.getEvolutionResultatsConformite());
        controle.setEvolutionAnalyse(request.getEvolutionAnalyse());
        controle.setEvolutionRecommandation(request.getEvolutionRecommandation());

        controle.setAnomalieConstat(request.getAnomalieConstat());
        controle.setAnomalieAnalyse(request.getAnomalieAnalyse());
        controle.setAnomalieRecommandation(request.getAnomalieRecommandation());

        controle.setFaiblesseConstat(request.getFaiblesseConstat());
        controle.setFaiblesseAnalyse(request.getFaiblesseAnalyse());
        controle.setFaiblesseRecommandation(request.getFaiblesseRecommandation());
    }

    private String generateCode(String sigleUnite) {
        long compteur = repository.countByUniteAdministrative_Code(sigleUnite) + 1;

        String code = "CSN_" + sigleUnite + String.format("%03d", compteur);
        while (repository.existsByCode(code)) {
            compteur++;
            code = "CSN_" + sigleUnite + String.format("%03d", compteur);
        }
        return code;
    }

    private ControleSecondNiveauResponse toResponse(ControleSecondNiveau c) {
        return new ControleSecondNiveauResponse(
                c.getId(),
                c.getCode(),
                c.getUniteAdministrative() != null ? c.getUniteAdministrative().getCode() : null,
                c.getUniteAdministrative() != null ? c.getUniteAdministrative().getLibelle() : null,
                c.getProcessus() != null ? c.getProcessus().getCode() : null,
                c.getProcessus() != null ? c.getProcessus().getLibelle() : null,
                c.getDateControle(),
                c.getTestsLibelle(),
                c.getTestsConstats(),
                c.getTestsAnalyse(),
                c.getTestsRecommandation(),
                c.getRevuesLibelle(),
                c.getRevuesConstats(),
                c.getRevuesAnalyse(),
                c.getRevuesRecommandation(),
                c.getVerificationLibelleDesPieces(),
                c.getVerificationConstats(),
                c.getVerificationAnalyse(),
                c.getVerificationRecommandation(),
                c.getEvolutionIntituleOperation(),
                c.getEvolutionProceduresInternesRenforcements(),
                c.getEvolutionResultatsConformite(),
                c.getEvolutionAnalyse(),
                c.getEvolutionRecommandation(),
                c.getAnomalieConstat(),
                c.getAnomalieAnalyse(),
                c.getAnomalieRecommandation(),
                c.getFaiblesseConstat(),
                c.getFaiblesseAnalyse(),
                c.getFaiblesseRecommandation(),
                c.getCreatedBy()
        );
    }
}

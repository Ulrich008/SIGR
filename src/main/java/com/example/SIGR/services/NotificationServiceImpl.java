package com.example.SIGR.services;

import com.example.SIGR.dto.response.NotificationResponse;
import com.example.SIGR.entity.*;
import com.example.SIGR.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Détection des situations à notifier + persistance d'une notification
 * par destinataire concerné (ciblage par rôle métier/étape du circuit de
 * validation). Reprend les règles de l'ancien AlerteServiceImpl (calcul
 * à la volée, sans destinataire ni état lu/non-lu) en y ajoutant :
 *  - un destinataire précis par notification (au lieu d'une liste plate
 *    visible de tous),
 *  - une déduplication (pas de nouvelle ligne tant que la précédente
 *    identique n'est pas lue),
 *  - une nouvelle règle "risque en attente de validation" ciblant le
 *    profil (PILOTE/CCI/CMMR) correspondant à l'étape courante.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final RisqueRepository risqueRepository;
    private final PlanMitigationRepository planMitigationRepository;
    private final ActionRepository actionRepository;
    private final IndicateurPerformanceRepository indicateurPerformanceRepository;
    private final AgentRepository agentRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public NotificationServiceImpl(
            RisqueRepository risqueRepository,
            PlanMitigationRepository planMitigationRepository,
            ActionRepository actionRepository,
            IndicateurPerformanceRepository indicateurPerformanceRepository,
            AgentRepository agentRepository,
            NotificationRepository notificationRepository,
            EmailService emailService
    ) {
        this.risqueRepository = risqueRepository;
        this.planMitigationRepository = planMitigationRepository;
        this.actionRepository = actionRepository;
        this.indicateurPerformanceRepository = indicateurPerformanceRepository;
        this.agentRepository = agentRepository;
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    @Override
    public List<NotificationResponse> genererNotifications() {
        List<Notification> creees = new ArrayList<>();

        for (Risque risque : risqueRepository.findAll()) {
            if (risque.getStatut() != StatutRisque.ACTIF && risque.getStatut() != StatutRisque.EN_COURS) {
                continue;
            }
            detecterMitigationManquante(risque, creees);
        }

        for (Risque risque : risqueRepository.findAll()) {
            detecterValidationEnAttente(risque, creees);
        }

        for (IndicateurPerformance indicateur : indicateurPerformanceRepository.findAll()) {
            if (indicateur.getSeuilAlerte() != null) {
                detecterEcheanceIndicateur(indicateur, creees);
            }
        }

        // Notification immédiate par email des seules nouveautés
        // CRITIQUE/HAUTE — les MOYENNE/FAIBLE restent in-app uniquement
        // pour éviter de noyer les destinataires sous les emails.
        for (Notification n : creees) {
            if (n.getSeverite() == SeveriteAlerte.CRITIQUE || n.getSeverite() == SeveriteAlerte.HAUTE) {
                emailService.envoyer(
                        n.getDestinataire().getEmail(),
                        "[SIGR] " + n.getTitre(),
                        n.getDescription()
                );
            }
        }

        return creees.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ================= RÈGLES DE DÉTECTION =================

    private void detecterMitigationManquante(Risque risque, List<Notification> creees) {
        List<PlanMitigation> plans = planMitigationRepository.findByRisque(risque);

        if (plans.isEmpty()) {
            creerPourProfils(
                    TypeNotification.RISQUE_SANS_MITIGATION,
                    "Risque sans plan de mitigation",
                    "Le risque '" + risque.getLibelle() + "' n'a aucun plan de mitigation défini.",
                    risque.getCode(),
                    risque.getLibelle(),
                    SeveriteAlerte.CRITIQUE,
                    risque,
                    List.of("RESPONSABLE_RISQUES", "RESPONSABLE_ACTION"),
                    creees
            );
            return;
        }

        boolean hasActionEnCours = plans.stream()
                .flatMap(plan -> actionRepository.findByPlanMitigation(plan).stream())
                .anyMatch(action -> action.getStatut() == StatutAction.EN_COURS);

        if (!hasActionEnCours) {
            creerPourProfils(
                    TypeNotification.RISQUE_SANS_ACTIONS_EN_COURS,
                    "Risque sans actions en cours",
                    "Le risque '" + risque.getLibelle() + "' a des plans de mitigation mais aucune action en cours.",
                    risque.getCode(),
                    risque.getLibelle(),
                    SeveriteAlerte.HAUTE,
                    risque,
                    List.of("RESPONSABLE_ACTION"),
                    creees
            );
        }
    }

    private void detecterValidationEnAttente(Risque risque, List<Notification> creees) {
        if (!Boolean.TRUE.equals(risque.getTransmis())) return;

        EtapeValidation etape = risque.getEtapeValidation();
        if (etape != EtapeValidation.PILOTE && etape != EtapeValidation.CCI && etape != EtapeValidation.CMMR) {
            return;
        }

        creerPourProfils(
                TypeNotification.RISQUE_EN_ATTENTE_VALIDATION,
                "Risque en attente de votre validation",
                "Le risque '" + risque.getLibelle() + "' attend une décision (Valider / Différer / Rejeter) à l'étape " + etape + ".",
                risque.getCode(),
                risque.getLibelle(),
                SeveriteAlerte.MOYENNE,
                risque,
                List.of(etape.name()),
                creees
        );
    }

    private void detecterEcheanceIndicateur(IndicateurPerformance indicateur, List<Notification> creees) {
        LocalDate aujourdHui = LocalDate.now();
        // Même source que IndicateurPerformance.getStatut() (affiché et
        // compté côté liste des indicateurs) : la date de fin propre à
        // l'indicateur, indépendamment d'une action ou d'un plan de
        // mitigation lié. Utiliser la date de fin d'une action liée ici
        // désynchronisait la notification du statut réellement affiché,
        // et privait d'alerte tout indicateur sans action rattachée.
        LocalDate dateFin = indicateur.getDateFin();
        if (dateFin == null) return;

        long joursRestants = ChronoUnit.DAYS.between(aujourdHui, dateFin);
        Processus processus = indicateur.getProcessus();

        if (aujourdHui.isAfter(indicateur.getSeuilAlerte())) {
            creerPourProfils(
                    TypeNotification.INDICATEUR_SEUIL_DEPASSE,
                    "Seuil d'alerte dépassé",
                    "L'indicateur '" + indicateur.getLibelle() + "' a dépassé son seuil d'alerte. " +
                            "Date de fin : " + dateFin + " (" + joursRestants + " jours).",
                    indicateur.getCode(),
                    indicateur.getLibelle(),
                    SeveriteAlerte.HAUTE,
                    processus,
                    List.of("RESPONSABLE_ACTION"),
                    creees
            );
        } else if (joursRestants <= 7) {
            creerPourProfils(
                    TypeNotification.INDICATEUR_ECHEANCE_PROCHE,
                    "Échéance proche",
                    "L'indicateur '" + indicateur.getLibelle() + "' approche de sa date de fin. " +
                            "Date de fin : " + dateFin + " (" + joursRestants + " jours restants).",
                    indicateur.getCode(),
                    indicateur.getLibelle(),
                    SeveriteAlerte.MOYENNE,
                    processus,
                    List.of("RESPONSABLE_ACTION"),
                    creees
            );
        }
    }

    // ================= CIBLAGE / PERSISTANCE =================

    private void creerPourProfils(
            TypeNotification type,
            String titre,
            String description,
            String codeElement,
            String libelleElement,
            SeveriteAlerte severite,
            Risque risque,
            List<String> codesProfils,
            List<Notification> creees
    ) {
        creerPourProfils(type, titre, description, codeElement, libelleElement, severite,
                risque.getProcessus(), codesProfils, creees);
    }

    private void creerPourProfils(
            TypeNotification type,
            String titre,
            String description,
            String codeElement,
            String libelleElement,
            SeveriteAlerte severite,
            Processus processus,
            List<String> codesProfils,
            List<Notification> creees
    ) {
        String codeMinistere = resoudreCodeMinistere(processus);
        if (codeMinistere == null) return;

        Set<Agent> destinataires = new LinkedHashSet<>();
        for (String codeProfil : codesProfils) {
            destinataires.addAll(agentRepository.findByMinistere_CodeAndProfil_CodeAndEnabledTrue(codeMinistere, codeProfil));
        }

        for (Agent destinataire : destinataires) {
            boolean dejaEnAttente = notificationRepository
                    .existsByDestinataire_MatriculeAndTypeAndCodeElementAndLuFalse(
                            destinataire.getMatricule(), type, codeElement
                    );
            if (dejaEnAttente) continue;

            Notification notification = new Notification()
                    .setType(type)
                    .setTitre(titre)
                    .setDescription(description)
                    .setCodeElement(codeElement)
                    .setLibelleElement(libelleElement)
                    .setCodeProcessus(processus != null ? processus.getCode() : null)
                    .setLibelleProcessus(processus != null ? processus.getLibelle() : null)
                    .setSeverite(severite)
                    .setDateAlerte(LocalDateTime.now())
                    .setDestinataire(destinataire)
                    .setLu(false);

            creees.add(notificationRepository.save(notification));
        }
    }

    private String resoudreCodeMinistere(Processus processus) {
        if (processus == null || processus.getUnite() == null || processus.getUnite().getMinistere() == null) {
            return null;
        }
        return processus.getUnite().getMinistere().getCode();
    }

    // ================= LECTURE =================

    @Override
    public List<NotificationResponse> listerPourAgent(String matricule) {
        return notificationRepository.findByDestinataire_MatriculeOrderByDateAlerteDesc(matricule).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long compterNonLuesPourAgent(String matricule) {
        return notificationRepository.countByDestinataire_MatriculeAndLuFalse(matricule);
    }

    @Override
    public NotificationResponse obtenirParId(String id, String matricule) {
        Notification notification = notificationRepository.findByIdAndDestinataire_Matricule(id, matricule)
                .orElseThrow(() -> new RuntimeException("Notification introuvable : " + id));
        return toResponse(notification);
    }

    @Override
    public NotificationResponse marquerCommeLue(String id, String matricule) {
        Notification notification = notificationRepository.findByIdAndDestinataire_Matricule(id, matricule)
                .orElseThrow(() -> new RuntimeException("Notification introuvable : " + id));

        if (!Boolean.TRUE.equals(notification.getLu())) {
            notification.setLu(true);
            notification.setDateLecture(LocalDateTime.now());
            notification = notificationRepository.save(notification);
        }
        return toResponse(notification);
    }

    @Override
    public void marquerToutesCommeLues(String matricule) {
        List<Notification> nonLues = notificationRepository.findByDestinataire_MatriculeAndLuFalse(matricule);
        LocalDateTime maintenant = LocalDateTime.now();
        for (Notification n : nonLues) {
            n.setLu(true);
            n.setDateLecture(maintenant);
        }
        notificationRepository.saveAll(nonLues);
    }

    // ================= RESPONSE =================

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getType(),
                n.getTitre(),
                n.getDescription(),
                n.getCodeElement(),
                n.getLibelleElement(),
                n.getCodeProcessus(),
                n.getLibelleProcessus(),
                n.getSeverite(),
                n.getDateAlerte(),
                n.getLu(),
                n.getDateLecture()
        );
    }
}

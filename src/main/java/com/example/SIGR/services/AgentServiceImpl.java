package com.example.SIGR.services;

import com.example.SIGR.dto.request.AgentRequest;
import com.example.SIGR.dto.response.AgentResponse;
import com.example.SIGR.entity.Agent;
import com.example.SIGR.entity.Ministere;
import com.example.SIGR.entity.Profil;
import com.example.SIGR.entity.Role;
import com.example.SIGR.entity.UniteAdministrative;
import com.example.SIGR.config.MinistereInterceptor;
import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.MinistereRepository;
import com.example.SIGR.repository.ProfilRepository;
import com.example.SIGR.repository.UniteAdministrativeRepository;
import com.example.SIGR.security.SecurityUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Rectangle;
import java.awt.Color;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final UniteAdministrativeRepository uniteRepository;
    private final ProfilRepository profilRepository;
    private final MinistereRepository ministereRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    public AgentServiceImpl(
            AgentRepository agentRepository,
            UniteAdministrativeRepository uniteRepository,
            ProfilRepository profilRepository,
            MinistereRepository ministereRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.agentRepository = agentRepository;
        this.uniteRepository = uniteRepository;
        this.profilRepository = profilRepository;
        this.ministereRepository = ministereRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AgentResponse create(AgentRequest request) {

        // ================= PASSWORD =================

        if (request.getPassword() == null
                || request.getPassword().isBlank()) {

            throw new RuntimeException(
                    "Le mot de passe est obligatoire"
            );
        }

        // ================= NPI =================

        if (request.getNpi() != null
                && !request.getNpi().isBlank()
                && agentRepository.existsByNpi(request.getNpi())) {

            throw new RuntimeException(
                    "Un agent avec ce NPI existe déjà : "
                            + request.getNpi()
            );
        }

        // ================= RÔLE =================

        verifierAttributionRole(request.getRole());

        // ================= UNITE =================
        // Obligatoire uniquement pour un AGENT (profil métier travaillant
        // dans une unité précise). Un ADMIN gère l'ensemble de son
        // ministère (il crée lui-même ses UA après coup) et un SUPER_ADMIN
        // a un accès global : ni l'un ni l'autre n'est rattaché de force
        // à une unité dès sa création.

        boolean uniteFournie = request.getCodeUnite() != null && !request.getCodeUnite().isBlank();

        if (request.getRole() == Role.AGENT && !uniteFournie) {
            throw new RuntimeException("Le code de l'unité est obligatoire");
        }

        UniteAdministrative unite = null;
        if (uniteFournie) {
            unite = uniteRepository
                    .findByCode(request.getCodeUnite())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Unité introuvable : "
                                            + request.getCodeUnite()
                            )
                    );
        }

        // ================= PROFIL =================

        Profil profil = resoudreProfil(request.getRole(), request.getCodeProfil());

        // ================= MINISTERE =================

        String codeMinistereCible = resoudreCodeMinistereCible(
                request.getCodeMinistere()
        );

        Ministere ministere = null;
        if (codeMinistereCible != null) {
            ministere = ministereRepository
                    .findByCode(codeMinistereCible)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Ministère introuvable : "
                                            + codeMinistereCible
                            )
                    );
        }

        // ================= GENERATION MATRICULE =================

        String matricule = genererMatricule(profil, unite, ministere);

        // ================= CREATION =================

        Agent agent = new Agent();

        agent.setMatricule(matricule);

        agent.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        agent.setEnabled(true);

        agent.setNpi(request.getNpi());
        agent.setNom(request.getNom());
        agent.setPrenoms(request.getPrenoms());
        agent.setSexe(request.getSexe());
        agent.setRole(request.getRole());
        agent.setDateNaissance(request.getDateNaissance());
        agent.setDatePriseService(request.getDatePriseService());
        agent.setEmail(request.getEmail());

        agent.setUnite(unite);

        // ================= PROFIL =================

        agent.setProfil(profil);

        // ================= MINISTERE =================

        agent.setMinistere(ministere);

        Agent saved = agentRepository.save(agent);

        return toResponse(saved);
    }

    @Override
    public AgentResponse getByMatricule(String matricule) {

        Agent agent = agentRepository
                .findByMatricule(matricule)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent introuvable : " + matricule
                        )
                );

        return toResponse(agent);
    }

    /**
     * Un agent doit toujours pouvoir se retrouver lui-même : cette
     * méthode désactive temporairement le filtre par ministère (qui
     * pourrait sinon, selon le contexte, l'exclure de sa propre
     * recherche) le temps de la requête, puis le restaure à
     * l'identique pour ne pas affecter la suite du traitement.
     */
    @Override
    public AgentResponse getMe(String matricule) {

        Session session = entityManager.unwrap(Session.class);
        boolean filtreActif = session.getEnabledFilter("ministereFilter") != null;

        if (filtreActif) {
            session.disableFilter("ministereFilter");
        }

        try {
            Agent agent = agentRepository
                    .findByMatricule(matricule)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Agent introuvable : " + matricule
                            )
                    );

            return toResponse(agent);

        } finally {
            if (filtreActif) {
                String codeMinistere = SecurityUtils.getCurrentMinistereCode();
                session.enableFilter("ministereFilter")
                        .setParameter(
                                "codeMinistere",
                                codeMinistere != null
                                        ? codeMinistere
                                        : MinistereInterceptor.AUCUN_MINISTERE
                        );
            }
        }
    }

    @Override
    public List<AgentResponse> getAll() {

        List<Agent> agents = agentRepository.findAll();

        // Un AGENT (profil métier) ne doit voir que les agents de sa propre
        // unité administrative, pas tout son ministère (déjà restreint par
        // le filtre Hibernate pour ADMIN/SUPER_ADMIN, mais un AGENT a besoin
        // d'un périmètre plus étroit — typiquement pour choisir un
        // responsable dans un formulaire sans exposer tout le ministère).
        if (SecurityUtils.hasAuthority("AGENT")) {

            String matricule = SecurityUtils.getCurrentUser();
            String codeUniteCourant = matricule != null
                    ? agentRepository.findCodeUniteByMatricule(matricule)
                    : null;

            agents = agents.stream()
                    .filter(a -> codeUniteCourant != null
                            && a.getUnite() != null
                            && codeUniteCourant.equals(a.getUnite().getCode()))
                    .collect(Collectors.toList());
        }

        return agents.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= EXPORT PDF =================

    private static final Color VERT_ENTETE   = new Color(4, 120, 87);   // emerald-700
    private static final Color VERT_CLAIR    = new Color(209, 250, 229); // emerald-100
    private static final Color GRIS_ZEBRA    = new Color(248, 250, 252); // slate-50
    private static final Color GRIS_TEXTE    = new Color(71, 85, 105);   // slate-600
    private static final Color ROUGE_INACTIF = new Color(190, 18, 60);   // rose-700

    @Override
    public byte[] generateAgentsPdf(String codeMinistereDemande) {

        boolean superAdmin = SecurityUtils.hasAuthority("SUPER_ADMIN");

        String codeMinistereEffectif;
        List<Agent> agents;

        if (superAdmin) {
            if (codeMinistereDemande == null || codeMinistereDemande.isBlank()) {
                throw new RuntimeException(
                        "Le code du ministère est obligatoire pour un super-administrateur"
                );
            }
            codeMinistereEffectif = codeMinistereDemande;

            // Le SUPER_ADMIN n'est pas soumis au filtre par ministère :
            // agentRepository.findAll() renvoie tous les agents, il faut
            // donc filtrer nous-mêmes sur le ministère demandé.
            agents = agentRepository.findAll().stream()
                    .filter(a -> a.getMinistere() != null
                            && codeMinistereEffectif.equals(a.getMinistere().getCode()))
                    .collect(Collectors.toList());
        } else {
            // ADMIN : déjà cantonné à son propre ministère par le filtre
            // Hibernate, peu importe ce qui est demandé en paramètre.
            codeMinistereEffectif = SecurityUtils.getCurrentMinistereCode();
            agents = agentRepository.findAll();
        }

        Ministere ministere = ministereRepository.findByCode(codeMinistereEffectif)
                .orElseThrow(() -> new RuntimeException(
                        "Ministère introuvable : " + codeMinistereEffectif
                ));

        agents.sort(Comparator.comparing(
                a -> a.getNom() != null ? a.getNom() : "",
                String.CASE_INSENSITIVE_ORDER
        ));

        return construirePdfAgents(ministere, agents);
    }

    private byte[] construirePdfAgents(Ministere ministere, List<Agent> agents) {

        Document document = new Document(PageSize.A4.rotate(), 30, 30, 90, 50);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setPageEvent(new EnteteEtPiedDePage(ministere));

            document.open();

            Font titreFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.WHITE);
            Font sousTitreFont = FontFactory.getFont(FontFactory.HELVETICA, 11, VERT_CLAIR);
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 9, GRIS_TEXTE);

            Paragraph espace = new Paragraph(" ");
            espace.setSpacingAfter(4f);
            document.add(espace);

            Paragraph infos = new Paragraph();
            infos.add(new Chunk(
                    "Ministère : " + ministere.getNom() + " (" + ministere.getCode() + ")\n",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, GRIS_TEXTE)
            ));
            infos.add(new Chunk(
                    agents.size() + " agent(s) — généré le "
                            + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    infoFont
            ));
            infos.setSpacingAfter(14f);
            document.add(infos);

            if (agents.isEmpty()) {
                Paragraph aucunAgent = new Paragraph(
                        "Aucun agent n'a encore été créé pour ce ministère.",
                        FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 11, GRIS_TEXTE)
                );
                document.add(aucunAgent);
            } else {
                document.add(construireTableauAgents(agents));
            }

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF des agents", e);
        }
    }

    private PdfPTable construireTableauAgents(List<Agent> agents) throws Exception {

        String[] entetes = {
                "Matricule", "Nom", "Prénoms", "Sexe", "Rôle",
                "Profil", "Unité", "Statut", "Prise de service"
        };
        float[] largeurs = { 10f, 14f, 16f, 7f, 10f, 14f, 15f, 8f, 12f };

        PdfPTable table = new PdfPTable(entetes.length);
        table.setWidthPercentage(100);
        table.setWidths(largeurs);
        table.setHeaderRows(1);

        Font enteteFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
        for (String entete : entetes) {
            PdfPCell cell = new PdfPCell(new Phrase(entete, enteteFont));
            cell.setBackgroundColor(VERT_ENTETE);
            cell.setPadding(6f);
            cell.setBorderColor(VERT_ENTETE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
        }

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 8.5f, Color.DARK_GRAY);
        Font actifFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, VERT_ENTETE);
        Font inactifFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, ROUGE_INACTIF);

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean zebre = false;

        for (Agent agent : agents) {

            Color fond = zebre ? GRIS_ZEBRA : Color.WHITE;
            zebre = !zebre;

            ajouterCellule(table, agent.getMatricule(), cellFont, fond);
            ajouterCellule(table, agent.getNom(), cellFont, fond);
            ajouterCellule(table, agent.getPrenoms(), cellFont, fond);
            ajouterCellule(table, agent.getSexe() != null ? agent.getSexe().name() : "-", cellFont, fond);
            ajouterCellule(table, agent.getRole() != null ? agent.getRole().name() : "-", cellFont, fond);
            ajouterCellule(table, agent.getProfil() != null ? agent.getProfil().getLibelle() : "-", cellFont, fond);
            ajouterCellule(table, agent.getUnite() != null ? agent.getUnite().getLibelle() : "-", cellFont, fond);

            boolean actif = Boolean.TRUE.equals(agent.getEnabled());
            PdfPCell statutCell = new PdfPCell(new Phrase(
                    actif ? "Actif" : "Inactif",
                    actif ? actifFont : inactifFont
            ));
            statutCell.setBackgroundColor(fond);
            statutCell.setPadding(5f);
            statutCell.setBorderColor(Color.LIGHT_GRAY);
            table.addCell(statutCell);

            String datePriseService = agent.getDatePriseService() != null
                    ? agent.getDatePriseService().format(dateFormat)
                    : "-";
            ajouterCellule(table, datePriseService, cellFont, fond);
        }

        return table;
    }

    private void ajouterCellule(PdfPTable table, String valeur, Font font, Color fond) {
        PdfPCell cell = new PdfPCell(new Phrase(valeur != null ? valeur : "-", font));
        cell.setBackgroundColor(fond);
        cell.setPadding(5f);
        cell.setBorderColor(Color.LIGHT_GRAY);
        table.addCell(cell);
    }

    /**
     * Bandeau vert en en-tête de chaque page + pied de page avec
     * pagination, pour un rendu plus soigné qu'un simple tableau brut.
     */
    private static class EnteteEtPiedDePage extends PdfPageEventHelper {

        private final Ministere ministere;

        EnteteEtPiedDePage(Ministere ministere) {
            this.ministere = ministere;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            com.lowagie.text.pdf.PdfContentByte canvas = writer.getDirectContent();

            // Bandeau d'en-tête
            Rectangle pageSize = document.getPageSize();
            canvas.saveState();
            canvas.setColorFill(VERT_ENTETE);
            canvas.rectangle(0, pageSize.getHeight() - 60, pageSize.getWidth(), 60);
            canvas.fill();
            canvas.restoreState();

            com.lowagie.text.pdf.ColumnText.showTextAligned(
                    canvas,
                    Element.ALIGN_LEFT,
                    new Phrase("Liste des agents — " + ministere.getNom(),
                            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.WHITE)),
                    36, pageSize.getHeight() - 38, 0
            );

            com.lowagie.text.pdf.ColumnText.showTextAligned(
                    canvas,
                    Element.ALIGN_RIGHT,
                    new Phrase("SIGR", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, VERT_CLAIR)),
                    pageSize.getWidth() - 36, pageSize.getHeight() - 38, 0
            );

            // Pied de page
            canvas.saveState();
            canvas.setLineWidth(0.5f);
            canvas.setColorStroke(Color.LIGHT_GRAY);
            canvas.moveTo(36, 40);
            canvas.lineTo(pageSize.getWidth() - 36, 40);
            canvas.stroke();
            canvas.restoreState();

            com.lowagie.text.pdf.ColumnText.showTextAligned(
                    canvas,
                    Element.ALIGN_LEFT,
                    new Phrase("Système Intégré de Gestion des Risques (SIGR)",
                            FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY)),
                    36, 26, 0
            );

            com.lowagie.text.pdf.ColumnText.showTextAligned(
                    canvas,
                    Element.ALIGN_RIGHT,
                    new Phrase("Page " + document.getPageNumber(),
                            FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY)),
                    pageSize.getWidth() - 36, 26, 0
            );
        }
    }

    @Override
    public AgentResponse update(String matricule, AgentRequest request) {

        Agent agent = agentRepository
                .findByMatricule(matricule)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent introuvable : " + matricule
                        )
                );

        // ================= NPI =================

        if (request.getNpi() != null
                && !request.getNpi().isBlank()) {

            boolean npiExiste = agentRepository
                    .existsByNpi(request.getNpi());

            if (npiExiste
                    && !request.getNpi().equals(agent.getNpi())) {

                throw new RuntimeException(
                        "Ce NPI est déjà utilisé : "
                                + request.getNpi()
                );
            }

            agent.setNpi(request.getNpi());
        }

        // ================= PASSWORD =================

        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            agent.setPassword(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }

        // ================= AUTRES CHAMPS =================

        if (request.getNom() != null
                && !request.getNom().isBlank()) {

            agent.setNom(request.getNom());
        }

        if (request.getPrenoms() != null
                && !request.getPrenoms().isBlank()) {

            agent.setPrenoms(request.getPrenoms());
        }

        if (request.getSexe() != null) {
            agent.setSexe(request.getSexe());
        }

        if (request.getRole() != null) {
            verifierAttributionRole(request.getRole());
            agent.setRole(request.getRole());
        }

        if (request.getDateNaissance() != null) {
            agent.setDateNaissance(
                    request.getDateNaissance()
            );
        }

        if (request.getDatePriseService() != null) {
            agent.setDatePriseService(
                    request.getDatePriseService()
            );
        }

        if (request.getEmail() != null) {
            // Chaîne vide autorisée : permet de retirer un email déjà saisi.
            agent.setEmail(request.getEmail().isBlank() ? null : request.getEmail());
        }

        // ================= UNITE =================

        if (request.getCodeUnite() != null
                && !request.getCodeUnite().isBlank()) {

            UniteAdministrative unite = uniteRepository
                    .findByCode(request.getCodeUnite())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Unité introuvable : "
                                            + request.getCodeUnite()
                            )
                    );

            agent.setUnite(unite);
        }

        // ================= PROFIL =================

        if (request.getCodeProfil() != null
                && !request.getCodeProfil().isBlank()) {

            Profil profil = profilRepository
                    .findByCode(request.getCodeProfil())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Profil introuvable : "
                                            + request.getCodeProfil()
                            )
                    );

            agent.setProfil(profil);

        } else if (agent.getRole() != Role.AGENT) {

            // Un ADMIN/SUPER_ADMIN n'a pas de profil métier : si le
            // formulaire n'en fournit aucun, on retire explicitement
            // l'ancien profil au lieu de le laisser traîner en base
            // (sinon un agent devenu ADMIN garderait silencieusement
            // son ancien profil, invisible et incohérent).
            agent.setProfil(null);
        }

        // Un AGENT doit obligatoirement avoir un profil métier
        // (ADMIN/SUPER_ADMIN n'en ont pas besoin, leur accès vient du rôle)
        if (agent.getRole() == Role.AGENT && agent.getProfil() == null) {
            throw new RuntimeException(
                    "Le profil métier est obligatoire pour un agent"
            );
        }

        // ================= MINISTERE =================

        if (request.getCodeMinistere() != null
                && !request.getCodeMinistere().isBlank()) {

            String codeMinistereCible = resoudreCodeMinistereCible(
                    request.getCodeMinistere()
            );

            Ministere ministere = ministereRepository
                    .findByCode(codeMinistereCible)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Ministère introuvable : "
                                            + codeMinistereCible
                            )
                    );

            agent.setMinistere(ministere);
        }

        Agent updated = agentRepository.save(agent);

        return toResponse(updated);
    }

    @Override
    public AgentResponse changeStatus(
            String matricule,
            Boolean enabled
    ) {

        Agent agent = agentRepository
                .findByMatricule(matricule)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent introuvable : "
                                        + matricule
                        )
                );

        agent.setEnabled(enabled);

        Agent updated = agentRepository.save(agent);

        return toResponse(updated);
    }

    @Override
    public void delete(String matricule) {

        Agent agent = agentRepository
                .findByMatricule(matricule)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Agent introuvable : "
                                        + matricule
                        )
                );

        agentRepository.delete(agent);
    }

    // ================= CONTRÔLE D'ATTRIBUTION DE RÔLE =================

    /**
     * Seul un SUPER_ADMIN peut attribuer le rôle SUPER_ADMIN à un agent.
     * Sans ce contrôle, un ADMIN pourrait s'auto-élever (ou élever un
     * tiers) au rôle le plus privilégié du système.
     */
    private void verifierAttributionRole(Role roleDemande) {

        if (roleDemande == Role.SUPER_ADMIN
                && !SecurityUtils.hasAuthority("SUPER_ADMIN")) {

            throw new AccessDeniedException(
                    "Seul un super-administrateur peut attribuer le rôle SUPER_ADMIN"
            );
        }
    }

    // ================= RÉSOLUTION DU PROFIL =================

    /**
     * Le profil métier n'est obligatoire que pour le rôle AGENT
     * (CMMR, CCI, PILOTE, RESPONSABLE_RISQUES, ...). Un ADMIN ou un
     * SUPER_ADMIN n'a pas de profil : son accès vient uniquement de
     * son rôle technique.
     */
    private Profil resoudreProfil(Role role, String codeProfil) {

        boolean profilFourni = codeProfil != null && !codeProfil.isBlank();

        if (role == Role.AGENT && !profilFourni) {
            throw new RuntimeException(
                    "Le profil métier est obligatoire pour un agent"
            );
        }

        if (!profilFourni) {
            return null;
        }

        return profilRepository
                .findByCode(codeProfil)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Profil introuvable : " + codeProfil
                        )
                );
    }

    /**
     * Préfixe de matricule par profil métier. Les profils absents de
     * cette table (Administrateur, Responsable d'action, ou un agent
     * sans profil) gardent l'ancien format générique AGT-XXX.
     */
    private static final java.util.Map<String, String> PREFIXES_MATRICULE_PAR_PROFIL = java.util.Map.of(
            "RESPONSABLE_RISQUES", "RR_",
            "PILOTE", "Pt_",
            "CCI", "CCI_",
            "CMMR", "CMMR_",
            "AUDITEUR", "Au_",
            "RESPONSABLE_ACTION", "RA_"
    );

    /**
     * Format : <préfixe profil><sigle UA>_<code ministère><séquence sur
     * 3 chiffres>, séquence propre à la combinaison profil + UA.
     * Retombe sur l'ancien format AGT-XXX si le profil n'a pas de
     * préfixe dédié, ou si l'UA/le ministère est manquant.
     */
    private String genererMatricule(Profil profil, UniteAdministrative unite, Ministere ministere) {

        String prefixe = profil != null ? PREFIXES_MATRICULE_PAR_PROFIL.get(profil.getCode()) : null;

        if (prefixe == null || ministere == null) {

            long total = agentRepository.count() + 1;
            String matricule = String.format("AGT-%03d", total);

            while (agentRepository.existsByMatricule(matricule)) {
                total++;
                matricule = String.format("AGT-%03d", total);
            }

            return matricule;
        }

        String sigleUnite = unite.getCode();
        String codeMinistere = ministere.getCode();

        long compteur = agentRepository.countByProfil_CodeAndUnite_Code(profil.getCode(), sigleUnite) + 1;
        String matricule = prefixe + sigleUnite + "_" + codeMinistere + String.format("%03d", compteur);

        while (agentRepository.existsByMatricule(matricule)) {
            compteur++;
            matricule = prefixe + sigleUnite + "_" + codeMinistere + String.format("%03d", compteur);
        }

        return matricule;
    }

    // ================= CONTRÔLE D'ACCÈS PAR MINISTÈRE =================

    /**
     * Un ADMIN ne peut créer/affecter un agent que dans son propre
     * ministère : le code demandé doit correspondre au sien (ou être
     * absent, auquel cas il est complété automatiquement). Le
     * SUPER_ADMIN peut affecter n'importe quel ministère.
     */
    private String resoudreCodeMinistereCible(String codeMinistereDemande) {

        if (SecurityUtils.hasAuthority("SUPER_ADMIN")) {
            return codeMinistereDemande;
        }

        String codeMinistereCourant = SecurityUtils.getCurrentMinistereCode();

        if (codeMinistereDemande != null
                && !codeMinistereDemande.equals(codeMinistereCourant)) {

            throw new AccessDeniedException(
                    "Vous ne pouvez gérer que les agents de votre propre ministère"
            );
        }

        return codeMinistereCourant;
    }

    // ================= RESPONSE =================

    private AgentResponse toResponse(Agent agent) {

       return new AgentResponse(
                agent.getId(),
                agent.getMatricule(),
                agent.getNpi(),
                agent.getNom(),
                agent.getPrenoms(),
                agent.getSexe(),
                agent.getRole(),
                agent.getProfil() != null
                        ? agent.getProfil().getCode()
                        : null,
                agent.getProfil() != null
                        ? agent.getProfil().getLibelle()
                        : null,
                agent.getEnabled(),
                agent.getDateNaissance(),
                agent.getDatePriseService(),
                agent.getUnite() != null
                        ? agent.getUnite().getCode()
                        : null,
                agent.getUnite() != null
                        ? agent.getUnite().getLibelle()
                        : null,
                agent.getMinistere() != null
                        ? agent.getMinistere().getCode()
                        : null,
                agent.getMinistere() != null
                        ? agent.getMinistere().getNom()
                        : null,
                agent.getMinistere() != null
                        ? agent.getMinistere().getSigle()
                        : null,
                agent.getEmail()
        );
    }
}
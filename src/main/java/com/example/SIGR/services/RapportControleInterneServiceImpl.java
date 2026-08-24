package com.example.SIGR.services;

import com.example.SIGR.dto.request.ActionCorrectiveRequest;
import com.example.SIGR.dto.request.AvisRapportCIRequest;
import com.example.SIGR.dto.request.RapportControleInterneRequest;
import com.example.SIGR.dto.request.DecisionSuiviRequest;
import com.example.SIGR.dto.request.StatutSuiviRequest;
import com.example.SIGR.dto.response.ActionCorrectiveResponse;
import com.example.SIGR.dto.response.AvisHistoriqueRapportCIResponse;
import com.example.SIGR.dto.response.LigneControleResponse;
import com.example.SIGR.dto.response.RapportControleInterneResponse;
import com.example.SIGR.entity.ActionCorrective;
import com.example.SIGR.entity.ControleSecondNiveau;
import com.example.SIGR.entity.Processus;
import com.example.SIGR.entity.RapportControleInterne;
import com.example.SIGR.entity.StatutRapportCI;
import com.example.SIGR.entity.UniteAdministrative;
import com.example.SIGR.repository.AgentRepository;
import com.example.SIGR.repository.ControleSecondNiveauRepository;
import com.example.SIGR.repository.ProcessusRepository;
import com.example.SIGR.repository.RapportControleInterneRepository;
import com.example.SIGR.repository.UniteAdministrativeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RapportControleInterneServiceImpl implements RapportControleInterneService {

    // Palette alignée sur les couleurs Tailwind déjà utilisées côté frontend
    // (badges de statut, boutons), pour que le PDF ait le même langage
    // visuel que le reste de l'application.
    private static final Color VERT_ENTETE = new Color(4, 120, 87);    // emerald-700
    private static final Color VERT_CLAIR  = new Color(209, 250, 229); // emerald-100
    private static final Color GRIS_ZEBRA  = new Color(248, 250, 252); // slate-50
    private static final Color GRIS_BORDURE = new Color(226, 232, 240); // slate-200
    private static final Color GRIS_TEXTE  = new Color(71, 85, 105);   // slate-600
    private static final Color GRIS_LABEL  = new Color(100, 116, 139); // slate-500
    private static final Color TEXTE_FONCE = new Color(15, 23, 42);    // slate-900

    private final RapportControleInterneRepository repository;
    private final ControleSecondNiveauRepository controleRepository;
    private final ControleSecondNiveauService controleService;
    private final UniteAdministrativeRepository uniteRepository;
    private final ProcessusRepository processusRepository;
    private final AgentRepository agentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public RapportControleInterneServiceImpl(
            RapportControleInterneRepository repository,
            ControleSecondNiveauRepository controleRepository,
            ControleSecondNiveauService controleService,
            UniteAdministrativeRepository uniteRepository,
            ProcessusRepository processusRepository,
            AgentRepository agentRepository
    ) {
        this.repository = repository;
        this.controleRepository = controleRepository;
        this.controleService = controleService;
        this.uniteRepository = uniteRepository;
        this.processusRepository = processusRepository;
        this.agentRepository = agentRepository;
    }

    // ================= CREATE =================
    @Override
    public RapportControleInterneResponse create(RapportControleInterneRequest request) {

        UniteAdministrative unite = uniteRepository.findByCode(request.getCodeUniteAdministrative())
                .orElseThrow(() -> new RuntimeException(
                        "Unité administrative introuvable : " + request.getCodeUniteAdministrative()
                ));

        Processus processus = processusRepository.findByCode(request.getCodeProcessus())
                .orElseThrow(() -> new RuntimeException(
                        "Processus introuvable : " + request.getCodeProcessus()
                ));

        RapportControleInterne rapport = new RapportControleInterne();
        rapport.setCode(generateCode(unite.getCode()));
        rapport.setUniteAdministrative(unite);
        rapport.setProcessus(processus);
        rapport.setDateEmission(request.getDateEmission());
        rapport.setPreambule(request.getPreambule());
        rapport.setActionsCorrectivesList(versEntite(request.getActionsCorrectives()));
        rapport.setConclusion(request.getConclusion());
        // statut reste null : brouillon tant que le PDF n'est pas généré.

        return toResponse(repository.save(rapport));
    }

    // ================= GET BY CODE =================
    @Override
    public RapportControleInterneResponse getByCode(String code) {
        return toResponse(findOrThrow(code));
    }

    // ================= GET ALL =================
    @Override
    public List<RapportControleInterneResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= DELETE =================
    @Override
    public void delete(String code) {
        repository.delete(findOrThrow(code));
    }

    // ================= GÉNÉRATION PDF =================
    @Override
    public RapportControleInterneResponse genererPdf(String code) {

        RapportControleInterne rapport = findOrThrow(code);

        if (rapport.getStatut() != null && rapport.getStatut() != StatutRapportCI.EN_ATTENTE_DE_VALIDATION) {
            throw new RuntimeException(
                    "Ce rapport a déjà été transmis : son PDF ne peut plus être régénéré."
            );
        }

        rapport.setPdfContent(construirePdf(rapport));
        rapport.setPdfGenereLe(LocalDateTime.now());
        rapport.setStatut(StatutRapportCI.EN_ATTENTE_DE_VALIDATION);

        return toResponse(repository.save(rapport));
    }

    // ================= TÉLÉCHARGEMENT PDF =================
    @Override
    public byte[] getPdf(String code) {
        RapportControleInterne rapport = findOrThrow(code);
        if (rapport.getPdfContent() == null) {
            throw new RuntimeException("Le PDF de ce rapport n'a pas encore été généré.");
        }
        return rapport.getPdfContent();
    }

    // ================= TRANSMETTRE =================
    @Override
    public RapportControleInterneResponse transmettre(String code) {

        RapportControleInterne rapport = findOrThrow(code);

        if (rapport.getStatut() != StatutRapportCI.EN_ATTENTE_DE_VALIDATION) {
            throw new RuntimeException(
                    "Seul un rapport en attente de validation peut être transmis à la CCI."
            );
        }

        rapport.setStatut(StatutRapportCI.TRANSMIS);

        return toResponse(repository.save(rapport));
    }

    // ================= AVIS CCI =================
    @Override
    public RapportControleInterneResponse validerAvis(String code, AvisRapportCIRequest request) {

        RapportControleInterne rapport = findOrThrow(code);

        if (rapport.getStatut() != StatutRapportCI.TRANSMIS) {
            throw new RuntimeException(
                    "Seul un rapport transmis peut recevoir un avis de la CCI."
            );
        }

        StatutRapportCI avis = request.getAvis();
        if (avis != StatutRapportCI.VALIDE && avis != StatutRapportCI.DIFFERE && avis != StatutRapportCI.REJETE) {
            throw new RuntimeException("Avis invalide : seuls VALIDE, DIFFERE et REJETE sont acceptés.");
        }

        boolean motifRequis = avis == StatutRapportCI.DIFFERE || avis == StatutRapportCI.REJETE;
        if (motifRequis && (request.getMotif() == null || request.getMotif().isBlank())) {
            throw new RuntimeException("Le motif est obligatoire en cas de différé ou de rejet.");
        }

        rapport.setStatut(avis);
        rapport.setMotif(request.getMotif());

        return toResponse(repository.save(rapport));
    }

    // ================= SUIVI DES RECOMMANDATIONS =================
    // Statut d'avancement (Contrôleur Interne) et décision (CCI) sont deux
    // actions distinctes et réservées chacune à son rôle — voir le document
    // de référence des profils ("Écriture" pour le Contrôleur, "Écriture
    // pour décision" pour la CCI).

    @Override
    public RapportControleInterneResponse enregistrerStatutSuivi(String code, StatutSuiviRequest request) {
        RapportControleInterne rapport = findOrThrow(code);

        rapport.setStatutSuivi(request.getStatutSuivi());

        return toResponse(repository.save(rapport));
    }

    @Override
    public RapportControleInterneResponse enregistrerDecisionSuivi(String code, DecisionSuiviRequest request) {
        RapportControleInterne rapport = findOrThrow(code);

        rapport.setDecisionSuivi(request.getDecision());
        rapport.setDateDecisionSuivi(LocalDateTime.now());

        return toResponse(repository.save(rapport));
    }

    // ================= HISTORIQUE DES AVIS =================

    /**
     * Reconstitue l'historique des décisions (Transmis, Validé, Différé,
     * Rejeté) à partir des révisions Envers de l'entité, sur le modèle de
     * RisqueServiceImpl.getHistoriqueAvis().
     *
     * Contrairement au Risque (jamais supprimé, seulement changé de statut),
     * un rapport différé/rejeté est ensuite supprimé par le Contrôleur
     * Interne (voir delete() ci-dessus) : on ne peut donc pas partir de
     * l'enregistrement vivant pour retrouver son id technique, comme le fait
     * RisqueServiceImpl. On recherche ici directement par la propriété
     * "code" (immuable, unique), ce qui fonctionne aussi bien pour un
     * rapport encore existant que pour un rapport déjà supprimé.
     */
    @Override
    public List<AvisHistoriqueRapportCIResponse> getHistoriqueAvis(String code) {

        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        @SuppressWarnings("unchecked")
        List<Object[]> revisions = auditReader.createQuery()
                .forRevisionsOfEntity(RapportControleInterne.class, false, true)
                .add(AuditEntity.property("code").eq(code))
                .addOrder(AuditEntity.revisionNumber().asc())
                .getResultList();

        if (revisions.isEmpty()) {
            throw new RuntimeException("Rapport de contrôle interne introuvable : " + code);
        }

        List<AvisHistoriqueRapportCIResponse> historique = new ArrayList<>();

        StatutRapportCI dernierStatut = null;
        String dernierMotif = null;

        for (Object[] tuple : revisions) {

            RevisionType revisionType = (RevisionType) tuple[2];
            if (revisionType == RevisionType.DEL) {
                // La suppression elle-même ne porte aucune décision d'avis
                // (et sa snapshot est de toute façon vide) — la dernière
                // vraie décision (Différé/Rejeté + motif) a déjà été
                // capturée par la révision précédente, celle du validerAvis()
                // qui a précédé la suppression.
                continue;
            }

            RapportControleInterne snapshot = (RapportControleInterne) tuple[0];
            DefaultRevisionEntity revisionInfo = (DefaultRevisionEntity) tuple[1];

            StatutRapportCI statutSnapshot = snapshot.getStatut();
            String motifSnapshot = snapshot.getMotif();

            boolean change =
                    !Objects.equals(statutSnapshot, dernierStatut)
                            || !Objects.equals(motifSnapshot, dernierMotif);

            if (change) {

                String matriculeAuteur = snapshot.getUpdatedBy();

                String nomAuteur = matriculeAuteur != null
                        ? agentRepository.findByMatricule(matriculeAuteur)
                                .map(a -> a.getNom() + " " + a.getPrenoms())
                                .orElse(matriculeAuteur)
                        : null;

                historique.add(new AvisHistoriqueRapportCIResponse(
                        LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(revisionInfo.getTimestamp()),
                                ZoneId.systemDefault()
                        ),
                        statutSnapshot,
                        motifSnapshot,
                        matriculeAuteur,
                        nomAuteur
                ));
            }

            dernierStatut = statutSnapshot;
            dernierMotif = motifSnapshot;
        }

        return historique;
    }

    // ================= HELPERS =================

    private RapportControleInterne findOrThrow(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Rapport de contrôle interne introuvable : " + code));
    }

    private String generateCode(String sigleUnite) {
        long compteur = repository.countByUniteAdministrative_Code(sigleUnite) + 1;

        String code = "RCI_" + sigleUnite + String.format("%03d", compteur);
        while (repository.existsByCode(code)) {
            compteur++;
            code = "RCI_" + sigleUnite + String.format("%03d", compteur);
        }
        return code;
    }

    private String resoudreNomCreateur(RapportControleInterne rapport) {
        String matricule = rapport.getCreatedBy();
        if (matricule == null) {
            return null;
        }
        return agentRepository.findByMatricule(matricule)
                .map(a -> a.getNom() + " " + a.getPrenoms())
                .orElse(matricule);
    }

    private RapportControleInterneResponse toResponse(RapportControleInterne r) {
        return new RapportControleInterneResponse(
                r.getId(),
                r.getCode(),
                r.getUniteAdministrative() != null ? r.getUniteAdministrative().getCode() : null,
                r.getUniteAdministrative() != null ? r.getUniteAdministrative().getLibelle() : null,
                r.getProcessus() != null ? r.getProcessus().getCode() : null,
                r.getProcessus() != null ? r.getProcessus().getLibelle() : null,
                r.getDateEmission(),
                r.getPreambule(),
                versReponse(r.getActionsCorrectivesList()),
                r.getConclusion(),
                r.getStatut(),
                r.getMotif(),
                r.getPdfContent() != null,
                r.getPdfGenereLe(),
                resoudreNomCreateur(r),
                r.getStatutSuivi(),
                r.getDecisionSuivi(),
                r.getDateDecisionSuivi()
        );
    }

    private List<ActionCorrective> versEntite(List<ActionCorrectiveRequest> actions) {
        if (actions == null) {
            return List.of();
        }
        return actions.stream()
                .map(a -> new ActionCorrective(a.getLibelle(), a.getDateDebut(), a.getDateFin()))
                .collect(Collectors.toList());
    }

    private List<ActionCorrectiveResponse> versReponse(List<ActionCorrective> actions) {
        return actions.stream()
                .map(a -> new ActionCorrectiveResponse(a.getLibelle(), a.getDateDebut(), a.getDateFin()))
                .collect(Collectors.toList());
    }

    // ================= CONSTRUCTION DU PDF =================

    private byte[] construirePdf(RapportControleInterne rapport) {

        String codeUnite = rapport.getUniteAdministrative().getCode();
        String codeProcessus = rapport.getProcessus().getCode();

        List<ControleSecondNiveau> controles =
                controleRepository.findByUniteAdministrative_CodeAndProcessus_Code(codeUnite, codeProcessus);

        List<LigneControleResponse> lignes = controleService.getLignesDetaillees(codeUnite, codeProcessus);

        // Marge du haut agrandie pour laisser la place au bandeau d'en-tête
        // dessiné par EnteteEtPiedDePageRapport sur chaque page.
        Document document = new Document(PageSize.A4, 36, 36, 78, 50);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setPageEvent(new EnteteEtPiedDePageRapport(rapport.getCode()));
            document.open();

            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12.5f, TEXTE_FONCE);
            Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
            Font texteFont = FontFactory.getFont(FontFactory.HELVETICA, 9, GRIS_TEXTE);
            Font sousTitreFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, VERT_ENTETE);

            document.add(carteInformations(rapport, codeUnite, codeProcessus));

            // ===== Déroulement des contrôles du second niveau =====
            document.add(titreSection("Déroulement des contrôles du second niveau"));

            if (controles.isEmpty()) {
                document.add(new Paragraph("Aucun contrôle de second niveau saisi pour ce processus.", texteFont));
            } else {
                for (ControleSecondNiveau c : controles) {
                    document.add(carteControle(c, sousTitreFont, labelFont, texteFont));
                }
            }

            // ===== Synthèse =====
            document.add(titreSection("Synthèse"));

            document.add(blocTexte("Préambule", rapport.getPreambule(), texteFont));
            document.add(tableauAnomaliesFaiblessesConstats(lignes, labelFont, texteFont));
            document.add(titreSousSection("Analyses et recommandations"));
            document.add(tableauAnalysesRecommandations(lignes, labelFont, texteFont));
            document.add(blocActionsCorrectives(rapport.getActionsCorrectivesList(), texteFont));
            document.add(blocTexte("Conclusion", rapport.getConclusion(), texteFont));

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF du rapport de contrôle interne", e);
        }
    }

    /**
     * Bandeau vert en en-tête de chaque page (titre + code du rapport) et
     * pied de page avec pagination, sur le modèle de
     * AgentServiceImpl.EnteteEtPiedDePage, pour un rendu cohérent avec les
     * autres PDF générés par l'application.
     */
    private static class EnteteEtPiedDePageRapport extends PdfPageEventHelper {

        private final String codeRapport;

        EnteteEtPiedDePageRapport(String codeRapport) {
            this.codeRapport = codeRapport;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            PdfContentByte canvas = writer.getDirectContent();
            Rectangle pageSize = document.getPageSize();

            canvas.saveState();
            canvas.setColorFill(VERT_ENTETE);
            canvas.rectangle(0, pageSize.getHeight() - 56, pageSize.getWidth(), 56);
            canvas.fill();
            canvas.restoreState();

            ColumnText.showTextAligned(
                    canvas, Element.ALIGN_LEFT,
                    new Phrase("Rapport de Contrôle Interne", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, Color.WHITE)),
                    36, pageSize.getHeight() - 32, 0
            );
            ColumnText.showTextAligned(
                    canvas, Element.ALIGN_LEFT,
                    new Phrase(codeRapport, FontFactory.getFont(FontFactory.HELVETICA, 9.5f, VERT_CLAIR)),
                    36, pageSize.getHeight() - 46, 0
            );
            ColumnText.showTextAligned(
                    canvas, Element.ALIGN_RIGHT,
                    new Phrase("SIGR", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, VERT_CLAIR)),
                    pageSize.getWidth() - 36, pageSize.getHeight() - 36, 0
            );

            canvas.saveState();
            canvas.setLineWidth(0.5f);
            canvas.setColorStroke(Color.LIGHT_GRAY);
            canvas.moveTo(36, 38);
            canvas.lineTo(pageSize.getWidth() - 36, 38);
            canvas.stroke();
            canvas.restoreState();

            ColumnText.showTextAligned(
                    canvas, Element.ALIGN_LEFT,
                    new Phrase("Système Intégré de Gestion des Risques (SIGR) — Contrôle Interne",
                            FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY)),
                    36, 24, 0
            );
            ColumnText.showTextAligned(
                    canvas, Element.ALIGN_RIGHT,
                    new Phrase("Page " + document.getPageNumber(), FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY)),
                    pageSize.getWidth() - 36, 24, 0
            );
        }
    }

    /**
     * Carte d'informations en tête de document (UA, Processus, dates,
     * créateur, statut) : fond légèrement teinté + badge de statut coloré
     * repris des mêmes couleurs que les badges du frontend.
     */
    /** Carte d'informations en 2 vraies colonnes côte à côte (et non un empilement vertical). */
    private PdfPTable carteInformations(RapportControleInterne rapport, String codeUnite, String codeProcessus) {

        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7.5f, GRIS_LABEL);
        Font valeurFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, TEXTE_FONCE);

        PdfPTable interieur = new PdfPTable(2);
        interieur.setWidthPercentage(100);
        try {
            interieur.setWidths(new float[] { 50f, 50f });
        } catch (Exception ignored) { /* défaut si largeurs invalides */ }

        interieur.addCell(celluleInfo("UNITÉ ADMINISTRATIVE",
                rapport.getUniteAdministrative().getLibelle() + " (" + codeUnite + ")", labelFont, valeurFont));
        interieur.addCell(celluleInfo("PROCESSUS",
                rapport.getProcessus().getLibelle() + " (" + codeProcessus + ")", labelFont, valeurFont));

        interieur.addCell(celluleInfo("DATE D'ÉMISSION", formatDate(rapport.getDateEmission()), labelFont, valeurFont));
        interieur.addCell(celluleInfo("CRÉÉ PAR", vide(resoudreNomCreateur(rapport)), labelFont, valeurFont));

        PdfPCell celluleStatutLabel = new PdfPCell(new Phrase("STATUT", labelFont));
        celluleStatutLabel.setBorder(Rectangle.NO_BORDER);
        celluleStatutLabel.setPaddingTop(8f);
        celluleStatutLabel.setPaddingBottom(3f);
        celluleStatutLabel.setColspan(2);
        interieur.addCell(celluleStatutLabel);

        PdfPCell badgeCell = new PdfPCell();
        badgeCell.setBorder(Rectangle.NO_BORDER);
        badgeCell.setColspan(2);
        badgeCell.addElement(badgeStatut(rapport.getStatut()));
        interieur.addCell(badgeCell);

        PdfPCell carte = new PdfPCell();
        carte.addElement(interieur);
        carte.setBackgroundColor(GRIS_ZEBRA);
        carte.setBorderColor(GRIS_BORDURE);
        carte.setBorderWidth(0.75f);
        carte.setPadding(14f);

        PdfPTable enveloppe = new PdfPTable(1);
        enveloppe.setWidthPercentage(100);
        enveloppe.setSpacingAfter(4f);
        enveloppe.addCell(carte);
        return enveloppe;
    }

    private PdfPCell celluleInfo(String label, String valeur, Font labelFont, Font valeurFont) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingTop(8f);
        cell.setPaddingBottom(2f);
        cell.setPaddingRight(10f);

        Paragraph libelle = new Paragraph(label, labelFont);
        libelle.setSpacingAfter(2f);
        cell.addElement(libelle);
        cell.addElement(new Paragraph(valeur, valeurFont));
        return cell;
    }

    /** Petit badge coloré (fond clair + texte) reprenant getStatutBadgeClass() du frontend. */
    private PdfPTable badgeStatut(StatutRapportCI statut) {
        Color fond;
        Color texte;
        String libelle;

        if (statut == null) {
            fond = new Color(241, 245, 249); texte = new Color(100, 116, 139); libelle = "Brouillon";
        } else {
            switch (statut) {
                case EN_ATTENTE_DE_VALIDATION -> { fond = new Color(219, 234, 254); texte = new Color(29, 78, 216); libelle = "En attente de validation"; }
                case TRANSMIS -> { fond = new Color(254, 243, 199); texte = new Color(180, 83, 9); libelle = "Transmis à la CCI"; }
                case VALIDE -> { fond = VERT_CLAIR; texte = VERT_ENTETE; libelle = "Validé"; }
                case DIFFERE -> { fond = new Color(255, 237, 213); texte = new Color(194, 65, 12); libelle = "Différé"; }
                case REJETE -> { fond = new Color(254, 226, 226); texte = new Color(185, 28, 28); libelle = "Rejeté"; }
                default -> { fond = new Color(241, 245, 249); texte = new Color(100, 116, 139); libelle = statut.name(); }
            }
        }

        PdfPTable badge = new PdfPTable(1);
        try { badge.setTotalWidth(160f); } catch (Exception ignored) { /* défaut */ }
        badge.setLockedWidth(true);

        PdfPCell cell = new PdfPCell(new Phrase(libelle, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, texte)));
        cell.setBackgroundColor(fond);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        badge.addCell(cell);
        return badge;
    }

    private PdfPTable titreSection(String texte) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        try {
            table.setWidths(new float[] { 1.5f, 98.5f });
        } catch (Exception ignored) { /* défaut */ }
        table.setSpacingBefore(14f);
        table.setSpacingAfter(8f);

        PdfPCell barre = new PdfPCell(new Phrase(" "));
        barre.setBackgroundColor(VERT_ENTETE);
        barre.setBorder(Rectangle.NO_BORDER);
        barre.setFixedHeight(18f);
        table.addCell(barre);

        PdfPCell titre = new PdfPCell(new Phrase(texte, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12.5f, TEXTE_FONCE)));
        titre.setBorder(Rectangle.NO_BORDER);
        titre.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titre.setPaddingLeft(8f);
        table.addCell(titre);

        return table;
    }

    /** Encadre un ControleSecondNiveau (3 lignes + évolution de conformité) dans une carte bordée. */
    private PdfPTable carteControle(ControleSecondNiveau c, Font sousTitreFont, Font labelFont, Font texteFont) {

        PdfPTable contenu = new PdfPTable(1);
        contenu.setWidthPercentage(100);

        PdfPCell entete = new PdfPCell(new Phrase(
                "Contrôle du " + formatDate(c.getDateControle()) + "  ·  " + c.getCode(), sousTitreFont));
        entete.setBorder(Rectangle.NO_BORDER);
        entete.setPaddingBottom(6f);
        contenu.addCell(entete);

        PdfPCell ligneTests = new PdfPCell(tableauLigne("Tests", c.getTestsLibelle(), c.getTestsConstats(), c.getTestsAnalyse(), c.getTestsRecommandation(), labelFont, texteFont));
        ligneTests.setBorder(Rectangle.NO_BORDER);
        ligneTests.setPaddingBottom(3f);
        contenu.addCell(ligneTests);

        PdfPCell ligneRevues = new PdfPCell(tableauLigne("Revues", c.getRevuesLibelle(), c.getRevuesConstats(), c.getRevuesAnalyse(), c.getRevuesRecommandation(), labelFont, texteFont));
        ligneRevues.setBorder(Rectangle.NO_BORDER);
        ligneRevues.setPaddingBottom(3f);
        contenu.addCell(ligneRevues);

        PdfPCell ligneVerif = new PdfPCell(tableauLigne("Vérification", c.getVerificationLibelleDesPieces(), c.getVerificationConstats(), c.getVerificationAnalyse(), c.getVerificationRecommandation(), labelFont, texteFont));
        ligneVerif.setBorder(Rectangle.NO_BORDER);
        ligneVerif.setPaddingBottom(3f);
        contenu.addCell(ligneVerif);

        PdfPCell evolution = new PdfPCell(tableauEvolution(c, labelFont, texteFont));
        evolution.setBorder(Rectangle.NO_BORDER);
        contenu.addCell(evolution);

        PdfPCell carte = new PdfPCell();
        carte.addElement(contenu);
        carte.setBorderColor(GRIS_BORDURE);
        carte.setBorderWidth(0.75f);
        carte.setPadding(12f);

        PdfPTable enveloppe = new PdfPTable(1);
        enveloppe.setWidthPercentage(100);
        enveloppe.setSpacingAfter(10f);
        enveloppe.addCell(carte);
        return enveloppe;
    }

    private PdfPTable tableauLigne(String libelleLigne, String libelle, String constats, String analyse, String recommandation, Font labelFont, Font texteFont) {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        try {
            table.setWidths(new float[] { 20f, 32f, 48f });
        } catch (Exception ignored) { /* largeurs par défaut si jamais invalide */ }

        PdfPCell entete = new PdfPCell(new Phrase(libelleLigne, labelFont));
        entete.setBackgroundColor(VERT_ENTETE);
        entete.setPadding(6f);
        entete.setVerticalAlignment(Element.ALIGN_MIDDLE);
        entete.setBorderColor(VERT_ENTETE);
        table.addCell(entete);

        PdfPCell celluleLibelle = new PdfPCell(new Phrase(vide(libelle), texteFont));
        celluleLibelle.setBackgroundColor(GRIS_ZEBRA);
        celluleLibelle.setPadding(6f);
        celluleLibelle.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celluleLibelle.setBorderColor(GRIS_BORDURE);
        table.addCell(celluleLibelle);

        PdfPCell celluleDetail = new PdfPCell();
        celluleDetail.setPadding(6f);
        celluleDetail.setBorderColor(GRIS_BORDURE);
        celluleDetail.addElement(paragrapheEtiquette("Constats", constats, texteFont));
        celluleDetail.addElement(paragrapheEtiquette("Analyse", analyse, texteFont));
        celluleDetail.addElement(paragrapheEtiquette("Recommandation", recommandation, texteFont));
        table.addCell(celluleDetail);

        return table;
    }

    /** Bloc "Évolution de conformité" présenté en 4 lignes label/valeur plutôt qu'en texte concaténé. */
    private PdfPTable tableauEvolution(ControleSecondNiveau c, Font labelFont, Font texteFont) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(4f);
        try {
            table.setWidths(new float[] { 20f, 80f });
        } catch (Exception ignored) { /* défaut */ }

        PdfPCell entete = new PdfPCell(new Phrase("Évolution de conformité", labelFont));
        entete.setBackgroundColor(VERT_ENTETE);
        entete.setPadding(6f);
        entete.setColspan(2);
        table.addCell(entete);

        ligneEvolution(table, "Intitulé de l'opération", c.getEvolutionIntituleOperation(), texteFont);
        ligneEvolution(table, "Procédures internes / Renforcements", c.getEvolutionProceduresInternesRenforcements(), texteFont);
        ligneEvolution(table, "Résultats de conformité", c.getEvolutionResultatsConformite(), texteFont);
        ligneEvolution(table, "Analyse", c.getEvolutionAnalyse(), texteFont);
        ligneEvolution(table, "Recommandation", c.getEvolutionRecommandation(), texteFont);

        return table;
    }

    private void ligneEvolution(PdfPTable table, String libelle, String valeur, Font texteFont) {
        PdfPCell celluleLibelle = new PdfPCell(new Phrase(libelle, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, GRIS_LABEL)));
        celluleLibelle.setBackgroundColor(GRIS_ZEBRA);
        celluleLibelle.setPadding(5f);
        celluleLibelle.setBorderColor(GRIS_BORDURE);
        table.addCell(celluleLibelle);

        PdfPCell celluleValeur = new PdfPCell(new Phrase(vide(valeur), texteFont));
        celluleValeur.setPadding(5f);
        celluleValeur.setBorderColor(GRIS_BORDURE);
        table.addCell(celluleValeur);
    }

    private Paragraph paragrapheEtiquette(String etiquette, String valeur, Font texteFont) {
        Paragraph p = new Paragraph();
        p.setSpacingAfter(2f);
        p.add(new Chunk(etiquette + " : ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, GRIS_LABEL)));
        p.add(new Chunk(vide(valeur), texteFont));
        return p;
    }

    /**
     * Anomalies, Faiblesses et Constats sur 3 colonnes — chaque puce précise
     * entre crochets sa catégorie et le contrôle d'origine (ex. "[Tests —
     * CSN_DB001]") pour qu'on sache clairement à quoi chaque élément se
     * rapporte, plutôt que des listes plates mélangées.
     */
    private PdfPTable tableauAnomaliesFaiblessesConstats(List<LigneControleResponse> lignes, Font labelFont, Font texteFont) {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(6f);
        table.setSpacingAfter(10f);

        for (String entete : new String[] { "Anomalies", "Faiblesses", "Constats" }) {
            PdfPCell cell = new PdfPCell(new Phrase(entete, labelFont));
            cell.setBackgroundColor(VERT_ENTETE);
            cell.setPadding(6f);
            table.addCell(cell);
        }

        table.addCell(celluleListeConstats(lignes, l -> "Anomalie".equals(l.getCategorie()), texteFont));
        table.addCell(celluleListeConstats(lignes, l -> "Faiblesse".equals(l.getCategorie()), texteFont));
        table.addCell(celluleListeConstats(lignes, l -> !"Anomalie".equals(l.getCategorie()) && !"Faiblesse".equals(l.getCategorie()), texteFont));

        return table;
    }

    private PdfPCell celluleListeConstats(List<LigneControleResponse> lignes, java.util.function.Predicate<LigneControleResponse> filtre, Font texteFont) {
        Font puceFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, VERT_ENTETE);
        Font sourceFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 7.5f, GRIS_LABEL);

        PdfPCell cell = new PdfPCell();
        cell.setPadding(8f);
        cell.setBorderColor(GRIS_BORDURE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);

        List<LigneControleResponse> filtrees = lignes.stream()
                .filter(filtre)
                .filter(l -> l.getConstat() != null && !l.getConstat().isBlank())
                .collect(Collectors.toList());

        if (filtrees.isEmpty()) {
            cell.addElement(new Paragraph("Aucun élément.", texteFont));
            return cell;
        }

        for (LigneControleResponse l : filtrees) {
            Paragraph p = new Paragraph();
            p.setSpacingAfter(5f);
            p.add(new Chunk("● ", puceFont));
            p.add(new Chunk(l.getConstat(), texteFont));
            p.add(Chunk.NEWLINE);
            p.add(new Chunk(sourceLigne(l), sourceFont));
            cell.addElement(p);
        }
        return cell;
    }

    /**
     * Analyses et recommandations reliées ligne par ligne (et non deux
     * colonnes indépendantes) : chaque ligne du tableau porte la source,
     * l'analyse et la recommandation d'un même constat, pour éviter tout
     * mélange entre analyses et recommandations qui ne se correspondent pas.
     */
    private PdfPTable tableauAnalysesRecommandations(List<LigneControleResponse> lignes, Font labelFont, Font texteFont) {
        List<LigneControleResponse> filtrees = lignes.stream()
                .filter(l -> (l.getAnalyse() != null && !l.getAnalyse().isBlank())
                        || (l.getRecommandation() != null && !l.getRecommandation().isBlank()))
                .collect(Collectors.toList());

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(6f);
        table.setSpacingAfter(10f);
        try {
            table.setWidths(new float[] { 22f, 39f, 39f });
        } catch (Exception ignored) { /* défaut */ }

        for (String entete : new String[] { "Source", "Analyse", "Recommandation" }) {
            PdfPCell cell = new PdfPCell(new Phrase(entete, labelFont));
            cell.setBackgroundColor(VERT_ENTETE);
            cell.setPadding(6f);
            table.addCell(cell);
        }

        if (filtrees.isEmpty()) {
            PdfPCell vide = new PdfPCell(new Phrase("Aucune analyse ni recommandation.", texteFont));
            vide.setColspan(3);
            vide.setPadding(8f);
            table.addCell(vide);
            return table;
        }

        Font sourceFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, GRIS_TEXTE);
        for (LigneControleResponse l : filtrees) {
            PdfPCell celluleSource = new PdfPCell(new Phrase(sourceLigne(l), sourceFont));
            celluleSource.setBackgroundColor(GRIS_ZEBRA);
            celluleSource.setPadding(6f);
            celluleSource.setBorderColor(GRIS_BORDURE);
            table.addCell(celluleSource);

            PdfPCell celluleAnalyse = new PdfPCell(new Phrase(vide(l.getAnalyse()), texteFont));
            celluleAnalyse.setPadding(6f);
            celluleAnalyse.setBorderColor(GRIS_BORDURE);
            table.addCell(celluleAnalyse);

            PdfPCell celluleRecommandation = new PdfPCell(new Phrase(vide(l.getRecommandation()), texteFont));
            celluleRecommandation.setPadding(6f);
            celluleRecommandation.setBorderColor(GRIS_BORDURE);
            table.addCell(celluleRecommandation);
        }

        return table;
    }

    private String sourceLigne(LigneControleResponse l) {
        return l.getCategorie() + " — " + l.getCodeControle() + " (" + formatDate(l.getDateControle()) + ")";
    }

    /** Actions correctrices sous forme de liste à puces, chacune avec sa période. */
    private Paragraph blocActionsCorrectives(List<ActionCorrective> actions, Font texteFont) {
        Paragraph p = new Paragraph();
        p.setSpacingAfter(8f);
        p.add(new Chunk("Actions correctrices : ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, GRIS_TEXTE)));

        if (actions == null || actions.isEmpty()) {
            p.add(new Chunk("—", texteFont));
            return p;
        }

        Font puceFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, VERT_ENTETE);
        for (ActionCorrective a : actions) {
            p.add(Chunk.NEWLINE);
            p.add(new Chunk("● ", puceFont));
            p.add(new Chunk(
                    vide(a.getLibelle()) + "  (du " + formatDate(a.getDateDebut()) + " au " + formatDate(a.getDateFin()) + ")",
                    texteFont
            ));
        }
        return p;
    }

    private Paragraph titreSousSection(String texte) {
        Paragraph p = new Paragraph(texte, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9.5f, TEXTE_FONCE));
        p.setSpacingBefore(4f);
        p.setSpacingAfter(4f);
        return p;
    }

    private Paragraph blocTexte(String titre, String contenu, Font texteFont) {
        Paragraph p = new Paragraph();
        p.setSpacingAfter(8f);
        p.add(new Chunk(titre + " : ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, GRIS_TEXTE)));
        p.add(new Chunk(vide(contenu), texteFont));
        return p;
    }

    private String vide(String valeur) {
        return valeur == null || valeur.isBlank() ? "—" : valeur;
    }

    private String formatDate(java.time.LocalDate date) {
        return date == null ? "—" : date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}

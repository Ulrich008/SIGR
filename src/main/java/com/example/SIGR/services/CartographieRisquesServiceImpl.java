package com.example.SIGR.services;

import com.example.SIGR.dto.request.CartographieRisquesRequest;
import com.example.SIGR.dto.response.CartographieRisqueDetailResponse;
import com.example.SIGR.dto.response.CartographieRisquesResponse;
import com.example.SIGR.entity.CartographieRisques;
import com.example.SIGR.repository.CartographieRisquesRepository;
import com.example.SIGR.repository.EvaluationRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartographieRisquesServiceImpl implements CartographieRisquesService {

    private final CartographieRisquesRepository repository;
    private final EvaluationRepository evaluationRepository;

    public CartographieRisquesServiceImpl(CartographieRisquesRepository repository, EvaluationRepository evaluationRepository) {
        this.repository = repository;
        this.evaluationRepository = evaluationRepository;
    }

    // ================= GENERATE CODE =================
    private String generateCode() {

        long count = repository.count() + 1;

        return String.format("CARTO-%03d", count);
    }

    // ================= CREATE =================
    @Override
    public CartographieRisquesResponse create(CartographieRisquesRequest request) {

        if (repository.existsByTitre(request.getTitre())) {
            throw new RuntimeException(
                    "Titre déjà utilisé : " + request.getTitre());
        }

        String code = generateCode();

        CartographieRisques entity = new CartographieRisques()
                .setCode(code)
                .setTitre(request.getTitre())
                .setPeriode(request.getPeriode())
                .setSeuilFaible(request.getSeuilFaible())
                .setSeuilMoyen(request.getSeuilMoyen())
                .setSeuilEleve(request.getSeuilEleve())
                .setStatut(request.getStatut());

        return toResponse(repository.save(entity));
    }

    // ================= GET BY CODE =================
    @Override
    public CartographieRisquesResponse getByCode(String code) {

        CartographieRisques entity = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Cartographie introuvable : " + code));

        return toResponse(entity);
    }

    // ================= GET ALL =================
    @Override
    public List<CartographieRisquesResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @Override
    public CartographieRisquesResponse update(
            String code,
            CartographieRisquesRequest request
    ) {

        CartographieRisques entity = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Cartographie introuvable : " + code));

        // Vérification unicité titre
        if (!entity.getTitre().equals(request.getTitre())
                && repository.existsByTitre(request.getTitre())) {

            throw new RuntimeException(
                    "Titre déjà utilisé : " + request.getTitre());
        }

        entity.setTitre(request.getTitre());
        entity.setPeriode(request.getPeriode());
        entity.setSeuilFaible(request.getSeuilFaible());
        entity.setSeuilMoyen(request.getSeuilMoyen());
        entity.setSeuilEleve(request.getSeuilEleve());
        entity.setStatut(request.getStatut());

        return toResponse(repository.save(entity));
    }

    // ================= DELETE =================
    @Override
    public void delete(String code) {

        CartographieRisques entity = repository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Cartographie introuvable : " + code));

        repository.delete(entity);
    }

    @Override
    public byte[] generateExcel() {
        List<CartographieRisqueDetailResponse> data =
                evaluationRepository.findCartographieRisquesDetail();

        try (
                InputStream templateStream = getClass()
                        .getResourceAsStream("/templates/cartographie-risques-template.xlsx");
                Workbook workbook = new XSSFWorkbook(templateStream);
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.getSheetAt(0);

            // ================= STYLE BODY =================
            CellStyle bodyStyle = workbook.createCellStyle();
            bodyStyle.setWrapText(true);
            bodyStyle.setVerticalAlignment(VerticalAlignment.TOP);
            bodyStyle.setBorderTop(BorderStyle.THIN);
            bodyStyle.setBorderBottom(BorderStyle.THIN);
            bodyStyle.setBorderLeft(BorderStyle.THIN);
            bodyStyle.setBorderRight(BorderStyle.THIN);

            // ================= DATA (ligne 4 = index 3, après les 3 lignes de headers) =================
            int rowIndex = 3; // ✅ CORRECTION : était 2, doit être 3

            for (CartographieRisqueDetailResponse r : data) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    row = sheet.createRow(rowIndex);
                }
                rowIndex++;

                // COL A (0)  — Ref.
                setCell(row, 0,  value(r.getCodeRisque()),             bodyStyle);
                // COL B (1)  — Mission/Activité
                setCell(row, 1,  value(r.getLibelleProcessus()),       bodyStyle);
                // COL C (2)  — Risques retenus
                setCell(row, 2,  value(r.getLibelleRisque()),          bodyStyle);
                // COL D (3)  — Type de risque
                setCell(row, 3,  value(r.getTypeRisque()),             bodyStyle);
                // COL E (4)  — Causes probables
                setCell(row, 4,  value(r.getCauseProbable()),          bodyStyle);
                // COL F (5)  — Conséquences probables
                setCell(row, 5,  value(r.getConsequenceProbable()),    bodyStyle);
                // COL G (6)  — Entités impliquées
                setCell(row, 6,  value(r.getUniteAdministrative()),    bodyStyle);
                // COL H (7)  — Impact Inhérent (I) 1 à 5
                setCellNumeric(row, 7,  r.getImpactInherent(),         bodyStyle);
                // COL I (8)  — Probabilité Inhérente (P) 1 à 5
                setCellNumeric(row, 8,  r.getProbabiliteInherente(),   bodyStyle);
                // COL J (9)  — Score Inhérent = PxI  ← formule automatique dans le template
                setCellNumeric(row, 9,  r.getScoreInherent(),          bodyStyle);
                // COL K (10) — Bonnes pratiques CI
                setCell(row, 10, value(r.getBonnesPratiques()),        bodyStyle);
                // COL L (11) — Contrôles existants (forces)
                setCell(row, 11, value(r.getControleExistants()),      bodyStyle);
                // COL M (12) — Contrôles inexistants (faiblesses)
                setCell(row, 12, value(r.getControleInexistants()),    bodyStyle);
                // COL N (13) — Protection (Pro) 0 à 3
                setCellNumeric(row, 13, r.getProtection(),             bodyStyle);
                // COL O (14) — Prévention (Pré) 0 à 3
                setCellNumeric(row, 14, r.getPrevention(),             bodyStyle);
                // COL P (15) — Impact Résiduel = I - Pro
                setCellNumeric(row, 15, r.getImpactResiduel(),         bodyStyle);
                // COL Q (16) — Probabilité Résiduelle = P - Pré
                setCellNumeric(row, 16, r.getProbabiliteResiduelle(),  bodyStyle);
                // COL R (17) — Score Résiduel = PxI
                setCellNumeric(row, 17, r.getScoreResiduel(),          bodyStyle);
                // COL S (18) — Stratégie de traitement du risque
                setCell(row, 18, value(r.getStatutRisque()),           bodyStyle);
                // COL T (19) — Déjà Survenu Oui/Non
                setCell(row, 19, value(r.getDejaSurvenu()),            bodyStyle);
                // COL U (20) — Rang de priorité Échelle 1 à 3
                setCellNumeric(row, 20, r.getRangPriorite(),           bodyStyle);
                // COL V (21) — Actions de maîtrise (Recommandations)
                setCell(row, 21, value(r.getRecommandation()),         bodyStyle);
                // COL W (22) — Début
                setCell(row, 22, value(r.getDateDebut()),              bodyStyle);
                // COL X (23) — Fin
                setCell(row, 23, value(r.getDateFin()),                bodyStyle);
                // COL Y (24) — Indicateur de réalisation
                // COL Z (25) — Audit Proposé
                setCell(row, 25, value(r.getEvaluePar()),              bodyStyle);

            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erreur génération Excel cartographie depuis template", e);
        }
    }

// ================= HELPER =================

    private void setCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(style);
        cell.setCellValue(value != null ? value : "");
    }

    private void setCellNumeric(Row row, int col, Object value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(style);
        if (value == null) {
            cell.setBlank();
            return;
        }
        try {
            if (value instanceof Number) {
                cell.setCellValue(((Number) value).doubleValue());
            } else {
                double d = Double.parseDouble(value.toString().trim());
                cell.setCellValue(d);
            }
        } catch (NumberFormatException e) {
            cell.setCellValue(value.toString());
        }
    }

    private String value(Object obj) {
        if (obj == null) return "";
        if (obj instanceof java.time.LocalDate) {
            return obj.toString(); // format ISO : 2026-01-01
        }
        if (obj instanceof java.time.LocalDateTime) {
            return ((java.time.LocalDateTime) obj)
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return obj.toString().trim();
    }



    // ================= MAPPER =================
    private CartographieRisquesResponse toResponse(
            CartographieRisques entity
    ) {

        int nbRisques = entity.getRisques() != null
                ? entity.getRisques().size()
                : 0;

        return new CartographieRisquesResponse(
                entity.getId(),
                entity.getCode(),
                entity.getTitre(),
                entity.getPeriode(),
                entity.getSeuilFaible(),
                entity.getSeuilMoyen(),
                entity.getSeuilEleve(),
                entity.getStatut(),
                nbRisques
        );
    }
}
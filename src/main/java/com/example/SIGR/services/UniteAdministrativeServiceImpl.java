package com.example.SIGR.services;
import com.example.SIGR.dto.request.UniteAdministrativeRequest;
import com.example.SIGR.dto.response.ImportLigneErreurResponse;
import com.example.SIGR.dto.response.ImportResultResponse;
import com.example.SIGR.dto.response.UniteAdministrativeResponse;
import com.example.SIGR.entity.Ministere;
import com.example.SIGR.entity.TypeUnite;
import com.example.SIGR.entity.UniteAdministrative;
import com.example.SIGR.repository.MinistereRepository;
import com.example.SIGR.repository.TypeUniteRepository;
import com.example.SIGR.repository.UniteAdministrativeRepository;
import com.example.SIGR.security.SecurityUtils;
import com.example.SIGR.util.ExcelImportUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UniteAdministrativeServiceImpl implements UniteAdministrativeService {

    private final UniteAdministrativeRepository uniteRepository;
    private final MinistereRepository ministereRepository;
    private final TypeUniteRepository typeUniteRepository;
    private final Validator validator;

    public UniteAdministrativeServiceImpl(
            UniteAdministrativeRepository uniteRepository,
            MinistereRepository ministereRepository,
            TypeUniteRepository typeUniteRepository,
            Validator validator
    ) {
        this.uniteRepository    = uniteRepository;
        this.ministereRepository = ministereRepository;
        this.typeUniteRepository = typeUniteRepository;
        this.validator = validator;
    }

    // =========================================================
    // CREATE
    // =========================================================
    @Override
    public UniteAdministrativeResponse create(UniteAdministrativeRequest request) {

        // ================= DOUBLON CODE =================
        if (uniteRepository.existsByCode(request.getCode())) {
            throw new RuntimeException(
                    "Une unité existe déjà avec ce code : " + request.getCode()
            );
        }

        // ================= DOUBLON LIBELLE =================
        if (uniteRepository.existsByLibelleIgnoreCase(request.getLibelle())) {
            throw new RuntimeException(
                    "Une unité existe déjà avec ce libellé : " + request.getLibelle()
            );
        }

        verifierAccesMinistere(request.getCodeMinistere());

        Ministere ministere = ministereRepository.findByCode(request.getCodeMinistere())
                .orElseThrow(() ->
                        new RuntimeException("Ministère introuvable : " + request.getCodeMinistere())
                );

        TypeUnite typeUnite = typeUniteRepository.findByCode(request.getIdTypeUnite())
                .orElseThrow(() ->
                        new RuntimeException("Type unité introuvable : " + request.getIdTypeUnite())
                );

        UniteAdministrative parent = resolveParent(request.getIdUniteParent());

        UniteAdministrative unite = new UniteAdministrative();
        unite.setCode(request.getCode());
        unite.setLibelle(request.getLibelle());
        unite.setTypeUnite(typeUnite);
        unite.setMinistere(ministere);
        unite.setParent(parent);
        unite.setNiveauHierarchique(request.getNiveauHierarchique());

        return toResponse(uniteRepository.save(unite));
    }

    // =========================================================
    // GET BY CODE
    // =========================================================
    @Override
    public UniteAdministrativeResponse getByCode(String code) {
        UniteAdministrative unite = uniteRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Unité introuvable : " + code)
                );
        return toResponse(unite);
    }

    // =========================================================
    // GET ALL
    // =========================================================
    @Override
    public List<UniteAdministrativeResponse> getAll() {
        return uniteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // UPDATE
    // =========================================================
    @Override
    public UniteAdministrativeResponse update(String code, UniteAdministrativeRequest request) {

        UniteAdministrative unite = uniteRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Unité introuvable : " + code)
                );

        // ================= DOUBLON LIBELLE (hors entité courante) =================
        if (request.getLibelle() != null) {
            boolean libelleDejaUtilise = uniteRepository
                    .existsByLibelleIgnoreCaseAndCodeNot(request.getLibelle(), code);
            if (libelleDejaUtilise) {
                throw new RuntimeException(
                        "Une autre unité existe déjà avec ce libellé : " + request.getLibelle()
                );
            }
            unite.setLibelle(request.getLibelle());
        }

        // ================= TYPE UNITE =================
        if (request.getIdTypeUnite() != null && !request.getIdTypeUnite().isBlank()) {
            TypeUnite typeUnite = typeUniteRepository.findByCode(request.getIdTypeUnite())
                    .orElseThrow(() ->
                            new RuntimeException("Type unité introuvable : " + request.getIdTypeUnite())
                    );
            unite.setTypeUnite(typeUnite);
        }

        // ================= MINISTERE =================
        if (request.getCodeMinistere() != null && !request.getCodeMinistere().isBlank()) {
            verifierAccesMinistere(request.getCodeMinistere());
            Ministere ministere = ministereRepository.findByCode(request.getCodeMinistere())
                    .orElseThrow(() ->
                            new RuntimeException("Ministère introuvable : " + request.getCodeMinistere())
                    );
            unite.setMinistere(ministere);
        }

        // ================= PARENT =================
        if (request.getIdUniteParent() != null) {
            if (request.getIdUniteParent().isBlank()) {
                unite.setParent(null);
            } else {
                UniteAdministrative parent = uniteRepository.findByCode(request.getIdUniteParent())
                        .orElseThrow(() ->
                                new RuntimeException("Unité parent introuvable : " + request.getIdUniteParent())
                        );
                unite.setParent(parent);
            }
        }

        if (request.getNiveauHierarchique() != null) {
            unite.setNiveauHierarchique(request.getNiveauHierarchique());
        }

        return toResponse(uniteRepository.save(unite));
    }

    // =========================================================
    // DELETE
    // =========================================================
    @Override
    public void delete(String code) {
        UniteAdministrative unite = uniteRepository.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Unité introuvable : " + code)
                );
        uniteRepository.delete(unite);
    }

    // =========================================================
    // HELPERS
    // =========================================================

    /**
     * Un ADMIN ne peut créer/déplacer une unité que dans son propre
     * ministère. Le SUPER_ADMIN n'est soumis à aucune restriction.
     */
    private void verifierAccesMinistere(String codeMinistereCible) {

        if (SecurityUtils.hasAuthority("SUPER_ADMIN")) {
            return;
        }

        String codeMinistereCourant = SecurityUtils.getCurrentMinistereCode();

        if (codeMinistereCourant == null
                || !codeMinistereCourant.equals(codeMinistereCible)) {

            throw new AccessDeniedException(
                    "Vous ne pouvez gérer que les unités de votre propre ministère"
            );
        }
    }

    private UniteAdministrative resolveParent(String idUniteParent) {
        if (idUniteParent == null || idUniteParent.isBlank()) {
            return null;
        }
        return uniteRepository.findByCode(idUniteParent)
                .orElseThrow(() ->
                        new RuntimeException("Unité parent introuvable : " + idUniteParent)
                );
    }

    private UniteAdministrativeResponse toResponse(UniteAdministrative unite) {
        return new UniteAdministrativeResponse(
                unite.getId(),
                unite.getCode(),
                unite.getLibelle(),
                unite.getTypeUnite()  != null ? unite.getTypeUnite().getCode()    : null,
                unite.getTypeUnite()  != null ? unite.getTypeUnite().getLibelle() : null,
                unite.getMinistere()  != null ? unite.getMinistere().getCode()    : null,
                unite.getMinistere()  != null ? unite.getMinistere().getNom()     : null,
                unite.getParent()     != null ? unite.getParent().getCode()       : null,
                unite.getNiveauHierarchique()
        );
    }

    // =========================================================
    // IMPORT EXCEL
    // =========================================================

    private static final String[] COLONNES_IMPORT_UNITE = {
            "Code", "Libellé", "Code type unité", "Code ministère",
            "Code unité parente", "Niveau hiérarchique"
    };

    @Override
    public ImportResultResponse importFromExcel(MultipartFile file) {
        List<ImportLigneErreurResponse> echecs = new ArrayList<>();
        int total = 0;
        int succes = 0;

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (ExcelImportUtils.isRowEmpty(row)) continue;

                int numeroLigne = i + 1;
                total++;

                try {
                    UniteAdministrativeRequest request = mapperLigneUnite(row);

                    Set<ConstraintViolation<UniteAdministrativeRequest>> violations =
                            validator.validate(request);
                    if (!violations.isEmpty()) {
                        throw new IllegalArgumentException(
                                violations.iterator().next().getMessage()
                        );
                    }

                    create(request);
                    succes++;
                } catch (Exception e) {
                    echecs.add(new ImportLigneErreurResponse(numeroLigne, e.getMessage()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire le fichier Excel : " + e.getMessage());
        }

        return new ImportResultResponse(total, succes, echecs);
    }

    private UniteAdministrativeRequest mapperLigneUnite(Row row) {
        UniteAdministrativeRequest request = new UniteAdministrativeRequest();
        request.setCode(ExcelImportUtils.getString(row, 0));
        request.setLibelle(ExcelImportUtils.getString(row, 1));
        request.setIdTypeUnite(ExcelImportUtils.getString(row, 2));
        request.setCodeMinistere(ExcelImportUtils.getString(row, 3));
        request.setIdUniteParent(ExcelImportUtils.getString(row, 4));
        request.setNiveauHierarchique(ExcelImportUtils.getInt(row, 5));
        return request;
    }

    @Override
    public byte[] generateImportTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Unités administratives");

            Font fontEntete = workbook.createFont();
            fontEntete.setBold(true);
            fontEntete.setColor(org.apache.poi.ss.usermodel.IndexedColors.WHITE.getIndex());

            CellStyle styleEntete = workbook.createCellStyle();
            styleEntete.setFont(fontEntete);
            styleEntete.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.DARK_GREEN.getIndex());
            styleEntete.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);

            Row entete = sheet.createRow(0);
            for (int c = 0; c < COLONNES_IMPORT_UNITE.length; c++) {
                Cell cell = entete.createCell(c);
                cell.setCellValue(COLONNES_IMPORT_UNITE[c]);
                cell.setCellStyle(styleEntete);
                sheet.setColumnWidth(c, 20 * 256);
            }

            Row exemple = sheet.createRow(1);
            String[] valeursExemple = {
                    "DGB", "Direction Générale du Budget", "DIR_GEN", "MEF", "", "2"
            };
            for (int c = 0; c < valeursExemple.length; c++) {
                exemple.createCell(c).setCellValue(valeursExemple[c]);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Impossible de générer le modèle : " + e.getMessage());
        }
    }
}
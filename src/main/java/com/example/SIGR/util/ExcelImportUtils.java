package com.example.SIGR.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Lecture tolérante des cellules d'un fichier d'import (agents, unités
 * administratives...) : les fichiers Excel remplis à la main mélangent
 * souvent cellules texte et cellules "vraie date"/"vrai nombre" pour la
 * même colonne — DataFormatter lisse cette différence au lieu de forcer
 * l'utilisateur à un format de cellule précis.
 */
public final class ExcelImportUtils {

    private static final DataFormatter FORMATTER = new DataFormatter();
    private static final DateTimeFormatter DATE_FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ExcelImportUtils() {
    }

    public static String getString(Row row, int col) {
        if (row == null) return null;
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        String value = FORMATTER.formatCellValue(cell).trim();
        return value.isEmpty() ? null : value;
    }

    public static LocalDate getDate(Row row, int col) {
        if (row == null) return null;
        Cell cell = row.getCell(col);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        String raw = getString(row, col);
        if (raw == null) return null;

        try {
            return LocalDate.parse(raw, DATE_FR);
        } catch (Exception eFr) {
            try {
                return LocalDate.parse(raw);
            } catch (Exception eIso) {
                throw new IllegalArgumentException(
                        "Date invalide : " + raw + " (format attendu jj/mm/aaaa)"
                );
            }
        }
    }

    public static Integer getInt(Row row, int col) {
        String raw = getString(row, col);
        if (raw == null) return null;
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Nombre invalide : " + raw);
        }
    }

    public static boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            if (getString(row, c) != null) return false;
        }
        return true;
    }
}

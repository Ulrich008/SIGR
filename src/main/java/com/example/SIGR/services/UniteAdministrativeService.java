package com.example.SIGR.services;

import com.example.SIGR.dto.request.UniteAdministrativeRequest;
import com.example.SIGR.dto.response.ImportResultResponse;
import com.example.SIGR.dto.response.UniteAdministrativeResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UniteAdministrativeService {

    UniteAdministrativeResponse create(UniteAdministrativeRequest request);

    UniteAdministrativeResponse getByCode(String code);

    List<UniteAdministrativeResponse> getAll();

    UniteAdministrativeResponse update(String code, UniteAdministrativeRequest request);

    void delete(String code);

    /**
     * Import en masse d'unités administratives depuis un fichier Excel.
     * Chaque ligne est traitée indépendamment en réutilisant {@link #create}.
     */
    ImportResultResponse importFromExcel(MultipartFile file);

    /**
     * Modèle Excel (fichier .xlsx) attendu par {@link #importFromExcel}.
     */
    byte[] generateImportTemplate();
}
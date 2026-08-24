package com.example.SIGR.services;

import com.example.SIGR.dto.request.ControleSecondNiveauRequest;
import com.example.SIGR.dto.response.ConstatsRecommandationsResponse;
import com.example.SIGR.dto.response.ControleSecondNiveauResponse;
import com.example.SIGR.dto.response.LigneControleResponse;

import java.util.List;

public interface ControleSecondNiveauService {

    ControleSecondNiveauResponse create(ControleSecondNiveauRequest request);

    ControleSecondNiveauResponse getByCode(String code);

    List<ControleSecondNiveauResponse> getAll();

    ControleSecondNiveauResponse update(String code, ControleSecondNiveauRequest request);

    void delete(String code);

    ConstatsRecommandationsResponse getConstatsEtRecommandations(String codeUnite, String codeProcessus);

    /** Constat + analyse + recommandation toujours reliés, par ligne et par contrôle d'origine. */
    List<LigneControleResponse> getLignesDetaillees(String codeUnite, String codeProcessus);
}

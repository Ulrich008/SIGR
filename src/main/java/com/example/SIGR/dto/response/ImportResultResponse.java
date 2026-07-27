package com.example.SIGR.dto.response;

import java.util.List;

public class ImportResultResponse {

    private int totalLignes;
    private int succes;
    private List<ImportLigneErreurResponse> echecs;

    public ImportResultResponse(int totalLignes, int succes, List<ImportLigneErreurResponse> echecs) {
        this.totalLignes = totalLignes;
        this.succes = succes;
        this.echecs = echecs;
    }

    public int getTotalLignes() {
        return totalLignes;
    }

    public int getSucces() {
        return succes;
    }

    public List<ImportLigneErreurResponse> getEchecs() {
        return echecs;
    }
}

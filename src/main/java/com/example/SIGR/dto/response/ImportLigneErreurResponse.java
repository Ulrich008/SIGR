package com.example.SIGR.dto.response;

public class ImportLigneErreurResponse {

    private int ligne;
    private String message;

    public ImportLigneErreurResponse(int ligne, String message) {
        this.ligne = ligne;
        this.message = message;
    }

    public int getLigne() {
        return ligne;
    }

    public String getMessage() {
        return message;
    }
}

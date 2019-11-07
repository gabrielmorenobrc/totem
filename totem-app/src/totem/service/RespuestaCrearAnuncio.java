package totem.service;

import java.io.Serializable;

public class RespuestaCrearAnuncio implements Serializable {
    private String error;
    private AnuncioInfo anuncioInfo;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public AnuncioInfo getAnuncioInfo() {
        return anuncioInfo;
    }

    public void setAnuncioInfo(AnuncioInfo anuncioInfo) {
        this.anuncioInfo = anuncioInfo;
    }
}

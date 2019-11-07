package totem.service;

import java.io.Serializable;

public class RespuestaCrearTurnoOnline implements Serializable {
    private String error;
    private TurnoInfo turnoInfo;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public TurnoInfo getTurnoInfo() {
        return turnoInfo;
    }

    public void setTurnoInfo(TurnoInfo turnoInfo) {
        this.turnoInfo = turnoInfo;
    }
}

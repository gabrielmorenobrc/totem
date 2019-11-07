package totem.service;

import java.io.Serializable;

public class TurnoInfo implements Serializable {
    private String id;
    private TramiteInfo tramiteInfo;
    private String lugar;
    private String horario;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TramiteInfo getTramiteInfo() {
        return tramiteInfo;
    }

    public void setTramiteInfo(TramiteInfo tramiteInfo) {
        this.tramiteInfo = tramiteInfo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String buildDescripcion() {
        return tramiteInfo.getDescripcion() + " - " + getHorario();
    }
}

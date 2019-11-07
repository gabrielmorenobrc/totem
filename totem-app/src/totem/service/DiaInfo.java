package totem.service;

import java.io.Serializable;

public class DiaInfo implements Serializable {
    private Integer anio;
    private Integer mes;
    private Integer dia;
    private Boolean ocupado;
    private Boolean deshabilitado;
    private String tokenTransito;

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Boolean getOcupado() {
        return ocupado;
    }

    public void setOcupado(Boolean ocupado) {
        this.ocupado = ocupado;
    }

    public Boolean getDeshabilitado() {
        return deshabilitado;
    }

    public void setDeshabilitado(Boolean deshabilitado) {
        this.deshabilitado = deshabilitado;
    }

    public String getTokenTransito() {
        return tokenTransito;
    }

    public void setTokenTransito(String tokenTransito) {
        this.tokenTransito = tokenTransito;
    }
}

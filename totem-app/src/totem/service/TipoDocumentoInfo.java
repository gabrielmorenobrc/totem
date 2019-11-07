package totem.service;

import java.io.Serializable;

public class TipoDocumentoInfo implements Serializable {
    private String codigo;
    private String nombre;
    private Integer cantidadCaracteres;
    private Integer minimoCaracteres;

    public Integer getMinimoCaracteres() {
        return minimoCaracteres;
    }

    public void setMinimoCaracteres(Integer minimoCaracteres) {
        this.minimoCaracteres = minimoCaracteres;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public Integer getCantidadCaracteres() {
        return cantidadCaracteres;
    }

    public void setCantidadCaracteres(Integer cantidadCaracteres) {
        this.cantidadCaracteres = cantidadCaracteres;
    }
}

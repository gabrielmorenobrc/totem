package totem.service;

import java.io.Serializable;

public class AnuncioInfo implements Serializable {
    private SedeInfo sedeInfo;
    private CategoriaInfo categoriaInfo;
    private SubcategoriaInfo subcategoriaInfo;
    private TramiteInfo tramiteInfo;
    private String numeroAnuncio;
    private String nombreCliente;

    public TramiteInfo getTramiteInfo() {
        return tramiteInfo;
    }

    public void setTramiteInfo(TramiteInfo tramiteInfo) {
        this.tramiteInfo = tramiteInfo;
    }

    public String getNumeroAnuncio() {
        return numeroAnuncio;
    }

    public void setNumeroAnuncio(String numeroAnuncio) {
        this.numeroAnuncio = numeroAnuncio;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public SedeInfo getSedeInfo() {
        return sedeInfo;
    }

    public void setSedeInfo(SedeInfo sedeInfo) {
        this.sedeInfo = sedeInfo;
    }

    public CategoriaInfo getCategoriaInfo() {
        return categoriaInfo;
    }

    public void setCategoriaInfo(CategoriaInfo categoriaInfo) {
        this.categoriaInfo = categoriaInfo;
    }

    public SubcategoriaInfo getSubcategoriaInfo() {
        return subcategoriaInfo;
    }

    public void setSubcategoriaInfo(SubcategoriaInfo subcategoriaInfo) {
        this.subcategoriaInfo = subcategoriaInfo;
    }
}

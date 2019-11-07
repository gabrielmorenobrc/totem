package totem.webapp.app;

import totem.service.*;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class SessionData implements Serializable {
    private TipoDocumentoInfo tipoDocumentoInfo;
    private String numeroDocumento;
    private DatosPersona datosPersona;
    private AnuncioInfo anuncioInfo;
    private CategoriaInfo categoriaInfo;
    private TramiteInfo tramiteInfo;
    private SubcategoriaInfo subcategoriaInfo;
    private String selectedMenu;
    private DiaInfo diaInfo;
    private HorarioInfo horarioInfo;
    private SedeInfo sedeInfo;
    private String tipoPaso;
    private String idSede;
    private String tipoTurno;

    public TipoDocumentoInfo getTipoDocumentoInfo() {
        return tipoDocumentoInfo;
    }

    public void setTipoDocumentoInfo(TipoDocumentoInfo tipoDocumento) {
        this.tipoDocumentoInfo = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public AnuncioInfo getAnuncioInfo() {
        return anuncioInfo;
    }

    public void setAnuncioInfo(AnuncioInfo anuncioInfo) {
        this.anuncioInfo = anuncioInfo;
    }

    public void setCategoriaInfo(CategoriaInfo categoriaInfo) {
        this.categoriaInfo = categoriaInfo;
    }

    public CategoriaInfo getCategoriaInfo() {
        return categoriaInfo;
    }

    public TramiteInfo getTramiteInfo() {
        return tramiteInfo;
    }

    public void setTramiteInfo(TramiteInfo tramiteInfo) {
        this.tramiteInfo = tramiteInfo;
    }

    public SubcategoriaInfo getSubcategoriaInfo() {
        return subcategoriaInfo;
    }

    public void setSubcategoriaInfo(SubcategoriaInfo subcategoriaInfo) {
        this.subcategoriaInfo = subcategoriaInfo;
    }

    public void setSelectedMenu(String selectedMenu) {
        this.selectedMenu = selectedMenu;
    }

    public String getSelectedMenu() {
        return selectedMenu;
    }

    public void setDiaInfo(DiaInfo diaInfo) {
        this.diaInfo = diaInfo;
    }

    public DiaInfo getDiaInfo() {
        return diaInfo;
    }

    public void setHorarioInfo(HorarioInfo horarioInfo) {
        this.horarioInfo = horarioInfo;
    }

    public HorarioInfo getHorarioInfo() {
        return horarioInfo;
    }

    public SedeInfo getSedeInfo() {
        return sedeInfo;
    }

    public void setSedeInfo(SedeInfo sedeInfo) {
        this.sedeInfo = sedeInfo;
    }

    public DatosPersona getDatosPersona() {
        return datosPersona;
    }

    public void setDatosPersona(DatosPersona datosPersona) {
        this.datosPersona = datosPersona;
    }

    public void setTipoPaso(String tipoPaso) {
        this.tipoPaso = tipoPaso;
    }

    public String getTipoPaso() {
        return tipoPaso;
    }

    public void setIdSede(String idSede) {
        this.idSede = idSede;
    }

    public String getIdSede() {
        return idSede;
    }

    public String getTipoTurno() {
        return tipoTurno;
    }

    public void setTipoTurno(String tipoTurno) {
        this.tipoTurno = tipoTurno;
    }
}

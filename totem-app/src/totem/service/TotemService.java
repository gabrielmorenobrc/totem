package totem.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class TotemService implements Serializable {
    @Inject
    private TurnosClient turnosClient;

    public List<TurnoInfo> listTurnoInfoByDocumento(String codigoTipoDocumento, String numeroDocumento, String idSede) {
        return turnosClient.listTurnoInfoByDocumento(codigoTipoDocumento, numeroDocumento, idSede);
    }

    public RespuestaCrearAnuncio crearAnuncio(TurnoInfo turnoInfo) {
        return turnosClient.crearAnuncio(turnoInfo);
    }


    public List<CategoriaInfo> listCategoriaInfo(String tipocategoria, String idSede) {
        List<CategoriaInfo> list = turnosClient.listCategoriaInfo(tipocategoria, idSede);
        list.sort((Comparator.comparing(CategoriaInfo::getDescripcion)));
        return list;
    }

    public byte[] loadCategoriaImage(Long id) {
        try {
            return turnosClient.imagenCategoria(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SubcategoriaInfo> listSubcategoriaInfo(Long idCategoria, String tipoCategoria, String idSede) {
        return turnosClient.listSubcategoriaInfo(idCategoria, tipoCategoria, idSede);
    }

    public List<TramiteInfo> listTramiteInfo(Long idCategoria, String tipoCategoria, String idSede) {
        return turnosClient.listTramiteInfo(idCategoria, tipoCategoria, idSede);
    }

    public RespuestaCrearAnuncio crearAnuncioSinTurno(String idSede, String codigoTipoDocumento, String numeroDocumento, String idTramite) {
        return turnosClient.crearAnuncioSinTurno(idSede, codigoTipoDocumento, numeroDocumento, idTramite);
    }

    public List<DiaInfo> listDiaInfo(String idTramite, String idSede, Integer mes, Integer anio) {
        System.out.println("listDiaInfo: " + mes + "/" + anio);
        return turnosClient.listDiaInfo(idTramite, idSede, mes, anio);
    }

    public List<HorarioInfo> listHorarioInfo(String idTramite, String idSede, Date fecha, String token) {
        System.out.println("listHorarioInfo: " + fecha);
        return turnosClient.listHorarioInfo(idTramite, idSede, fecha, token);
    }


    public RespuestaCrearTurnoOnline crearTurnoOnline(String codigoTipoDocumento, String numeroDocumento, String  idTramite, String idSede, DiaInfo diaInfo, HorarioInfo horarioInfo) {
        return turnosClient.crearTurnoOnline(codigoTipoDocumento, numeroDocumento, idTramite, idSede, diaInfo.getAnio(), diaInfo.getMes(), diaInfo.getDia(), horarioInfo.getHora(), horarioInfo.getMinutos());
    }

    public List<SedeInfo> listSedeInfo(String idTramite) {
        return turnosClient.listSedeInfo(idTramite);
    }

    public SedeInfo findSedeInfo(String id) {
        return turnosClient.findSedeInfo(id);
    }

    public List<TipoDocumentoInfo> listTipoDocumentoInfo() {
        return turnosClient.listTipoDocumentoInfo();
    }
    public boolean documentoValido(String codDoc, String numDoc){
        return turnosClient.existeDocumento(codDoc, numDoc);
    }
}

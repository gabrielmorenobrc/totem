package totem.service;

import plataforma1.util.StreamUtils;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Alternative
public class TurnosClient implements Serializable {
    @Inject
    private TotemConfig totemConfig;

    public List<TurnoInfo> listTurnoInfoByDocumento(String tipoDocumento, String numeroDocumento, String idSede) {
        List<TurnoInfo> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            TramiteInfo tramiteInfo = new TramiteInfo();
            tramiteInfo.setDescripcion("Habilitaciones");
            tramiteInfo.setId("1l");
            TurnoInfo turnoInfo = new TurnoInfo();
            turnoInfo.setLugar("Acá");
            turnoInfo.setHorario("1" + i + ":00");
            turnoInfo.setTramiteInfo(tramiteInfo);
            list.add(turnoInfo);
        }
        return list;
    }

    public List<SedeInfo> listSedeInfo(String idTramite) {
        ArrayList<SedeInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SedeInfo info = new SedeInfo();
            info.setId(String.valueOf(i + 1));
            info.setDescripcion("Sede " + info.getId());
            list.add(info);
        }
        return list;
    }


    public RespuestaCrearAnuncio crearAnuncio(TurnoInfo turnoInfo) {
        RespuestaCrearAnuncio respuestaCrearAnuncio = new RespuestaCrearAnuncio();
        AnuncioInfo anuncioInfo = new AnuncioInfo();
        anuncioInfo.setNombreCliente("Melina Lezcano");
        anuncioInfo.setNumeroAnuncio("HABW23");
        anuncioInfo.setTramiteInfo(turnoInfo.getTramiteInfo());
        CategoriaInfo categoriaInfo = new CategoriaInfo();
        categoriaInfo.setDescripcion("Categoría");
        SubcategoriaInfo subcategoriaInfo = new SubcategoriaInfo();
        subcategoriaInfo.setDescripcion("Subcategoría");
        SedeInfo sedeInfo = new SedeInfo();
        sedeInfo.setDescripcion("La sede");
        anuncioInfo.setCategoriaInfo(categoriaInfo);
        anuncioInfo.setSubcategoriaInfo(subcategoriaInfo);
        anuncioInfo.setSedeInfo(sedeInfo);
        respuestaCrearAnuncio.setAnuncioInfo(anuncioInfo);
        return respuestaCrearAnuncio;
    }

    public List<CategoriaInfo> listCategoriaInfo(String tipocategoria, String idSede) {
      CategoriaInfo categoria1 = new CategoriaInfo();
        CategoriaInfo categoria2 = new CategoriaInfo();
        List<CategoriaInfo> lista = new ArrayList<CategoriaInfo>();
        categoria1.setActiva(true);
        categoria2.setActiva(true);
        categoria1.setDescripcion("Tránsito y Transporte");
        categoria2.setDescripcion(("Comercios e Industrias"));
        categoria1.setIcono("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");
        categoria2.setIcono("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");

        lista.add(categoria1);
        lista.add(categoria2);
        return lista;

    }

    public List<SubcategoriaInfo> listSubcategoriaInfo(Long idCategoria, String tipoCategoria, String idSede) {
        SubcategoriaInfo subcategoria1 = new SubcategoriaInfo();
        SubcategoriaInfo subcategoria2 = new SubcategoriaInfo();
        List<SubcategoriaInfo> lista = new ArrayList<SubcategoriaInfo>();
        subcategoria1.setDescripcion("Subcategoria 1");
        subcategoria2.setDescripcion(("Subcategoria 2"));
        lista.add(subcategoria1);
        lista.add(subcategoria2);
        return lista;
    }

    public List<TramiteInfo> listTramiteInfo(Long idSubcategoria, String tipoCategoria, String idSede) {
        ArrayList<TramiteInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TramiteInfo tramiteInfo = new TramiteInfo();
            tramiteInfo.setDescripcion("Trámite de prueba " + i);
            tramiteInfo.setId(String.valueOf(i));
            list.add(tramiteInfo);
        }
        return list;
    }

    public RespuestaCrearAnuncio crearAnuncioSinTurno(String idSede, String tipoDocumento, String numeroDocumento, String idTramite) {
        AnuncioInfo anuncioInfo = new AnuncioInfo();
        anuncioInfo.setNombreCliente("Melina Lezcano");
        anuncioInfo.setNumeroAnuncio("HABW23");
        TramiteInfo tramiteInfo = new TramiteInfo();
        tramiteInfo.setId(idTramite);
        tramiteInfo.setDescripcion("El trámite");
        CategoriaInfo categoriaInfo = new CategoriaInfo();
        categoriaInfo.setDescripcion("Categoría");
        SubcategoriaInfo subcategoriaInfo = new SubcategoriaInfo();
        subcategoriaInfo.setDescripcion("Subcategoría");
        SedeInfo sedeInfo = new SedeInfo();
        sedeInfo.setDescripcion("La sede");
        anuncioInfo.setCategoriaInfo(categoriaInfo);
        anuncioInfo.setSubcategoriaInfo(subcategoriaInfo);
        anuncioInfo.setTramiteInfo(tramiteInfo);
        anuncioInfo.setSedeInfo(sedeInfo);
        RespuestaCrearAnuncio respuesta = new RespuestaCrearAnuncio();
        respuesta.setAnuncioInfo(anuncioInfo);
        return respuesta;
    }


    public List<DiaInfo> listDiaInfo(String idTramite, String idSede, Integer mes, Integer anio) {
        ArrayList<DiaInfo> list = new ArrayList<DiaInfo>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, anio);
        calendar.set(Calendar.MONTH, mes - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek > 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 0 - dayOfWeek);
        }
        for (int i = 0; i < 35; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            DiaInfo diaInfo = new DiaInfo();
            diaInfo.setAnio(calendar.get(Calendar.YEAR));
            diaInfo.setMes(calendar.get(Calendar.MONTH) + 1);
            diaInfo.setDia(calendar.get(Calendar.DAY_OF_MONTH));
            diaInfo.setOcupado(false);
            diaInfo.setDeshabilitado(dayOfWeek == 1 || dayOfWeek == 7 );
            list.add(diaInfo);
        }
        return list;
    }

    public ArrayList<HorarioInfo> listHorarioInfo(String idTramite, String idSede, Date fecha, String token) {
        ArrayList<HorarioInfo> list = new ArrayList<>();
        int h = 8;
        int m = 0;
        for (int i = 0; i < 24; i++) {
            HorarioInfo info = new HorarioInfo();
            info.setHora(h);
            info.setMinutos(m);
            info.setOcupado(m == 0);
            list.add(info);
            m += 15;
            if (m == 60) {
                h++;
                m = 0;
            }
        }
        return list;
    }

    public byte[] imagenCategoria(Long idCategoria) throws IOException {
        return StreamUtils.loadResourceBytes(getClass(), "categoria.png");
    }

    public RespuestaCrearTurnoOnline crearTurnoOnline(String codigoTipoDocumento, String numeroDocumento, String idTramite,
                                                      String idSede, Integer anio, Integer mes, Integer dia, Integer hora, Integer minutos) {

        TramiteInfo tramiteInfo = new TramiteInfo();
        tramiteInfo.setId(idTramite);
        tramiteInfo.setDescripcion("Cambio de domicilio");

        TurnoInfo turnoInfo = new TurnoInfo();
        turnoInfo.setTramiteInfo(tramiteInfo);
        turnoInfo.setHorario("10:00");
        turnoInfo.setLugar("Sede");
        turnoInfo.setId("0");


        RespuestaCrearTurnoOnline respuesta = new RespuestaCrearTurnoOnline();
        respuesta.setTurnoInfo(turnoInfo);
        return respuesta;

    }

    public SedeInfo findSedeInfo(String id) {
        SedeInfo info = new SedeInfo();
        info.setId(id);
        info.setDescripcion("Billinghurst");
        return info;
    }

    public List<TipoDocumentoInfo> listTipoDocumentoInfo() {
        TipoDocumentoInfo info = new TipoDocumentoInfo();
        info.setCodigo("DNI");
        info.setNombre("DNI");
        info.setCantidadCaracteres(8);
        return Collections.singletonList(info);
    }
    public Boolean existeDocumento(String codDoc, String numDoc){
        return true;
    }
}

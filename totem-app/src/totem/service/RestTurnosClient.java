package totem.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import plataforma1.util.StreamUtils;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Alternative
public class RestTurnosClient extends TurnosClient {
    private static final Logger LOGGER = Logger.getLogger(RestTurnosClient.class.getName());
    @Inject
    private TotemConfig totemConfig;

    @Override
    public List<CategoriaInfo> listCategoriaInfo(String tipocategoria, String idSede) {
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/listcategorias");
        GenericType<List<CategoriaInfo>> genericType = new GenericType<List<CategoriaInfo>>() {
        };
        WebResource builder = resource.queryParam("tipocategoria", tipocategoria).queryParam("sedeid", idSede);
        LOGGER.log(Level.INFO, builder.toString());
        return builder.get(genericType);
    }

    @Override
    public List<SubcategoriaInfo> listSubcategoriaInfo(Long idCategoria, String tipoCategoria, String idSede) {
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/listsubcategorias");
        GenericType<List<SubcategoriaInfo>> genericType = new GenericType<List<SubcategoriaInfo>>() {
        };
        WebResource builder = resource.queryParam("categoriaid", idCategoria.toString())
                .queryParam("tipocategoria", tipoCategoria).queryParam("sedeid", idSede);
        LOGGER.log(Level.INFO, builder.toString());
        return builder.get(genericType);
    }

    @Override
    public List<TramiteInfo> listTramiteInfo(Long idSubcategoria, String tipocategoria, String idSede) {
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/listtramites");
        GenericType<List<TramiteInfo>> genericType = new GenericType<List<TramiteInfo>>() {
        };
        WebResource builder = resource.queryParam("subcategoriaid", idSubcategoria.toString())
                .queryParam("tipocategoria", tipocategoria).queryParam("sedeid", idSede);
        LOGGER.log(Level.INFO, builder.toString());
        return builder.get(genericType);
    }

    public List<SedeInfo> listSedeInfo(String idTramite) {
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/listsedes");
        GenericType<List<SedeInfo>> genericType = new GenericType<List<SedeInfo>>() {
        };
        return resource.queryParam("tramiteid", idTramite.toString()).get(genericType);
    }

    public ArrayList<DiaInfo> listDiaInfo(String idTramite, String idSede, Integer mes, Integer anio) {
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/listdias");
        GenericType<ArrayList<DiaInfo>> genericType = new GenericType<ArrayList<DiaInfo>>() {
        };
        WebResource builder = resource.queryParam("tramiteid", idTramite.toString())
                .queryParam("sedeid", idSede)
                .queryParam("anio", anio.toString())
                .queryParam("mes", mes.toString());
        LOGGER.log(Level.INFO, builder.toString());
        return builder.get(genericType);
    }

    public ArrayList<HorarioInfo> listHorarioInfo(String idTramite, String idSede, Date fecha, String token) {
        System.out.println("listHorarioInfo: " + fecha);
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/listhorarios");
        GenericType<ArrayList<HorarioInfo>> genericType = new GenericType<ArrayList<HorarioInfo>>() {
        };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        Integer year = calendar.get(Calendar.YEAR);
        Integer mes = calendar.get(Calendar.MONTH) + 1;
        Integer dia = calendar.get(Calendar.DAY_OF_MONTH);
        WebResource builder = resource.queryParam("tramiteid", idTramite.toString())
                .queryParam("sedeid", idSede)
                .queryParam("anio", year.toString())
                .queryParam("mes", mes.toString())
                .queryParam("dia", dia.toString());
        if(token != null){
            builder = builder.queryParam("token", token);
        }
        LOGGER.log(Level.INFO, builder.toString());
        return builder.get(genericType);
    }

    public List<TurnoInfo> listTurnoInfoByDocumento(String tipoDocumento, String numeroDocumento, String idSede) {
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/listturnos");
        GenericType<List<TurnoInfo>> genericType = new GenericType<List<TurnoInfo>>() {
        };
        WebResource builder = resource.queryParam("tipodocumento", tipoDocumento)
                .queryParam("numerodocumento", numeroDocumento)
                .queryParam("sedeid", idSede);
        LOGGER.log(Level.INFO, builder.toString());
        return builder.get(genericType);
    }

    public RespuestaCrearAnuncio crearAnuncio(TurnoInfo turnoInfo) {
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/anunciarturno");
        WebResource builder = resource.queryParam("turnoid", turnoInfo.getId());
        LOGGER.log(Level.INFO, builder.toString());
        String json = builder.get(String.class);
        LOGGER.log(Level.INFO, json);
        try {
            return new ObjectMapper().readValue(json.getBytes(), RespuestaCrearAnuncio.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RespuestaCrearAnuncio crearAnuncioSinTurno(String idSede, String codigoTipoDocumento, String numeroDocumento, String  idTramite) {
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/anunciarsinturno");
        WebResource builder = resource.queryParam("tipodocumento", codigoTipoDocumento)
                .queryParam("numerodocumento", numeroDocumento)
                .queryParam("tramiteid", idTramite)
                .queryParam("sedeid", idSede);
        LOGGER.log(Level.INFO, builder.toString());
        String json = builder.get(String.class);
        LOGGER.log(Level.INFO, json);
        try {
            return new ObjectMapper().readValue(json.getBytes(), RespuestaCrearAnuncio.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public RespuestaCrearTurnoOnline crearTurnoOnline(String codigoTipoDocumento, String numeroDocumento, String idTramite,
                                                      String idSede, Integer anio, Integer mes, Integer dia, Integer hora,
                                                      Integer minutos) {
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/pedirTurno");
        WebResource builder = resource.queryParam("tipodoc", codigoTipoDocumento)
                .queryParam("numerodoc", numeroDocumento)
                .queryParam("tramiteid", idTramite.toString())
                .queryParam("sedeid", idSede)
                .queryParam("anio", anio.toString())
                .queryParam("mes", mes.toString())
                .queryParam("dia", dia.toString())
                .queryParam("hora", hora.toString())
                .queryParam("minutos", minutos.toString());
        LOGGER.log(Level.INFO, builder.toString());
        return builder.get(RespuestaCrearTurnoOnline.class);
    }

    public byte[] imagenCategoria(Long idCategoria) throws IOException {
        return StreamUtils.loadResourceBytes(getClass(), "categoria.png");
    }

    @Override
    public SedeInfo findSedeInfo(String id) {
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/getsede");
        WebResource builder = resource.queryParam("sedeid", id);
        LOGGER.log(Level.INFO, builder.toString());
        return builder.get(SedeInfo.class);
    }

    @Override
    public List<TipoDocumentoInfo> listTipoDocumentoInfo() {
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/listiposdocumentos");
        GenericType<List<TipoDocumentoInfo>> genericType = new GenericType<List<TipoDocumentoInfo>>() {
        };
        return resource.get(genericType);
    }

    @Override
    public Boolean existeDocumento(String codDoc, String numDoc){
        Client client = Client.create();
        WebResource resource = client.resource(totemConfig.getTurneroUriBase() + "/existepersona");
        WebResource builder = resource.queryParam("tipodocumento", codDoc).queryParam("numerodocumento", numDoc);
        LOGGER.log(Level.INFO, builder.toString());
        return builder.get(Boolean.class);
    }
}


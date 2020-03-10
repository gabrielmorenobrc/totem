package totem.webapp.app;

import org.apache.wicket.ajax.AjaxRequestTarget;
import totem.service.RespuestaCrearAnuncio;
import totem.service.TotemService;
import totem.service.TurnoInfo;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AtencionPanel extends TotemPanel {
    @Inject
    private SessionData sessionData;
    @Inject
    private TotemService totemService;


    public AtencionPanel(String id) {
        super(id);
        setOutputMarkupId(true);
        addIdentificacion();
    }

    private void addIdentificacion() {
        IdentificacionPanel panel = new IdentificacionPanel("contenido", "ANUNCIAR TURNO PRESENCIAL",false, new IdentificacionPanel.Callback() {
            @Override
            public void onBuscar(AjaxRequestTarget ajaxRequestTarget) {
                addTurnos();
                ajaxRequestTarget.add(AtencionPanel.this);
            }
        });
        addOrReplace(panel);
    }

    private void addTurnos() {
        SeleccionTurnoPanel panel = new SeleccionTurnoPanel("contenido", new SeleccionTurnoPanel.Callback() {
            @Override
            public void onTurnoSeleccionado(TurnoInfo turnoInfo, AjaxRequestTarget ajaxRequestTarget) {

                RespuestaCrearAnuncio respuesta = totemService.crearAnuncio(turnoInfo);
                if (respuesta.getError() == null) {
                    sessionData.setAnuncioInfo(respuesta.getAnuncioInfo());
                    addAnuncio();
                    ajaxRequestTarget.appendJavaScript("completeProgressBar()");
                    ajaxRequestTarget.add(AtencionPanel.this);
                } else {
                    Logger.getLogger(AtencionPanel.class.getName()).log(Level.WARNING, respuesta.getError());
                    addTurnoOcupado(respuesta.getError());
                    ajaxRequestTarget.appendJavaScript("isErrorProgressBar()");
                    ajaxRequestTarget.add(AtencionPanel.this);
                }
            }

            @Override
            public void onOtroTramite(AjaxRequestTarget ajaxRequestTarget) {
                addOtroTramite();
                ajaxRequestTarget.appendJavaScript("initProgressBar(" + 6 + ", " + 34 + ")");
                ajaxRequestTarget.add(AtencionPanel.this);
            }
        });
        addOrReplace(panel);
    }

    private void addOtroTramite() {
        sessionData.setTipoPaso("anunciarseSinTurno");
        sessionData.setTipoTurno("PRESENCIAL");
        TramitePanel panel = new TramitePanel("contenido", new TramitePanel.Callback() {
            @Override
            public void onTramiteSeleccionado(AjaxRequestTarget ajaxRequestTarget) {
                RespuestaCrearAnuncio respuesta = totemService.crearAnuncioSinTurno(sessionData.getIdSede(),
                        sessionData.getTipoDocumentoInfo().getCodigo(),
                        sessionData.getNumeroDocumento(), sessionData.getTramiteInfo().getId());
                if (respuesta.getError() == null) {
                    sessionData.setAnuncioInfo(respuesta.getAnuncioInfo());
                    addAnuncio();
                } else {
                    Logger.getLogger(AtencionPanel.class.getName()).log(Level.WARNING, respuesta.getError());
                    addTurnoOcupado(respuesta.getError());
                    ajaxRequestTarget.appendJavaScript("isErrorProgressBar()");
                    ajaxRequestTarget.add(AtencionPanel.this);
                }
                ajaxRequestTarget.add(AtencionPanel.this);
            }
        });
        addOrReplace(panel);
    }

    private void addAnuncio() {
        sessionData.setTipoPaso("anunciarseSinTurno");
        AnuncioPanel panel = new AnuncioPanel("contenido");
        addOrReplace(panel);
    }

    private void addTurnoOcupado(String msg) {
        final TurnoOcupadoPanel panel = new TurnoOcupadoPanel("contenido", msg, new TurnoOcupadoPanel.Callback() {
            @Override
            public void onSelected(AjaxRequestTarget ajaxRequestTarget) {
                addTurnos();
                ajaxRequestTarget.add(AtencionPanel.this);
            }
        });
        addOrReplace(panel);
    }
}

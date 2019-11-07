package totem.webapp.app;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import totem.service.RespuestaCrearTurnoOnline;
import totem.service.TotemService;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TurnoOnlinePanel extends TotemPanel {
    private Panel currentPanel;
    private String tipocategoria;
    @Inject
    private SessionData sessionData;
    @Inject
    private TotemService totemService;

    public TurnoOnlinePanel(String id) {
        super(id);
        addIdentificacion();
        setOutputMarkupId(true);
    }

    private void addIdentificacion() {
        IdentificacionPanel panel = new IdentificacionPanel("contenido", "CREAR TURNO ONLINE", true, new IdentificacionPanel.Callback() {
            @Override
            public void onBuscar(AjaxRequestTarget ajaxRequestTarget) {
                addTramite();
                ajaxRequestTarget.appendJavaScript("initProgressBar(" + 8 + ", " + 12 + ")");
                ajaxRequestTarget.add(TurnoOnlinePanel.this);
            }
        });
        addOrReplace(panel);
    }

    private void addTramite() {
        sessionData.setTipoPaso("turnoOnline");
        sessionData.setTipoTurno("ONLINE");
        TramitePanel panel = new TramitePanel("contenido", new TramitePanel.Callback() {
            @Override
            public void onTramiteSeleccionado(AjaxRequestTarget ajaxRequestTarget) {
                addLugares();
                ajaxRequestTarget.add(TurnoOnlinePanel.this);
            }
        });
        addOrReplace(panel);
    }

    private void addCalendario() {
        final CalendarioComponent panel = new CalendarioComponent("contenido", new CalendarioComponent.Callback() {
            @Override
            public void onSelected(AjaxRequestTarget ajaxRequestTarget) {
                addHorarios();
                ajaxRequestTarget.add(TurnoOnlinePanel.this);
            }
        });
        addOrReplace(panel);
        currentPanel = panel;
    }

    private void addHorarios() {
        final HorariosComponent panel = new HorariosComponent("contenido", new HorariosComponent.Callback() {
            @Override
            public void onSelected(AjaxRequestTarget ajaxRequestTarget) {
                RespuestaCrearTurnoOnline respuesta = totemService.crearTurnoOnline(sessionData.getTipoDocumentoInfo().getCodigo(),
                        sessionData.getNumeroDocumento(),
                        sessionData.getTramiteInfo().getId(), sessionData.getSedeInfo().getId(),
                        sessionData.getDiaInfo(), sessionData.getHorarioInfo());
                if (respuesta.getError() == null) {
                    ajaxRequestTarget.appendJavaScript("completeProgressBar()");
                    addTurnoConfirmado();
                } else {
                    Logger.getLogger(TurnoOnlinePanel.class.getName()).log(Level.WARNING, respuesta.getError());
                    ajaxRequestTarget.appendJavaScript("isErrorProgressBar()");
                    addTurnoOcupado(respuesta.getError());
                }
                ajaxRequestTarget.add(TurnoOnlinePanel.this);
            }
        });
        addOrReplace(panel);
        currentPanel = panel;
    }

    private void addLugares() {
        final SeleccionLugarPanel panel = new SeleccionLugarPanel("contenido", new SeleccionLugarPanel.Callback() {
            @Override
            public void onSelected(AjaxRequestTarget ajaxRequestTarget) {
                addCalendario();
                ajaxRequestTarget.add(TurnoOnlinePanel.this);
            }
        });
        addOrReplace(panel);
        currentPanel = panel;
    }

    private void addTurnoOcupado(String msg) {
        final TurnoOcupadoPanel panel = new TurnoOcupadoPanel("contenido", msg, new TurnoOcupadoPanel.Callback() {
        @Override
        public void onSelected(AjaxRequestTarget ajaxRequestTarget) {
            addCalendario();
            ajaxRequestTarget.add(TurnoOnlinePanel.this);
        }
    });
        addOrReplace(panel);
        currentPanel = panel;
    }

    private void addTurnoConfirmado() {
        final TurnoConfirmadoPanel panel = new TurnoConfirmadoPanel("contenido");
        addOrReplace(panel);
        currentPanel = panel;
    }
}

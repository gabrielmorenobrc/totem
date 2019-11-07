package totem.webapp.app;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import plataforma1.wicket.semantic.NotifierProvider;
import totem.service.TotemService;
import totem.service.TurnoInfo;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeleccionTurnoPanel extends Panel {
    @Inject
    private SessionData sessionData;
    @Inject
    private TotemService totemService;
    private final Callback callback;
    private WebMarkupContainer turnosContainer;
    private ProgressBar progressBar;
    @Inject
    private NotifierProvider notifierProvider;

    public interface Callback extends Serializable {
        void onTurnoSeleccionado(TurnoInfo turnoInfo, AjaxRequestTarget ajaxRequestTarget);
        void onOtroTramite(AjaxRequestTarget ajaxRequestTarget);
    }

    public SeleccionTurnoPanel(String id, Callback callback) {
        super(id);
        this.callback = callback;
        addProgressBar();
        addTurnos();
        addOtroTramite();
    }

    private void addProgressBar(){
        WebMarkupContainer progressBar = new WebMarkupContainer("ProgressBar");
        progressBar.setOutputMarkupId(true);
        progressBar.add( new ProgressBar("bar", "Turno"));
        add(progressBar);
    }

    private void addOtroTramite() {
        WebMarkupContainer otroItem = new WebMarkupContainer("otro");
        turnosContainer.add(otroItem);
        otroItem.add(new AjaxLink<Void>("link") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                ajaxRequestTarget.appendJavaScript("hideDimmer();");
                callback.onOtroTramite(ajaxRequestTarget);
            }
        });
    }

    private void addTurnos() {
        turnosContainer = new WebMarkupContainer("turnos");
        turnosContainer.setOutputMarkupId(true);
        ListView<TurnoInfo> listView = new ListView<TurnoInfo>("item",
                totemService.listTurnoInfoByDocumento(sessionData.getTipoDocumentoInfo().getCodigo(),
                        sessionData.getNumeroDocumento(), sessionData.getIdSede())) {
            @Override
            protected void populateItem(ListItem<TurnoInfo> listItem) {
                AjaxLink<Void> link = new AjaxLink<Void>("link") {
                    @Override
                    public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                        try {
                            callback.onTurnoSeleccionado(listItem.getModelObject(), ajaxRequestTarget);
                        } catch (Throwable t) {
                            Logger.getLogger(SeleccionTurnoPanel.class.getName()).log(Level.WARNING, "Error", t);
                            notifierProvider.getNotifier(getPage()).notify("Error", "Error inesperado");
                        } finally {
                            ajaxRequestTarget.appendJavaScript("hideDimmer();");
                        }
                    }
                };
                listItem.add(link);
                link.add(new Label("label", "Trámite: " + listItem.getModelObject().buildDescripcion()));
            }
        };
        turnosContainer.add(listView);
        add(turnosContainer);
    }
}

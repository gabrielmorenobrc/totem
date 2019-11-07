package totem.webapp.app;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import plataforma1.wicket.semantic.NotifierProvider;
import totem.service.TotemService;
import totem.service.TramiteInfo;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeleccionTramitePanel extends TotemPanel {
    @Inject
    private TotemService totemService;
    @Inject
    private SessionData sessionData;
    @Inject
    private NotifierProvider notifierProvider;

    private final Callback callback;
    private WebMarkupContainer container;

    public interface Callback extends Serializable {
        void onSelected(AjaxRequestTarget ajaxRequestTarget);
    }
    
    public SeleccionTramitePanel(String id, Callback callback) {
        super(id);
        this.callback = callback;
        addTramites();
    }

    private void addTramites() {
        container = new WebMarkupContainer("list");
        container.setOutputMarkupId(true);
        add(container);
        ListView<TramiteInfo> listView = new ListView<TramiteInfo>("item",
                totemService.listTramiteInfo(sessionData.getSubcategoriaInfo().getId(), sessionData.getTipoTurno(),
                        sessionData.getIdSede())) {
            @Override
            protected void populateItem(ListItem<TramiteInfo> listItem) {
                if (listItem.getModelObject().equals(sessionData.getTramiteInfo())) {
                    listItem.add(new AttributeAppender("class", " active"));
                }
                AjaxLink<Void> content = new AjaxLink<Void>("link") {
                    @Override
                    public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                        try {
                            sessionData.setTramiteInfo(listItem.getModelObject());
                            callback.onSelected(ajaxRequestTarget);
                            ajaxRequestTarget.add(container);
                        } catch (Throwable t) {
                            Logger.getLogger(SeleccionTramitePanel.class.getName()).log(Level.WARNING, "Error", t);
                            notifierProvider.getNotifier(getPage()).notify("Error", "Error inesperado");
                        } finally {
                            ajaxRequestTarget.appendJavaScript("hideDimmer();");
                        }
                    }
                };
                listItem.add(content);
                content.add(new Label("label", listItem.getModelObject().getDescripcion()));
            }
        };
        container.add(listView);
    }

}
    


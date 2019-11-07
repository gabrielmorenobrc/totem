package totem.webapp.app;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import plataforma1.wicket.semantic.NotifierProvider;
import totem.service.SubcategoriaInfo;
import totem.service.TotemService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeleccionSubcategoriaPanel extends TotemPanel {
    @Inject
    private SessionData sessionData;
    @Inject
    private TotemService totemService;

    private final SeleccionSubcategoriaPanel.Callback callback;
    private WebMarkupContainer container;
    @Inject
    private NotifierProvider notifierProvider;

    public interface Callback extends Serializable {
        void onSelected(AjaxRequestTarget ajaxRequestTarget);
    }

    public SeleccionSubcategoriaPanel(String id, SeleccionSubcategoriaPanel.Callback callback) {
        super(id);
        this.callback = callback;
        addSubcategorias();
    }

    private void addSubcategorias() {
        container = new WebMarkupContainer("list");
        container.setOutputMarkupId(true);
        add(container);
        ListView<SubcategoriaInfo> listView = new ListView<SubcategoriaInfo>("item",
                totemService.listSubcategoriaInfo(sessionData.getCategoriaInfo().getId(), sessionData.getTipoTurno(),
                        sessionData.getIdSede())) {
            @Override
            protected void populateItem(ListItem<SubcategoriaInfo> listItem) {
                AjaxLink<Void> content = new AjaxLink<Void>("link") {
                    @Override
                    public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                        try {
                            sessionData.setSubcategoriaInfo(listItem.getModelObject());
                            callback.onSelected(ajaxRequestTarget);
                            ajaxRequestTarget.add(container);
                        } catch (Throwable t) {
                            Logger.getLogger(SeleccionSubcategoriaPanel.class.getName()).log(Level.WARNING, "Error", t);
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
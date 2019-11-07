package totem.webapp.app;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import plataforma1.wicket.semantic.NotifierProvider;
import totem.service.CategoriaInfo;
import totem.service.TotemService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeleccionCategoriaPanel extends TotemPanel {
    @Inject
    private SessionData sessionData;
    @Inject
    private TotemService totemService;

    private final Callback callback;
    private WebMarkupContainer container;
    @Inject
    private NotifierProvider notifierProvider;

    public interface Callback extends Serializable {
        void onSelected(AjaxRequestTarget ajaxRequestTarget);
    }
    public SeleccionCategoriaPanel(String id, Callback callback) {
        super(id);
        this.callback = callback;
        sessionData.setSubcategoriaInfo(null);
        addCategorias();
    }

    private void addCategorias() {
        container = new WebMarkupContainer("categorias");
        container.setOutputMarkupId(true);
        add(container);
        List<CategoriaInfo> list = totemService.listCategoriaInfo(sessionData.getTipoTurno(), sessionData.getIdSede());
        ListView<CategoriaInfo> listView = new ListView<CategoriaInfo>("item", list) {
            @Override
            protected void populateItem(ListItem<CategoriaInfo> listItem) {
                if (listItem.getModelObject().equals(sessionData.getCategoriaInfo())) {
                    listItem.add(new AttributeAppender("class", " active"));
                }
                AjaxLink<Void> content = new AjaxLink<Void>("content") {
                    @Override
                    public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                        try {
                        sessionData.setCategoriaInfo(listItem.getModelObject());
                        callback.onSelected(ajaxRequestTarget);
                        ajaxRequestTarget.add(container);
                        } catch (Throwable t) {
                            Logger.getLogger(SeleccionCategoriaPanel.class.getName()).log(Level.WARNING, "Error", t);
                            notifierProvider.getNotifier(getPage()).notify("Error", "Error inesperado");
                        } finally {
                            ajaxRequestTarget.appendJavaScript("hideDimmer()");
                        }
                    }
                };
                listItem.add(content);
                Image img = new Image("img", "");
                img.add(new AttributeModifier("src", "data:image/jpeg;base64," + listItem.getModelObject().getIcono()));
                img.add(new AttributeModifier("class", "centered image tiny ui"));
                content.add(img);
                content.add(new Label("label", listItem.getModelObject().getDescripcion()));
            }
        };
        container.add(listView);
    }
}

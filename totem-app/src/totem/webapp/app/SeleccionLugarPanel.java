package totem.webapp.app;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import plataforma1.wicket.semantic.NotifierProvider;
import totem.service.SedeInfo;
import totem.service.TotemService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeleccionLugarPanel extends Panel {
    @Inject
    private TotemService totemService;
    @Inject
    private SessionData sessionData;

    private final Callback callback;
    private WebMarkupContainer container;
    @Inject
    private NotifierProvider notifierProvider;

    public interface Callback extends Serializable {
        void onSelected(AjaxRequestTarget ajaxRequestTarget);
    }
    
    public SeleccionLugarPanel(String id, Callback callback) {
        super(id);
        this.callback = callback;
        addProgressBar();
        addTramites();
    }

    private void addProgressBar(){
        WebMarkupContainer progressBar = new WebMarkupContainer("ProgressBar");
        progressBar.setOutputMarkupId(true);
        progressBar.add( new ProgressBar("bar", "Lugar"));
        add(progressBar);
    }

    private void addTramites() {
        container = new WebMarkupContainer("list");
        container.setOutputMarkupId(true);
        add(container);
        ListView<SedeInfo> listView = new ListView<SedeInfo>("item", totemService.listSedeInfo(sessionData.getTramiteInfo().getId())) {
            @Override
            protected void populateItem(ListItem<SedeInfo> listItem) {
                if (listItem.getModelObject().equals(sessionData.getSedeInfo())) {
                    listItem.add(new AttributeAppender("class", " active"));
                }
                AjaxLink<Void> content = new AjaxLink<Void>("link") {
                    @Override
                    public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                        try {
                        sessionData.setSedeInfo(listItem.getModelObject());
                        callback.onSelected(ajaxRequestTarget);
                        ajaxRequestTarget.add(container);
                        } catch (Throwable t) {
                            Logger.getLogger(SeleccionLugarPanel.class.getName()).log(Level.WARNING, "Error", t);
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
    

